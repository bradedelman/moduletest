package com.affirm.baller.view

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.affirm.baller.platform.Native
import com.affirm.baller.utils.LayoutView

class NativeList constructor(context: Native) : NativeView(context) {

    var _context = context._context;
    var _viewTypeId: String = "";
    var _viewWidth: Int = 0;
    var _viewHeight: Int = 0;
    var _count: Int = 0;
    var _bHorizontal: Boolean = false;
    var _adapter: MyViewAdapter? = null;

    inner class MyViewHolder(view:View) : ViewHolder(view) {
        var _nv: NativeView? = null;
        fun bind(n: Int) {

            _nv?.jsCall("doLayout", _viewWidth, _viewHeight);

            var v: LayoutView = itemView as LayoutView;
            v.layoutParams = LayoutParams(_viewWidth, _viewHeight);

            // POPULATE
            _nv?.jsCall("onPopulate", n, _id);
        }
    }

    inner class MyViewAdapter : Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            var nv: NativeView = _native.jsCreate(_viewTypeId)!!;

            // get Android view
            var v: View = nv._e!!;

            var vh = MyViewHolder(v);
            vh._nv  = nv;
            return vh;
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bind(position);
        }

        override fun getItemCount(): Int {
            return _count;
        }

    }

    override fun create(): View?
    {
        var v = RecyclerView(_native._context);
        v.setBackgroundColor(Color.TRANSPARENT);
        return v;
    }

    fun setViewType(viewTypeId: String) {
        _viewTypeId = viewTypeId;
    }

    fun ready() {
        if (_adapter == null) {
            var v: RecyclerView = _e as RecyclerView;
            var o: Int = LinearLayoutManager.VERTICAL;
            if (_bHorizontal) {
                o = LinearLayoutManager.HORIZONTAL;
            }
            v.apply {
                layoutManager = LinearLayoutManager(_context, o, false);
                _adapter = MyViewAdapter();
                adapter = _adapter;
            }
        }
    }

    fun setHorizontal(bHorizontal: Boolean) {
        _bHorizontal = bHorizontal;
    }

    fun setViewSize(width: Int, height: Int) {
        _viewWidth = width;
        _viewHeight = height;
    }

    fun setCount(count: Int) {
        _count = count;
        _adapter?.notifyDataSetChanged();
    }

}
