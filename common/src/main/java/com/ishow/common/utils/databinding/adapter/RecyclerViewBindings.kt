package com.ishow.common.utils.databinding.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.adapter.BindAdapter

object RecyclerViewBindings {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <T> setItems(listView: RecyclerView, items: MutableList<T>) {
        if (listView.adapter is BindAdapter<*>) {
            val adapter = listView.adapter as BindAdapter<T>
            adapter.data = items
        }
    }
}