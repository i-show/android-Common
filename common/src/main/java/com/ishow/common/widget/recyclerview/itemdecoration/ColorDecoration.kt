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

package com.ishow.common.widget.recyclerview.itemdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ishow.common.R
import com.ishow.common.extensions.dp2px
import com.ishow.common.extensions.findColor
import com.ishow.common.extensions.getDimensionPixelSize

class ColorDecoration : RecyclerView.ItemDecoration {

    private var mDividerPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var mDividerHeight: Int

    private var mPaddingStart: Int = 0
    private var mPaddingEnd: Int = 0
    /**
     * 是否显示最后的Diver
     * 仅支持 LinearLayoutManager
     */
    var showLastDivider: Boolean = false

    @JvmOverloads
    constructor(context: Context, @ColorRes color: Int = R.color.line, @Px height: Int = 1) {
        mDividerPaint.color = context.findColor(color)
        mDividerHeight = height
    }


    @JvmOverloads
    constructor(color: Int = 0XFFC2CADC.toInt(), @Px height: Int = 1) {
        mDividerPaint.color = color
        mDividerHeight = height
    }

    /**
     * 设置Padding的值
     * @param start dp值
     * @param end dp值
     * 注意：Padding只对LinearLayout有效
     */
    fun setPadding(start: Int, end: Int) {
        mPaddingStart = start.dp2px()
        mPaddingEnd = end.dp2px()
    }

    fun setPaddingRes(context: Context, start: Int, end: Int) {
        mPaddingStart = context.getDimensionPixelSize(start)
        mPaddingEnd = context.getDimensionPixelSize(end)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager,
            is StaggeredGridLayoutManager -> {
                drawGridLayout(canvas, parent)
            }

            is LinearLayoutManager -> {
                drawLinearLayout(canvas, layoutManager, parent)
            }
        }
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager,
            is StaggeredGridLayoutManager -> {
                offsetGridLayout(parent, view, outRect)
            }

            is LinearLayoutManager -> {
                offsetLinearLayout(layoutManager, parent, view, outRect)
            }
        }
    }

    /**
     * GridLayoutManager 增加间距
     */
    private fun offsetGridLayout(parent: RecyclerView, child: View, outRect: Rect) {
        val adapter = parent.adapter ?: return
        val spacing = mDividerHeight
        val position = parent.getChildAdapterPosition(child)
        val spanCount = getSpanCount(parent)
        val childCount = adapter.itemCount

        @Suppress("CascadeIf")
        if (isLastRaw(parent, position, spanCount, childCount)) {
            // 如果是最后一行，则不需要绘制底部
            outRect.set(0, 0, spacing, 0)
        } else if (isLastColumn(parent, position, spanCount, childCount)) {
            // 如果是最后一列，则不需要绘制右边
            outRect.set(0, 0, 0, spacing)
        } else {
            outRect.set(0, 0, spacing, spacing)
        }
    }

    /**
     * LinearLayoutManager 增加间距
     */
    private fun offsetLinearLayout(
        layoutManager: LinearLayoutManager,
        parent: RecyclerView,
        child: View,
        outRect: Rect
    ) {
        val adapter = parent.adapter ?: return
        val position = parent.getChildAdapterPosition(child)
        val count = adapter.itemCount

        if (!showLastDivider && position == count - 1) {
            outRect.set(0, 0, 0, 0)
        } else if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerHeight)
        } else {
            outRect.set(0, 0, mDividerHeight, 0)
        }
    }


    private fun drawLinearLayout(canvas: Canvas, layoutManager: LinearLayoutManager, parent: RecyclerView) {
        parent.adapter ?: return
        if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
            drawLinearLayoutHorizontal(canvas, parent)
        } else {
            drawLinearLayoutVertical(canvas, parent)
        }
    }

    private fun drawLinearLayoutHorizontal(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + mPaddingStart
        val bottom = parent.height - parent.paddingBottom - mPaddingEnd

        val childCount = if (showLastDivider) parent.childCount else parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val x = child.right + params.rightMargin + mDividerHeight / 2F
            canvas.drawLine(x, top.toFloat(), x, bottom.toFloat(), mDividerPaint)
        }
    }

    private fun drawLinearLayoutVertical(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft + mPaddingStart
        val right = parent.width - parent.paddingRight - mPaddingEnd

        val childCount = if (showLastDivider) parent.childCount else parent.childCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val y = top + mDividerHeight / 2F
            canvas.drawLine(left.toFloat(), y, right.toFloat(), y, mDividerPaint)
        }
    }

    /**
     * Grid画线
     */
    private fun drawGridLayout(canvas: Canvas, parent: RecyclerView) {
        parent.adapter ?: return
        drawGridLayoutHorizontal(canvas, parent)
        drawGridLayoutVertical(canvas, parent)
    }

    private fun drawGridLayoutHorizontal(canvas: Canvas, parent: RecyclerView) {
        val spacing = mDividerHeight / 2F
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val x = child.right + params.rightMargin + spacing
            val top = child.top - params.topMargin
            val bottom = child.bottom + params.bottomMargin
            canvas.drawLine(x, top.toFloat(), x, bottom.toFloat(), mDividerPaint)
        }
    }

    private fun drawGridLayoutVertical(canvas: Canvas, parent: RecyclerView) {
        val spacing = mDividerHeight / 2F
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val right = child.right + params.rightMargin
            val y = child.bottom + params.bottomMargin + spacing

            canvas.drawLine(left.toFloat(), y, right.toFloat(), y, mDividerPaint)
        }
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.spanCount
        }
        return -1
    }

    /**
     * 是否是最后一列，如果是则不进行设置
     */
    private fun isLastColumn(parent: RecyclerView, pos: Int, spanCount: Int, childCount: Int): Boolean {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                return (pos + 1) % spanCount == 0
            }

            is StaggeredGridLayoutManager -> {
                return if (layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL) {
                    (pos + 1) % spanCount == 0
                } else {
                    val result = childCount - childCount % spanCount
                    pos >= result
                }
            }
        }

        return false
    }

    /**
     * 是否是最后一行，如果是最后一行不需要进行绘制
     */
    private fun isLastRaw(parent: RecyclerView, pos: Int, spanCount: Int, childCount: Int): Boolean {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                val position = childCount - childCount % spanCount
                return pos >= position
            }

            is StaggeredGridLayoutManager -> {
                // StaggeredGridLayoutManager 且纵向滚动
                return if (layoutManager.orientation == StaggeredGridLayoutManager.VERTICAL) {
                    val position = childCount - childCount % spanCount
                    pos >= position
                } else {
                    // StaggeredGridLayoutManager 且横向滚动
                    (pos + 1) % spanCount == 0
                }
            }
        }

        return false
    }
}