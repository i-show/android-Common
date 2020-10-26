package com.ishow.common.utils.databinding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.BR
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.utils.databinding.bus.Event
import com.ishow.common.widget.pulltorefresh.recycleview.LoadMoreAdapter

object RecyclerViewBindingAdapter {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <T> setItems(listView: RecyclerView, items: MutableList<T>?) {

        val adapter = listView.adapter

        if (adapter is BindAdapter<*>) {
            val bindingAdapter = adapter as BindAdapter<T>
            bindingAdapter.data = items
        } else if (adapter is LoadMoreAdapter<*>) {
            val bindingAdapter = adapter.innerAdapter as BindAdapter<T>
            bindingAdapter.data = items
        }
    }

    /**
     * bindSelected状态
     */
    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter(value = ["bindData", "bindItemLayout"], requireAll = false)
    fun <T> bindData(view: RecyclerView, items: MutableList<T>?, bindItemLayout: Int = 0) {
        val adapter = view.adapter
        if (adapter == null && bindItemLayout != 0) {
            val bindAdapter = BindAdapter<T>()
            bindAdapter.addLayout(BR.item, bindItemLayout)
            bindAdapter.data = items
            view.adapter = bindAdapter
            return
        }

        if (adapter is BindAdapter<*>) {
            val bindAdapter = adapter as BindAdapter<T>
            bindAdapter.data = items
        } else if (adapter is LoadMoreAdapter<*>) {
            val bindingAdapter = adapter.innerAdapter as BindAdapter<T>
            bindingAdapter.data = items
        }
    }

    @JvmStatic
    @BindingAdapter("notifyDataSetChanged")
    fun notifyDataSetChanged(listView: RecyclerView, event: Event<Any>?) {
        event?.value?.let {
            listView.adapter?.notifyDataSetChanged()
        }
    }

    @JvmStatic
    @BindingAdapter("notifyItemChanged")
    fun notifyItemChanged(listView: RecyclerView, event: Event<Int>?) {
        event?.value?.let {
            listView.adapter?.notifyItemChanged(it)
        }
    }
}