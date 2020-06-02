package com.ishow.common.adapter

import android.content.Context
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.core.util.set
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.BR
import com.ishow.common.R
import com.ishow.common.extensions.inflate
import java.util.*

open class BindAdapter<T> : RecyclerView.Adapter<BindAdapter.BindHolder>() {
    protected lateinit var context: Context

    private var _data: MutableList<T> = ArrayList()
    var data: MutableList<T>?
        get() = _data
        set(data) = setData(data, true)

    /**
     * item的layout记录
     */
    private val layoutList = SparseIntArray()

    /**
     * data模型数据dataBinding的BR的值
     */
    private val dataVariableList = SparseIntArray()

    /**
     * 成员数据的dataBinding值
     */
    private var memberVariableList: SparseArray<MutableList<Variable>>? = null

    /**
     * Item的点击事件
     */
    private var itemClickListener: OnItemClickListener? = null

    private var childItemClickListener = SparseArray<OnItemChildClickListener>()

    /**
     * 设置itemType的Block
     */
    private var itemTypeBlock: ((Int) -> Int)? = null

    /**
     * 终止掉本身的ClickListener
     */
    @Suppress("MemberVisibilityCanBePrivate")
    protected var disableOnItemClickListener = false

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindHolder {
        val item = parent.inflate(layoutList[viewType])
        onViewCreated(item, viewType)
        setItemListener(item)
        setItemChildListener(item)
        return BindHolder(item, viewType, memberVariableList)
    }

    override fun onBindViewHolder(holder: BindHolder, position: Int) {
        val itemData = getItem(position)
        val viewType = getItemViewType(position)
        holder.item.setTag(R.id.tag_position, position)
        holder.bind(dataVariableList[viewType], itemData, position)
    }

    override fun getItemCount(): Int = _data.size

    fun getItem(position: Int): T = _data[position]

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemTypeBlock == null) {
            super.getItemViewType(position)
        } else {
            itemTypeBlock!!(position)
        }
    }

    @Suppress("unused")
    fun setItemTypeBlock(block: ((Int) -> Int)? = null) {
        itemTypeBlock = block
    }

    /**
     * 添加布局文件
     */
    @JvmOverloads
    fun addLayout(variableId: Int, layoutRes: Int, viewType: Int = 0) {
        layoutList.put(viewType, layoutRes)
        dataVariableList.put(viewType, variableId)
    }

    /**
     * 增加variable
     */
    @JvmOverloads
    fun addVariable(variableId: Int, data: Any, viewType: Int = 0) {
        if (memberVariableList == null) {
            memberVariableList = SparseArray()
        }

        val variableList = memberVariableList!!

        var list = variableList[viewType]
        if (list == null) {
            list = ArrayList()
        }
        list.add(Variable(variableId, data))
        variableList[viewType] = list
    }

    /**
     * @param data  设定的值
     * @param force 是否强制添加值
     */
    fun setData(data: MutableList<T>?, force: Boolean) {
        if (data != null) {
            _data = data
            notifyDataSetChanged()
        } else if (force) {
            val size = _data.size
            _data.clear()
            notifyItemRangeRemoved(0, size)
        }
    }

    /**
     * 增加数据
     */
    fun plusData(data: T) {
        val lastIndex = _data.size
        _data.add(data)
        notifyItemRangeInserted(lastIndex, 1)
    }

    /**
     * 增加数据
     */
    fun plusData(data: List<T>?) {
        if (data != null) {
            val lastIndex = _data.size
            _data.addAll(data)
            notifyItemRangeInserted(lastIndex, data.size)
        }
    }

    /**
     * 清空数据
     */
    fun clear() {
        _data.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        itemClickListener = listener
    }

    fun setOnItemChildClickListener(viewId: Int, listener: OnItemChildClickListener? = null) {
        childItemClickListener.put(viewId, listener)
    }


    protected open fun onViewCreated(item: View, viewType: Int) {

    }

    protected open fun setItemListener(item: View) {
        if (disableOnItemClickListener) {
            return
        }

        item.setOnClickListener { _ ->
            val position = item.getTag(R.id.tag_position) as Int
            itemClickListener?.let { it(position) }
        }
    }


    protected open fun setItemChildListener(item: View) {
        if (disableOnItemClickListener) {
            return
        }

        childItemClickListener.forEach { key, value ->
            val view: View? = item.findViewById(key)
            view?.setOnClickListener {
                val position = item.getTag(R.id.tag_position) as Int
                value(position, key)
            }
        }
    }

    class BindHolder(
        val item: View,
        val viewType: Int,
        private val memberVariableList: SparseArray<MutableList<Variable>>?
    ) : RecyclerView.ViewHolder(item) {
        val binding: ViewDataBinding? = DataBindingUtil.bind(item)

        init {
            initMemberVariable()
        }

        private fun initMemberVariable() {
            if (memberVariableList == null) return
            val data = memberVariableList[viewType]
            if (data.isNullOrEmpty()) {
                return
            }

            data.forEach { binding?.setVariable(it.variableId, it.data) }
        }

        fun bind(variableId: Int, data: Any?, position: Int) {
            binding?.setVariable(BR.position, position)
            binding?.setVariable(variableId, data)
            binding?.executePendingBindings()
        }
    }


    class Variable(val variableId: Int, val data: Any)
}

/**
 * Item的点击事件
 */
typealias OnItemClickListener = ((position: Int) -> Unit)

/**
 * ItemChild的点击事件
 */
typealias OnItemChildClickListener = ((position: Int, viewId: Int) -> Unit)