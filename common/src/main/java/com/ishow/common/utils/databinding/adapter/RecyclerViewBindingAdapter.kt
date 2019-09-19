package com.ishow.common.utils.databinding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
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
        } else if (adapter is LoadMoreAdapter) {
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