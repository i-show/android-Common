package com.ishow.common.adapter

import android.content.Context
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R
import com.ishow.common.extensions.inflate
import java.util.ArrayList

open class BindAdapter<T>(val context: Context) : RecyclerView.Adapter<BindAdapter.BindHolder>() {

    private var mData: MutableList<T> = ArrayList()

    var data: MutableList<T>
        get() = mData
        set(data) = setData(data, true)

    private val layoutList = SparseIntArray()
    private val variableList = SparseIntArray()
    private var itemClickListener: ((Int) -> Unit)? = null

    protected var disableOnItemClickListener = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder {
        val item = parent.inflate(layoutList[viewType])
        setItemListener(item)
        return BindHolder(item, viewType)
    }

    override fun onBindViewHolder(holder: BindHolder, position: Int) {
        val itemData = getItem(position)
        val viewType = getItemViewType(position)
        holder.item.setTag(R.id.tag_position, position)
        holder.bind(variableList[viewType], itemData)
    }

    override fun getItemCount(): Int = mData.size

    fun getItem(position: Int): T = mData[position]

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
            mData = data
            notifyDataSetChanged()
        } else if (force) {
            val size = mData.size
            mData.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    /**
     * 增加数据
     */
    fun plusData(data: List<T>?) {
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

    fun setOnItemClickListener(listener: ((Int) -> Unit)?) {
        itemClickListener = listener
    }

    private fun setItemListener(item: View) {
        if (disableOnItemClickListener) {
            return
        }

        itemClickListener?.let { listener ->
            item.setOnClickListener {
                val position = it.getTag(R.id.tag_position) as Int
                listener(position)
            }
        }
    }

    class BindHolder(val item: View, val viewType: Int) : RecyclerView.ViewHolder(item) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(item)

        fun bind(variableId: Int, data: Any?) {
            binding?.setVariable(variableId, data)
            binding?.executePendingBindings()
        }
    }
}