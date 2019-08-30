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
package com.ishow.common.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IdRes
import com.ishow.common.R
import com.ishow.common.utils.AppUtils

/**
 * 最底部的一条对应TopBar
 */
class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    /**
     * 选中切换的监听
     */
    private var mBottomBarListener: OnBottomBarListener? = null
    private var mBottomBarBlock: ((ViewGroup, Int, Int) -> Unit)? = null

    private var mBottomBarClickListener: OnBottomBarClickListener? = null
    private var mBottomBarClickBolck: ((View, Boolean) -> Unit)? = null
    /**
     * 选中的ID
     */
    private var mSelectedId: Int = 0
    /**
     * 不能进行点选的ID 一般只有一个
     */
    private val mCanNotSelectedId: Int

    private val canAnimation: Boolean
    private val animationZoom: Float

    /**
     * 当前选中的ID
     */
    var selectedId: Int
        get() = mSelectedId
        set(@IdRes id) = setSelectedId(id, false)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BottomBar)
        mSelectedId = a.getResourceId(R.styleable.BottomBar_selectedChild, View.NO_ID)
        mCanNotSelectedId = a.getResourceId(R.styleable.BottomBar_cannotSelectedChild, View.NO_ID)
        canAnimation = a.getBoolean(R.styleable.BottomBar_canAnimation, true)
        animationZoom = a.getFloat(R.styleable.BottomBar_animationZoom, ZOOM)
        orientation = HORIZONTAL
        a.recycle()
        setOnHierarchyChangeListener(PassThroughHierarchyChangeListener())
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (mSelectedId != View.NO_ID) {
            setSelectedStateForView(mSelectedId, true)
        }
    }

    /**
     * 设置当前选中的ID
     */
    fun setSelectedId(@IdRes id: Int, force: Boolean) {
        if (AppUtils.isFastDoubleClick) {
            return
        }

        if (id == mCanNotSelectedId) {
            Log.i(TAG, "setSelectedId: id is can not selected id")
            return
        }

        // 如果2个id相等 那么就返回
        if (id == mSelectedId && !force) {
            Log.i(TAG, "setSelectedId: is same id && not force")
            return
        }

        // 1. 把 将要选中的View的状态修改为true
        if (id != -1) {
            setSelectedStateForView(id, true)
        }
        // 1. 把当前选中的View的状态修改为false
        setSelectedStateForView(mSelectedId, false)

        mSelectedId = id
        mBottomBarListener?.onSelectedChanged(this, mSelectedId, findSelectedChildIndex())
        mBottomBarBlock?.let { it(this, mSelectedId, findSelectedChildIndex()) }
    }

    /**
     * 查询当前选中view的index
     */
    private fun findSelectedChildIndex(): Int {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (mSelectedId == view.id) {
                return i
            }
        }
        return -1
    }

    /**
     * 更新选中View的状态
     */
    private fun setSelectedStateForView(viewId: Int, checked: Boolean) {
        val view = findViewById<View>(viewId)
        if (view == null) {
            Log.i(TAG, "setSelectedStateForView: unknown view")
            return
        }
        view.isSelected = checked
        zoomView(view, checked)
    }

    override fun onClick(v: View) {
        val id = v.id
        mBottomBarClickListener?.onClickChild(v, id == mSelectedId)
        mBottomBarClickBolck?.let { it(v, id == mSelectedId) }
        selectedId = id
    }

    /**
     * 设置切换事件
     */
    fun setOnSelectedChangedListener(listener: OnBottomBarListener) {
        mBottomBarListener = listener
    }

    /**
     *  ViewGroup   点击的View的父类
     *  Int 选中View 的ID
     *  选中View的位置
     */
    fun setOnSelectedChangedListener(listener: (ViewGroup, Int, Int) -> Unit) {
        mBottomBarBlock = listener
    }

    /**
     * 设置Child的点击事件
     */
    fun setOnChildClickListener(listener: OnBottomBarClickListener) {
        mBottomBarClickListener = listener
    }

    /**
     *  设置Child的点击事件
     */
    fun setOnChildClickListener(listener: (View, Boolean) -> Unit) {
        mBottomBarClickBolck = listener
    }

    /**
     * 切换事件
     */
    interface OnBottomBarListener {
        /**
         * @param parent   点击的View的父类
         * @param selectId 选中View 的ID
         * @param index    选中View的位置
         */
        fun onSelectedChanged(parent: ViewGroup, @IdRes selectId: Int, index: Int)
    }

    /**
     * 切换事件
     */
    interface OnBottomBarClickListener {
        /**
         * 点击子View
         *
         * @param v          子view
         * @param isSameView 是否是同一个View
         */
        fun onClickChild(v: View, isSameView: Boolean)
    }

    /**
     * 布局更改的Listener
     */
    private inner class PassThroughHierarchyChangeListener : OnHierarchyChangeListener {

        override fun onChildViewAdded(parent: View, child: View) {
            child.setOnClickListener(this@BottomBar)
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            child.setOnClickListener(null)
        }
    }

    private fun zoomView(view: View?, checked: Boolean) {
        if (!canAnimation || view == null) {
            Log.i(TAG, "zoomView: view is null")
            return
        }

        val start = if (checked) 1f else animationZoom
        val end = if (checked) animationZoom else 1f

        view.clearAnimation()
        val animator = ValueAnimator.ofFloat(start, end)
        animator.duration = 100
        animator.addUpdateListener { animation ->
            val zoom = animation.animatedValue as Float
            view.scaleX = zoom
            view.scaleY = zoom
        }
        animator.start()

    }

    companion object {
        private const val TAG = "BottomBar"

        /**
         * 可放大多少
         */
        private const val ZOOM = 1.05f
    }
}
