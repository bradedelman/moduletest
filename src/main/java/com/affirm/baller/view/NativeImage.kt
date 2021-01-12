package com.affirm.baller.view

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.affirm.baller.platform.Native
import com.bumptech.glide.Glide

class NativeImage constructor(context: Native) : NativeView(context) {

    override fun create(): View?
    {
        var v = ImageView(_native._context);
        v.scaleType = ImageView.ScaleType.FIT_XY;
        return v;
    }

    fun url(url: String) {

        var v:ImageView = this._e as ImageView;

        var uri:Uri = Uri.parse(url);
        Glide.with(_native._context)
            .load(uri)
            .into(v);
    }
}
