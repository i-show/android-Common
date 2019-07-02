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

package com.ishow.common.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.GridLayoutAnimationController
import android.view.animation.LayoutAnimationController

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ishow.common.R

/**
 * Created by yuhaiyang on 2017/6/1.
 * 带有动画的RecycleView
 */

class AnimationRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : RecyclerView(context, attrs, defStyle) {

    private val mLayoutAnimationId: Int

    init {
        val a = context.obtainStyledAttributes(attrs, ATTRS)
        mLayoutAnimationId = a.getResourceId(0, -1)
        a.recycle()
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        // 如果已经设置过那么不再重复设置
        if (mLayoutAnimationId != -1) {
            Log.i(TAG, "setLayoutManager: ani is already set")
            return
        }

        if (layout == null) {
            Log.i(TAG, "setLayoutManager: layout is null")
            return
        }

        var controller: LayoutAnimationController? = null
        if (layout is GridLayoutManager) {
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation)
        } else if (layout is LinearLayoutManager) {
            controller = AnimationUtils.loadLayoutAnimation(context, R.anim.linear_layout_animation)
        }
        controller?.let {
            layoutAnimation = it
        }
    }

    override fun attachLayoutAnimationParameters(child: View, params: ViewGroup.LayoutParams, index: Int, count: Int) {
        val layoutManager = layoutManager
        if (adapter == null || layoutManager == null) {
            super.attachLayoutAnimationParameters(child, params, index, count)
            return
        }

        when (layoutManager) {
            is GridLayoutManager -> attachGridAnimationParameters(params, index, count)
            is LinearLayoutManager -> attachLinearLayoutAnimationParameters(params, index, count)
            else -> super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }


    private fun attachLinearLayoutAnimationParameters(params: ViewGroup.LayoutParams, index: Int, count: Int) {
        var aniParams: LayoutAnimationController.AnimationParameters? = params.layoutAnimationParameters
        if (aniParams == null) {
            aniParams = LayoutAnimationController.AnimationParameters()
            params.layoutAnimationParameters = aniParams
        }
        aniParams.count = count
        aniParams.index = index
    }

    private fun attachGridAnimationParameters(params: ViewGroup.LayoutParams, index: Int, count: Int) {
        val aniParams: GridLayoutAnimationController.AnimationParameters
        if (params.layoutAnimationParameters == null) {
            aniParams = GridLayoutAnimationController.AnimationParameters()
            params.layoutAnimationParameters = aniParams
        }else{
            aniParams = params.layoutAnimationParameters as GridLayoutAnimationController.AnimationParameters
        }

        val columns = (layoutManager as GridLayoutManager).spanCount

        aniParams.count = count
        aniParams.index = index
        aniParams.columnsCount = columns
        aniParams.rowsCount = count / columns

        val invertedIndex = count - 1 - index
        aniParams.column = columns - 1 - invertedIndex % columns
        aniParams.row = aniParams.rowsCount - 1 - invertedIndex / columns
    }

    companion object {
        private const val TAG = "AnimationRecyclerView"

        private val ATTRS = intArrayOf(android.R.attr.layoutAnimation)
    }
}
