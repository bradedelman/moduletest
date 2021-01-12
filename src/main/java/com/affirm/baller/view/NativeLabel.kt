package com.affirm.baller.view

import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.affirm.baller.platform.Native

class NativeLabel constructor(context: Native) : NativeView(context) {

    override fun create():View?
    {
        var v = TextView(_native._context);
        v.setTextIsSelectable(false);
        v.setTextColor(Color.BLACK);
        v.setSingleLine(false);
        v.setHorizontallyScrolling(false);
        v.setPadding(0, 0, 0, -3); // TODO: revisit "schluff factor"
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18.toFloat());
        return v;
    }

    fun text(text: String) {
        var v:TextView = this._e as TextView;
        v.setText(text);
    }

    fun font(url: String, size: Int) {
        var v:TextView = this._e as TextView;
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat() / 1.07f); // TODO: revisit "schluff factor"
        v.setTypeface(_native._fonts.getFont(url));
    }
}

