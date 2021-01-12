package com.affirm.baller.view

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.affirm.baller.platform.Native


class NativeButton constructor(context: Native) : NativeView(context) {

    override fun create():View?
    {
        var v = Button(_native._context);
        v.setTextColor(Color.BLACK);


        val shapedrawable:ShapeDrawable = ShapeDrawable()
        shapedrawable.shape = RectShape()
        shapedrawable.paint.color = Color.BLACK
        shapedrawable.paint.strokeWidth = 1f
        shapedrawable.paint.style = Paint.Style.STROKE
        v.setBackground(shapedrawable)

        val shapedrawable2:ShapeDrawable = ShapeDrawable()
        shapedrawable2.shape = RectShape()
        var c: Int = Color.parseColor("#EEEEEE");
        if (v != null) {
            shapedrawable2.paint.color = c
        }
        shapedrawable2.paint.style = Paint.Style.FILL

        val layers = arrayOf<Drawable>(
            shapedrawable2, shapedrawable
        )

        val layerDrawable = LayerDrawable(layers);
        v.setBackground(layerDrawable)
        v.setOnClickListener(View.OnClickListener { v -> jsCall("onClick") });


        v.setPadding(0, 0, 0, -2); // TODO: revisit "schluff factor"
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, 18.toFloat());
        return v;
    }

    fun text(text: String) {
        var v:TextView = this._e as Button;
        v.setText(text);
    }

    fun font(url: String, size: Int) {
        var v:TextView = this._e as Button;
        v.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat() / 1.07f); // TODO: revisit "schluff factor"
        v.setTypeface(_native._fonts.getFont(url));
    }
}

