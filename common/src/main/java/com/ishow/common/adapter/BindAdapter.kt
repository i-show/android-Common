package com.ishow.common.adapter

import android.content.Context
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.extensions.inflate

open class BindAdapter<T>(val context: Context?) : RecyclerView.Adapter<BindAdapter.BindHolder>() {

    var data: MutableList<T> = ArrayList()

    private val layoutList = SparseIntArray()
    private val variableList = SparseIntArray()
    private var itemClickListener: ((Int) -> Unit)? = null

    protected var disableOnItemClickListener = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder {
        return BindHolder(parent.inflate(layoutList[viewType]), viewType)
    }

    override fun onBindViewHolder(holder: BindHolder, position: Int) {
        if (!disableOnItemClickListener) {
            itemClickListener?.let { holder.item.setOnClickListener { it(position) } }
        }
        val itemData = getItem(position)
        val viewType = getItemViewType(position)
        holder.bind(variableList[viewType], itemData)
    }

    override fun getItemCount(): Int = data.size

    fun getItem(position: Int): T = data[position]

    /**
     * 添加布局文件
     */
    @JvmOverloads
    fun addLayout(layoutRes: Int, variableId: Int, viewType: Int = 0) {
        layoutList.put(viewType, layoutRes)
        variableList.put(viewType, variableId)
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

    fun setOnItemClickListener(listener: ((Int) -> Unit)?) {
        itemClickListener = listener
    }

    class BindHolder(val item: View, val viewType: Int) : RecyclerView.ViewHolder(item) {
        private val binding: ViewDataBinding? = DataBindingUtil.bind(item)

        fun bind(variableId: Int, data: Any?) {
            binding?.setVariable(variableId, data)
            binding?.executePendingBindings()
        }
    }
}