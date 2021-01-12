package com.affirm.baller.view

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.DrawableContainer
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import com.affirm.baller.platform.Native

class NativeField constructor(context: Native) : NativeView(context) {

    override fun create():View?
    {
        var v = EditText(_native._context);
        v.setTextColor(Color.BLACK);
        v.setLines(1);
        v.setPadding(8,0,8,0); // TODO reconsider

        // get white background with black border - really this hard?
        var shape = ShapeDrawable( RectShape());
        shape.getPaint().setColor(Color.RED);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(3.0f);
        var shape2 = ShapeDrawable( RectShape());
        shape2.getPaint().setColor(Color.WHITE);
        shape2.getPaint().setStyle(Paint.Style.FILL);
        var shape3 = LayerDrawable(arrayOf(shape2, shape));

        // Assign the created border to EditText widget
        v.setBackground(shape3);

        return v;
    }

    fun text(text: String) {
        var v:EditText = this._e as EditText;
        v.setText(text);
    }

    fun value():String {
        var v:EditText = this._e as EditText;
        return v.text.toString()
    }

    fun font(url: String, size: Int) {
        var v:EditText = this._e as EditText;
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat());
        v.setTypeface(_native._fonts.getFont(url));
    }
}

