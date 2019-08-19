package com.ishow.common.widget.viewpager.looping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.ishow.common.R

import java.util.ArrayList

/**
 * 轮播图的Adapter
 */
abstract class LoopingViewPagerAdapter<DATA, HOLDER : LoopingViewPagerAdapter.Holder>(protected var mContext: Context) : PagerAdapter() {
    /**
     * 数据内容
     */
    private var mData: MutableList<DATA> = ArrayList()
    protected var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    /**
     * 获取轮播图的真实数量
     */
    val realCount: Int
        get() = mData.size

    /**
     * @param data  设定的值
     * @param force 是否强制添加值
     */
    @JvmOverloads
    fun setData(data: MutableList<DATA>?, force: Boolean = true) {
        if (data != null) {
            mData = data
            notifyDataSetChanged()
        } else if (force) {
            mData.clear()
            notifyDataSetChanged()
        }
    }

    /**
     * 获取当前数据item内容
     * 真实数据
     */
    fun getItem(realPosition: Int): DATA {
        return mData[realPosition]
    }

    /**
     * 获取轮播图中图片数量
     * 第一个和最后一个 加一个可以轮播
     */
    override fun getCount(): Int {
        val realCount = realCount
        return if (realCount <= 1) {
            realCount
        } else {
            realCount * 3
        }
    }

    /**
     * 根据轮播的position来获取真实的position
     */
    fun getRealPosition(innerPosition: Int): Int {
        val realCount = realCount
        if (realCount <= 1) {
            return 0
        }

        var realPosition = innerPosition % realCount
        if (realPosition < 0) {
            realPosition += realCount
        }
        return realPosition
    }

    /**
     * 根据真实的position来获取轮播的position
     */
    fun getInnerPosition(realPosition: Int): Int {
        val realCount = realCount
        return if (realCount <= 1) {
            realPosition
        } else {
            realPosition + realCount
        }
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val realPosition = getRealPosition(position)
        val holder = onCreateView(container, realPosition, position)
        val item = holder.itemView
        onBindView(holder, realPosition, position)
        container.addView(item)
        return item
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        if (obj is View) {
            container.removeView(obj)
            recycle(obj, position)
        } else {
            super.destroyItem(container, position, obj)
        }
    }

    /**
     * 销毁View
     */
    @Suppress("UNUSED_PARAMETER")
    protected fun recycle(item: View, position: Int) {
    }

    /**
     * 创建View容器
     *
     * @param position 当前的 pos
     * @return 当前view的容器
     */
    abstract fun onCreateView(parent: ViewGroup, position: Int, innerPosition: Int): HOLDER

    /**
     * 通过容器来 绑定view
     *
     * @param holder   当前view的容器
     * @param position 当前的 pos
     */
    abstract fun onBindView(holder: HOLDER, position: Int, innerPosition: Int)


    /**
     * ViewHolder复用View的
     */
    abstract class Holder(val itemView: View, val type: Int) {
        init {
            itemView.setTag(R.id.tag_looping_viewpager_holder, this)
        }
    }
}

