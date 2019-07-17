/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.common.widget.recyclerview.layoutmanager

import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class FlowLayoutManager : RecyclerView.LayoutManager() {
    /**
     * 总共位移的距离
     */
    private var mTotalOffset: Int = 0
    /**
     * 第一个可见的item position
     */
    private var mFirstVisiblePosition: Int = 0
    /**
     * 最后一个可见的item 的position
     */
    private var mLastVisiblePosition: Int = 0
    /**
     * 所有Item的位置信息（下滑时候使用）
     */
    private val mItemRectArray: SparseArray<Rect> = SparseArray()


    val verticalSpace: Int
        get() = height - paddingTop - paddingBottom

    @Suppress("MemberVisibilityCanBePrivate")
    val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler!!)
            return
        }
        if (childCount == 0 && state!!.isPreLayout) {
            return
        }
        detachAndScrapAttachedViews(recycler!!)

        //初始化区域
        mTotalOffset = 0
        mFirstVisiblePosition = 0
        mLastVisiblePosition = itemCount

        fill(recycler)
    }

    /**
     * 是否自动计算
     */
    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    /**
     * 初始化时调用 填充childView
     */
    private fun fill(recycler: RecyclerView.Recycler) {
        fill(recycler, 0)
    }


    private fun fill(recycler: RecyclerView.Recycler?, dy: Int): Int {
        /*
         * 先移除已经不可见的View
         */
        removeAlreadyOutChild(recycler, dy)
        /*
         * 重新布局 可见的View
         */
        return if (dy >= 0) {
            fillUp(recycler, dy)
        } else {
            fillDown(recycler, dy)
        }
    }

    /**
     * 移除不可见的View
     */
    private fun removeAlreadyOutChild(recycler: RecyclerView.Recycler?, dy: Int) {
        val childCount = childCount
        // 如果没有Child或者没有移动 那么不需要操作
        if (childCount <= 0 || dy == 0) {
            return
        }
        val topOffset = paddingTop
        val bottomOffset = paddingBottom
        val recyclerHeight = height

        /*
         * 当有View填充的时候并且有滑动的时候 那么重新计算滑动后的position
         * dy > 0 为上滑
         * dy < 0 为下滑
         */
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            if (dy > 0 && isAlreadyScrollUpOut(child, dy, topOffset)) {
                removeAndRecycleView(child!!, recycler!!)
                mFirstVisiblePosition++
            } else if (dy < 0 && isAlreadyScrollDownOut(child, dy, recyclerHeight, bottomOffset)) {
                removeAndRecycleView(child!!, recycler!!)
                mLastVisiblePosition--
            }
        }
    }

    /**
     * 上滑
     */
    private fun fillUp(recycler: RecyclerView.Recycler?, dy: Int): Int {
        val width = horizontalSpace
        var left = paddingLeft
        var top = paddingTop
        var lineHeight = 0

        var firstPosition = mFirstVisiblePosition
        mLastVisiblePosition = itemCount - 1

        /*
         * 如果已经有View add 那么从可见View的下一个进行排序
         * 不需要对已经排序的重新排序
         */
        if (childCount > 0) {
            val lastView = getChildAt(childCount - 1)
            firstPosition = getPosition(lastView!!) + 1
            top = getDecoratedTop(lastView)
            left = getDecoratedRight(lastView)
            lineHeight = max(lineHeight, getDecoratedMeasurementVertical(lastView))
        }

        //顺序addChildView
        for (i in firstPosition..mLastVisiblePosition) {
            val child = recycler!!.getViewForPosition(i)
            addView(child)
            measureChildWithMargins(child, 0, 0)
            val childSize = getViewSize(child)
            val childWidth = childSize[0]
            val childHeight = childSize[1]
            // 当前行
            if (left + childWidth <= width) {
                layoutDecoratedWithMargins(child, left, top, left + childWidth, top + childHeight)

                /*
                 * 保存Rect供逆序layout用
                 * 备注：为什么要 + mTotalOffset;
                 */
                val rect = Rect(left, top + mTotalOffset, left + childWidth, top + childHeight + mTotalOffset)
                mItemRectArray.put(i, rect)

                left += childWidth
                lineHeight = max(lineHeight, childHeight)
            } else {
                /*
                 * 换行操作
                 */
                left = paddingLeft
                top += lineHeight
                lineHeight = 0

                //新起一行的时候要判断一下边界
                if (top - dy > height - paddingBottom) {
                    //越界了 就回收
                    removeAndRecycleView(child, recycler)
                    mLastVisiblePosition = i - 1
                } else {
                    layoutDecoratedWithMargins(child, left, top, left + childWidth, top + childHeight)

                    val rect = Rect(left, top + mTotalOffset, left + childWidth, top + childHeight + mTotalOffset)
                    mItemRectArray.put(i, rect)

                    left += childWidth
                    lineHeight = max(lineHeight, childHeight)
                }
            }
        }

        return checkMoveUpOffset(dy)
    }


    /**
     * 下滑
     */
    private fun fillDown(recycler: RecyclerView.Recycler?, dy: Int): Int {
        var lastPosition = itemCount - 1
        mFirstVisiblePosition = 0
        /*
         * 以顶部第一个不可见的View作为最后一个进行倒序添加View
         */
        if (childCount > 0) {
            val firstView = getChildAt(0)
            lastPosition = getPosition(firstView!!) - 1
        }

        for (i in lastPosition downTo mFirstVisiblePosition) {
            val rect = mItemRectArray.get(i)

            if (rect.bottom - mTotalOffset - dy < paddingTop) {
                mFirstVisiblePosition = i + 1
                break
            } else {
                val child = recycler!!.getViewForPosition(i)
                addView(child, 0)//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                measureChildWithMargins(child, 0, 0)

                layoutDecoratedWithMargins(
                    child,
                    rect.left,
                    rect.top - mTotalOffset,
                    rect.right,
                    rect.bottom - mTotalOffset
                )
            }
        }
        return dy
    }

    override fun canScrollVertically(): Boolean {
        return true
    }


    /**
     * dy > 0 上滑
     * dy < 0 下滑
     */
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        if (dy == 0 || childCount == 0) {
            return 0
        }

        var realOffset = checkMoveOffset(dy)
        // 如果当前需要移动的距离为0 那么不需要其他操作
        if (realOffset == 0) {
            return 0
        }

        realOffset = fill(recycler, realOffset)
        mTotalOffset += realOffset
        offsetChildrenVertical(-realOffset)
        return realOffset
    }


    private fun checkMoveOffset(dy: Int): Int {
        /*
         * 下滑 判断距离上边界
         */
        if (dy < 0 && mTotalOffset + dy < 0) {
            return -mTotalOffset
        }
        /*
         * 上滑 判断下边界
         */
        return if (dy > 0) {
            checkMoveUpOffset(dy)
        } else dy
    }

    /**
     * 通过最后一个显示view和adapter中最后一个view来判断 移动距离
     */
    private fun checkMoveUpOffset(dy: Int): Int {

        val lastChild = getChildAt(childCount - 1)
        if (getPosition(lastChild!!) == itemCount - 1) {
            val gap = height - paddingBottom - getViewBottomWithMargin(lastChild)
            // 如果空出来的距离比要移动的距离大 那么不需要移动
            if (gap == 0 || abs(gap) >= abs(dy)) {
                return 0
            }

            return if (gap > 0) {
                -gap
            } else {
                min(dy, -gap)
            }
        }
        return dy
    }

    /**
     * 是否已经上滑 出顶部
     *
     * @param dy 滑动的距离
     */
    private fun isAlreadyScrollUpOut(child: View?, dy: Int, parentTop: Int): Boolean {
        /*
         * 这里往上滑动 dy > 0 ,所以bootom - dy < pTop 就是已经不可见状态
         */
        return getDecoratedBottom(child!!) - dy < parentTop
    }

    /**
     * 是否已经下午出底部
     *
     * @param dy 滑动的距离
     */
    private fun isAlreadyScrollDownOut(child: View?, dy: Int, parentHeight: Int, paddingBottom: Int): Boolean {
        /*
         * 这里往下滑动 dy < 0 ,所以childTop - dy < height - pTop 就是已经不可见状态
         */
        return getDecoratedTop(child!!) - dy > parentHeight - paddingBottom
    }

    /**
     * 获取某个childView在水平方向所占的空间
     */
    fun getViewSize(view: View): IntArray {
        val params = view.layoutParams as RecyclerView.LayoutParams
        val size = IntArray(2)
        size[0] = getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin
        size[1] = getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin
        return size
    }

    /**
     * 获取 View的bottom 包含margin
     */
    private fun getViewBottomWithMargin(view: View?): Int {
        if (view == null) {
            return 0
        }
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedBottom(view) + params.bottomMargin
    }

    /**
     * 获取某个childView在水平方向所占的空间
     */
    fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     */
    fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin
    }
}
