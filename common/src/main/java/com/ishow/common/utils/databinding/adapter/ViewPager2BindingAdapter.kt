package com.ishow.common.utils.databinding.adapter

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.ishow.common.adapter.BindAdapter

object ViewPager2BindingAdapter {

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("items")
    fun <T> setItems(pager2: ViewPager2, items: MutableList<T>) {
        if (pager2.adapter is BindAdapter<*>) {
            val adapter = pager2.adapter as BindAdapter<T>
            adapter.data = items
        }
    }
}