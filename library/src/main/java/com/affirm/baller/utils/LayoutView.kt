package com.affirm.baller.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import java.lang.Exception


// this is needed because Android doesn't provide a simple coordinate based layout
open class LayoutView constructor(context: Context) : ViewGroup(context) {

    interface LayoutViewEventHandler {
        fun onUp(x: Int, y: Int)
    }

    var _handler: LayoutViewEventHandler? = null;

    class LayoutParams constructor(x: Int, y: Int, w: Int, h: Int) :
        FrameLayout.LayoutParams(w, h) {

        var _x: Int = x;
        var _y: Int = y;
        var _w: Int = w;
        var _h: Int = h;

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int): Unit {
        for (child: View in children) {
            if (child.getVisibility() != GONE) {
                var p: LayoutParams = child.getLayoutParams() as LayoutParams;

                var width = MeasureSpec.makeMeasureSpec(p._w, EXACTLY);
                var height = MeasureSpec.makeMeasureSpec(p._h, EXACTLY);

                child.measure(width, height);
                child.layout(p._x, p._y, p._x + p._w, p._y + p._h);
            }
        }
    }


    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        super.dispatchTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                _handler?.onUp(event.x.toInt(), event.y.toInt());
                return true
            }
        }
        return false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var w:Int = View.MeasureSpec.getSize(widthMeasureSpec);
        var h:Int = View.MeasureSpec.getSize(heightMeasureSpec);

        try {
            w = (layoutParams as LayoutParams)._w;
            h = (layoutParams as LayoutParams)._h;
        } catch (e:Exception) {
        }

        setMeasuredDimension(w, h)
    }
}
