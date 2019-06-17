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

package com.ishow.common.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ishow.common.R;

/**
 * Created by yuhaiyang on 2017/6/1.
 * 带有动画的RecycleView
 */

public class AnimationRecyclerView extends RecyclerView {
    private static final String TAG = "AnimationRecyclerView";

    private final static int[] ATTRS = new int[]{android.R.attr.layoutAnimation};

    private int mLayoutAnimaitonId;

    public AnimationRecyclerView(Context context) {
        this(context, null);
    }

    public AnimationRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        mLayoutAnimaitonId = a.getResourceId(0, -1);
        a.recycle();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        // 如果已经设置过那么不再重复设置
        if (mLayoutAnimaitonId != -1) {
            Log.i(TAG, "setLayoutManager: ani is already set");
            return;
        }

        if (layout == null) {
            Log.i(TAG, "setLayoutManager: layout is null");
            return;
        }

        LayoutAnimationController controller = null;
        if (layout instanceof GridLayoutManager) {
            controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.grid_layout_animation);
        } else if (layout instanceof LinearLayoutManager) {
            controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.linear_layout_animation);
        }
        if (controller != null) {
            setLayoutAnimation(controller);
        }
    }

    @Override
    protected void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        LayoutManager layoutManager = getLayoutManager();
        if (getAdapter() == null || layoutManager == null) {
            super.attachLayoutAnimationParameters(child, params, index, count);
            return;
        }

        if (layoutManager instanceof GridLayoutManager) {
            attachGridAnimationParameters(params, index, count);
        } else if (layoutManager instanceof LinearLayoutManager) {
            attachLinearLayoutAnimationParameters(params, index, count);
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count);
        }
    }


    private void attachLinearLayoutAnimationParameters(ViewGroup.LayoutParams params, int index, int count) {
        LayoutAnimationController.AnimationParameters animationParameters = params.layoutAnimationParameters;
        if (animationParameters == null) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(1000);
            animationParameters = new LayoutAnimationController.AnimationParameters();
            params.layoutAnimationParameters = animationParameters;
        }
        animationParameters.count = count;
        animationParameters.index = index;
    }

    private void attachGridAnimationParameters(ViewGroup.LayoutParams params, int index, int count) {
        GridLayoutAnimationController.AnimationParameters animationParams = (GridLayoutAnimationController.AnimationParameters) params.layoutAnimationParameters;

        if (animationParams == null) {
            animationParams = new GridLayoutAnimationController.AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }

        int columns = ((GridLayoutManager) getLayoutManager()).getSpanCount();

        animationParams.count = count;
        animationParams.index = index;
        animationParams.columnsCount = columns;
        animationParams.rowsCount = count / columns;

        final int invertedIndex = count - 1 - index;
        animationParams.column = columns - 1 - (invertedIndex % columns);
        animationParams.row = animationParams.rowsCount - 1 - invertedIndex / columns;
    }
}
