package com.affirm.baller.view

import android.view.View
import com.affirm.baller.platform.Native
import com.affirm.baller.utils.LayoutView

open class NativeView constructor(native: Native) {

    var _e: View? = null;
    var _native: Native = native;
    var _id: String = "";

    fun setView(e: View)
    {
        _e = e;
    }

    open fun create():View?
    {
        var v: LayoutView = LayoutView(_native._context);
        return v;
    }

    fun jsCall(method: String, vararg args: Any)
    {
        var s: String = "Baller.call('" + _native._nativeId + "', '" + _id + "', '" +  method + "'";
        for (arg in args) {
            if (arg is String) {
                s += ", '" + arg.toString() + "'"
            } else {
                s += ", " + arg.toString()
            }
        }
        s += ")";
        _native._javascriptEngine.evaluate(s);
    }

    fun setBounds(x: Int, y: Int, w: Int, h: Int)
    {
        var l: LayoutView.LayoutParams = LayoutView.LayoutParams(x, y, w, h);
        _e?.layoutParams = l;
    }
}
