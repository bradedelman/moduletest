package com.affirm.baller.view

import android.graphics.Color
import android.view.View
import com.affirm.baller.platform.Native
import com.affirm.baller.utils.LayoutView

class NativeDiv constructor(context: Native) : NativeView(context), LayoutView.LayoutViewEventHandler {

    override fun create(): View?
    {
        var v: LayoutView = LayoutView(_native._context);
        v._handler = this;
        return v;
    }

    fun setBgColor(color:String)
    {
        var v: LayoutView = this._e as LayoutView;

        var c: Int = Color.parseColor(color);
        if (v != null) {
            v.setBackgroundColor(c);
        }
    }

    override fun onUp(x: Int, y: Int)
    {
        jsCall("onUp", x, y);
    }
}
