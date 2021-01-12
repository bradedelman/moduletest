package com.affirm.baller.service

import android.view.ViewGroup
import com.affirm.baller.platform.Native
import com.affirm.baller.utils.CreateNativeView
import com.affirm.baller.view.NativeView

class NativeHost constructor(native: Native) {

    var _native: Native = native;

    fun onEvent(name: String, value: String) {
        _native._hostView._delegate?.onEvent(name, value);
    }

    fun create(nativeType: String, id: String) {

        var nv: NativeView = CreateNativeView.create(nativeType, this._native);

        var v = nv.create();
        if (v != null) {
            nv.setView(v);
            nv._id = id;
        }

        nv.setBounds(0, 0, 0, 0); // need a Layout object, even if all 0
        _native._views.set(id, nv)
        _native.fixSize(nv)
    }

    fun addChild(parentId: String, childId: String)
    {
        var parent = _native._views.get(parentId);
        var child = _native._views.get(childId);
        var parentView = parent?._e;
        var childView = child?._e;

        if (parentView != null) {
            var l: ViewGroup = parentView as ViewGroup;
            l.addView(childView);
        }
    }

    fun finishInit(nativeType: String)
    {
        var nv = _native.jsCreate(nativeType);
        var view = nv?._e;
        _native._hostView._nv = nv;
        _native._hostView.addView(view!!);
    }
}
