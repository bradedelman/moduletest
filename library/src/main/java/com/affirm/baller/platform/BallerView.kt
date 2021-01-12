package com.affirm.baller.platform

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.affirm.baller.service.NativeHost
import com.affirm.baller.service.NativeHttp
import com.affirm.baller.service.NativeStore
import com.affirm.baller.utils.Console
import com.affirm.baller.utils.ConsoleInterface
import com.affirm.baller.utils.LayoutView
import com.affirm.baller.utils.NativeInterface
import com.affirm.baller.view.NativeView

open class BallerView constructor(context: Context, script: String, scaledWidth: Int) : LayoutView(context) {

    interface BallerViewInterface {
        fun  onEvent(name: String, value: String);
    }

    var _scaledWidth: Int = scaledWidth;
    var _nv: NativeView? = null;
    var _delegate: BallerViewInterface? = null;

    init {
        // remove .js
        var script2 = script.substring(0, script.length-3);
        create(context, this, script2);
    }

    fun create(context: Context,
               parent: LayoutView,
               path: String): View
    {
        var params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        parent.layoutParams = params
        parent.setBackgroundColor(Color.parseColor("#deb887"));

        var javascriptEngine: JavascriptEngine = JavascriptEngine();
        var native: Native = Native(context, javascriptEngine, this);

        // register services
        native.addService("NativeHttp", NativeHttp(native));
        native.addService("NativeStore", NativeStore());
        native.addService("NativeHost", NativeHost(native));

        javascriptEngine.set("console", ConsoleInterface::class.java, Console());
        javascriptEngine.set("Native", NativeInterface::class.java, native);

        javascriptEngine.evaluate(EmbeddedScripts.Require);
        javascriptEngine.evaluate(EmbeddedScripts.json2);
        load(context, javascriptEngine, path + ".js");
        javascriptEngine.evaluate(EmbeddedScripts.Baller);

        // inject
        var t: String = "Baller.getNative = function() {return Native;}";
        javascriptEngine.evaluate(t);

        // initialize
        var s: String = "Baller.init('" + path + "', '" + native._nativeId + "')";
        javascriptEngine.evaluate(s);

        return parent;
    }

    fun load(context: Context, javascriptEngine: JavascriptEngine, path: String) {
        val code = context.assets.open(path).bufferedReader().use {
            it.readText()
        }
        try {
            javascriptEngine.evaluate(code);
        } catch (e: Exception) {
            Log.d("baller", e.toString());
        } finally {
        }
    }
}