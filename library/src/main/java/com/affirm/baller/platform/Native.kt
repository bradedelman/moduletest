package com.affirm.baller.platform

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import com.affirm.baller.service.NativeHost
import com.affirm.baller.service.NativeHttp
import com.affirm.baller.service.NativeStore
import com.affirm.baller.utils.CallNamedMethod
import com.affirm.baller.utils.NativeInterface
import com.affirm.baller.view.NativeView
import kotlin.reflect.KClass


// APK 4.3 MB before adding Duktape
// APK 4.9 MB after adding Duktape  - nice!

class Native constructor(
    context: Context,
    hostView: BallerView
) : NativeInterface {
    var _context: Context = context;
    var _javascriptEngine: JavascriptEngine = JavascriptEngine();
    var _views: HashMap<String, NativeView> = HashMap();
    var _nativeId: String = "NATIVE";
    var _hostView: BallerView = hostView;
    var _services: HashMap<String, Any> = HashMap();
    var _fonts: NativeFont = NativeFont(context);

    fun addService(name: String, service: Any)
    {
        _services.put(name, service);
    }

    fun jsCreate(jsTypeId: String): NativeView?
    {
        var s: String = "Baller.create('" + _nativeId + "', '" + jsTypeId + "')";
        var viewId: String =_javascriptEngine.evaluate(s) as String
        var nv: NativeView? = _views.get(viewId);
        return nv;
    }

    // TODO: not called by NativeView jsCall because not sure how to pass along the varargs
    fun jsCall(_id: String, method: String, vararg args: Any):Any
    {
        var s: String = "Baller.call('" + _nativeId + "', '" + _id + "', '" +  method + "'";
        for (arg in args) {
            if (arg is String) {
                s += ", '" + arg.toString() + "'"
            } else {
                s += ", " + arg.toString()
            }
        }
        s += ")";
        return _javascriptEngine.evaluate(s);
    }

    fun getRootView(): View {
        var v:View = _hostView;
        while (v.parent != null && (v.parent as? View) != null) {
            v = v.parent as View;
        }
        return v;
    }

    // TODO: remove this hack, find better way to size root native view to host view
    var once: Boolean = true;
    fun fixSize(nv: NativeView) {
        _hostView.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
            if (once) {
                once = false;
                nv.setBounds(left, top, right - left, bottom - top);

                var realWidth = right - left;
                var realHeight = bottom - top;
                var scale = realWidth.toFloat() / _hostView._scaledWidth.toFloat();
                var scaledHeight = (bottom - top).toFloat() / scale;

                var dx = realWidth - _hostView._scaledWidth;
                var dy = realHeight - scaledHeight;

                var tx = scale * dx.toFloat() / 2.0f;
                var ty = scale * dy.toFloat() / 2.0f;

                _hostView.setScaleX(scale);
                _hostView.setScaleY(scale);
                _hostView.translationX = tx;
                _hostView.translationY = ty;


                _hostView._nv?.jsCall("doLayout", _hostView._scaledWidth, scaledHeight.toInt());

                // TODO revisit scale/kebyoard code
                val parentView = getRootView();
                val rectOrig = Rect()

                parentView.getWindowVisibleDisplayFrame(rectOrig)

                parentView.viewTreeObserver.addOnGlobalLayoutListener(object :
                        OnGlobalLayoutListener {
                    private var alreadyOpen = false
                    private val defaultKeyboardHeightDP = 100
                    private val EstimatedKeyboardDP =
                            defaultKeyboardHeightDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
                    private val rect = Rect()
                    override fun onGlobalLayout() {
                        val estimatedKeyboardHeight = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                EstimatedKeyboardDP.toFloat(),
                                parentView.resources.displayMetrics
                        )
                                .toInt()
                        parentView.getWindowVisibleDisplayFrame(rect)
                        val heightDiff = parentView.rootView.height - (rect.bottom - rect.top)
                        val isShown = heightDiff >= estimatedKeyboardHeight
                        if (isShown == alreadyOpen) {
                            Log.i("Keyboard state", "Ignoring global layout change...")
                            return
                        }
                        alreadyOpen = isShown
                        if (isShown) {
                            // keyboard is up

                            var keyboardHeight = (rectOrig.height() - rect.height());
                            var windowHeight = rectOrig.height();

                            var field = (_context as Activity).currentFocus;


                            // so... if bottom of field is greater than the window height - the keyboard height, we need to scootch it up... so that it isn't
                            var usableHeight = windowHeight - keyboardHeight;

                            if (field != null) {
                                val offsetViewBounds = Rect()
                                field.getDrawingRect(offsetViewBounds)

                                var a: Rect = Rect();
                                parentView.getWindowVisibleDisplayFrame(a);
                                var b: Rect = Rect();
                                field.getGlobalVisibleRect(b);
                                Log.d("baller", "foo");

                                if (b.bottom > a.bottom) {
                                    // add small buffer - 5% of keyboard height


                                    var target = ty - (b.bottom - a.bottom) - (keyboardHeight * 0.05).toInt();

                                    ObjectAnimator.ofFloat(_hostView, "translationY", target).apply {
                                        duration = 100
                                        start()
                                    }

                                }
                            }



                            Log.d("baller", "open");
                        } else {
                            // keyboard is down

                            ObjectAnimator.ofFloat(_hostView, "translationY", ty.toFloat()).apply {
                                duration = 100
                                start()
                            }

                            Log.d("baller", "closed");
                        }
                    }
                })
            }
        }
    }

    fun call(id: String, method: String, args: Array<Any>):Any?
    {
        var v: NativeView? = _views.get(id);
        if (v != null) {
            return CallNamedMethod.call(v, method, args.asList());
        }
        return "";
    }

    override fun call6(id: String, method: String, a: Any, b: Any, c: Any, d: Any, e: Any, f: Any):Any? {
        return call(id, method, arrayOf(a, b, c, d, e, f));
    }

    override fun call5(id: String, method: String, a: Any, b: Any, c: Any, d: Any, e: Any):Any? {
        return call(id, method, arrayOf(a, b, c, d, e));
    }

    override fun call4(id: String, method: String, a: Any, b: Any, c: Any, d: Any):Any? {
        return call(id, method, arrayOf(a, b, c, d));
    }

    override fun call3(id: String, method: String, a: Any, b: Any, c: Any):Any? {
        return call(id, method, arrayOf(a, b, c));
    }

    override fun call2(id: String, method: String, a: Any, b: Any):Any? {
        return call(id, method, arrayOf(a, b));
    }

    override fun call1(id: String, method: String, a: Any):Any? {
        return call(id, method, arrayOf(a));
    }

    override fun call0(id: String, method: String):Any? {
        return call(id, method, arrayOf());
    }

    override fun callAPI2(apiName: String, method: String, a: Any, b: Any):Any?
    {
        var api: Any? = _services[apiName];
        if (api != null) {
            return CallNamedMethod.call(api, method, arrayOf(a, b).asList());
        }
        return null;
    }

    override fun callAPI1(apiName: String, method: String, a: Any):Any?
    {
        var api: Any? = _services[apiName];
        if (api != null) {
            return CallNamedMethod.call(api, method, arrayOf(a).asList());
        }
        return null;
    }

}
