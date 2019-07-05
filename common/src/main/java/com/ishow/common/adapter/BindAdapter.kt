package com.ishow.common.adapter

import android.content.Context
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.extensions.inflate

class BindAdapter<T>(val context: Context?) : RecyclerView.Adapter<BindAdapter.BindHolder>() {

    var data: MutableList<T> = ArrayList()
    var variableId: Int = 0

    private val layoutList = SparseIntArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder {
        return BindHolder(parent.inflate(layoutList[viewType]), viewType)
    }

    override fun onBindViewHolder(holder: BindHolder, position: Int) {
        val itemData = getItem(position)
        holder.bind(variableId, itemData)
    }

    override fun getItemCount(): Int = data.size

    fun getItem(position: Int): T = data[position]

    /**
     * 添加布局文件
     */
    @JvmOverloads
    fun addLayout(layoutRes: Int, viewType: Int = 0) {
        layoutList.put(viewType, layoutRes)
    }

    /**
     * @param data  设定的值
     * @param force 是否强制添加值
     */
    fun setData(data: MutableList<T>?, force: Boolean) {
        if (data != null) {
            this.data = data
            notifyDataSetChanged()
        } else if (force) {
            val size = this.data.size
            this.data.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    /**
     * 增加数据
     */
    fun plusData(data: List<T>?) {
        if (data != null) {
            val lastIndex = this.data.size
            this.data.addAll(data)
            notifyItemRangeInserted(lastIndex, data.size)
        }
    }


    /**
     * 清空数据
     */
    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }


    class BindHolder(val item: View, val viewType: Int) : RecyclerView.ViewHolder(item) {
        private val binding: ViewDataBinding? = DataBindingUtil.bind(item)

        fun bind(variableId: Int, data: Any?) {
            binding?.setVariable(variableId, data)
            binding?.executePendingBindings()
        }
    }
}