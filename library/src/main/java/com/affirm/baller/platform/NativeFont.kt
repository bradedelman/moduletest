package com.affirm.baller.platform

import android.content.Context
import android.graphics.Typeface

class NativeFont constructor(
    context: Context,
) {

    var _context = context;
    var _fonts: HashMap<String, Typeface> = HashMap()

    fun getFont(url: String) : Typeface?
    {
        var typeface:Typeface? = _fonts.get(url);

        if (typeface == null) {
            typeface = Typeface.createFromAsset(_context.assets, "dist/" + url);
            _fonts.put(url, typeface);
        }

        if (typeface == null) {
            return null;
        } else {
            return typeface;
        }
    }
}


