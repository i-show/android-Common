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

package com.ishow.common.widget.recyclerview.itemdecoration

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.utils.log.LogUtils

/**
 * 只进行增加空边距的
 */
class SpacingDecoration constructor(context: Context, @DimenRes spacing: Int) :
        RecyclerView.ItemDecoration() {
    /**
     * Item间距
     */
    private val mSpacing: Int = context.resources.getDimensionPixelSize(spacing)

    /**
     * 是否显示最后的Diver
     * 仅支持 LinearLayoutManager
     */
    var showLastDivider: Boolean = false


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                spacingGrid(outRect)
            }
            is LinearLayoutManager -> {
                spacingLinear(layoutManager, parent, view, outRect)
            }
        }
    }

    /**
     * GridLayoutManager 增加间距
     */
    private fun spacingGrid(outRect: Rect) {
        val spacing = mSpacing / 2
        outRect.set(spacing, spacing, spacing, spacing)
    }

    /**
     * LinearLayoutManager 增加间距
     */
    private fun spacingLinear(layoutManager: LinearLayoutManager, parent: RecyclerView, child: View, outRect: Rect) {
        val adapter = parent.adapter ?: return
        val position = parent.getChildAdapterPosition(child)
        val count = adapter.itemCount

        if (!showLastDivider && position == count - 1) {
            outRect.set(0, 0, 0, 0)
        } else if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mSpacing)
        } else {
            outRect.set(0, 0, mSpacing, 0)
        }
    }


}