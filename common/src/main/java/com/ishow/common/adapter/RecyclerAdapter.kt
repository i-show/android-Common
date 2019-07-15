/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R

import java.util.ArrayList

/**
 * 封装后的RecycleAdapter
 */
abstract class RecyclerAdapter<DATA, HOLDER : RecyclerAdapter.Holder>(protected var mContext: Context) : RecyclerView.Adapter<HOLDER>() {

    protected var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    protected var disableOnItemClickListener = false

    private var mData: MutableList<DATA> = ArrayList()
    private var mOnItemClickListener: ((Int) -> Unit)? = null
    /**
     * 当前绑定的RecycleView
     * [.onAttachedToRecyclerView] 中进行赋值操作
     * [.onDetachedFromRecyclerView] 会进行值空
     */
    private var mRecyclerView: RecyclerView? = null

    /**
     * 是否为空
     */
    val isEmpty: Boolean
        get() = mData.isEmpty()

    /**
     * 获取数据
     */
    /**
     * 添加数据
     */
    var data: MutableList<DATA>?
        get() = mData
        set(data) = setData(data, true)


    /**
     * @param data  设定的值
     * @param force 是否强制添加值
     */
    fun setData(data: MutableList<DATA>?, force: Boolean) {
        if (data != null) {
            val canAni = mData.isEmpty()
            mData = data
            notifyDataSetChanged()
            if (canAni) animation()
        } else if (force) {
            val size = mData.size
            mData.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    /**
     * 增加数据
     */
    fun plusData(data: List<DATA>?) {
        if (data != null) {
            val lastIndex = mData.size
            mData.addAll(data)
            notifyItemRangeInserted(lastIndex, data.size)
        }
    }

    /**
     * 清空数据
     */
    fun clear() {
        mData.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    fun getItem(position: Int): DATA {
        return mData[position]
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, type: Int): HOLDER

    abstract fun onBindViewHolder(holder: HOLDER, position: Int, type: Int)

    override fun onBindViewHolder(holder: HOLDER, position: Int) {
        holder.itemView.setTag(R.id.tag_view_holder_recycle_item_click, position)
        if (!disableOnItemClickListener) {
            holder.itemView.setOnClickListener(ItemViewClickListener())
        }
        onBindViewHolder(holder, position, holder.itemViewType)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    /**
     * 执行动画
     */
    private fun animation() {
        mRecyclerView?.scheduleLayoutAnimation()
    }

    abstract class Holder(val item: View, val type: Int) : RecyclerView.ViewHolder(item)


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: ((Int) -> Unit)?) {
        mOnItemClickListener = listener
    }

    private inner class ItemViewClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val position = v.getTag(R.id.tag_view_holder_recycle_item_click) as Int
            mOnItemClickListener?.let { it(position) }
        }
    }

}
