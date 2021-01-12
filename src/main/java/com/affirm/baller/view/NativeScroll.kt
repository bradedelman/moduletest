package com.affirm.baller.view

import android.view.View
import android.widget.ScrollView
import com.affirm.baller.platform.Native

class NativeScroll constructor(context: Native) : NativeView(context) {

    override fun create():View?
    {
        var v = ScrollView(_native._context);
        return v;
    }

    fun layoutChildren() {
    }

}

