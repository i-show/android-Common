package com.ishow.common.utils.databinding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.utils.databinding.bus.Event

object RecyclerViewBindingAdapter {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <T> setItems(listView: RecyclerView, items: MutableList<T>) {
        if (listView.adapter is BindAdapter<*>) {
            val adapter = listView.adapter as BindAdapter<T>
            adapter.data = items
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