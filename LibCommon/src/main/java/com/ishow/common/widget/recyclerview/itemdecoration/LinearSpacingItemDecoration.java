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

package com.ishow.common.widget.recyclerview.itemdecoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 只进行增加空边距的
 * <p>
 * 注意：
 * 1. 目前仅仅支持 GridLayoutManager
 */
public class LinearSpacingItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * Item间距
     */
    private int mSpacing;

    private int mOrientation;

    private boolean isShowLastDivider;

    public LinearSpacingItemDecoration(Context context, @DimenRes int spacing) {
        this(context, spacing, LinearLayoutManager.VERTICAL);
    }

    @SuppressWarnings("WeakerAccess")
    public LinearSpacingItemDecoration(Context context, @DimenRes int spacing, int orientation) {
        mSpacing = context.getResources().getDimensionPixelSize(spacing);
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    /**
     * 是否显示最后的Diver
     */
    public void setShowLastDivier(boolean show) {
        isShowLastDivider = show;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            offsetVertical(parent, adapter, view, outRect);
        } else {
            offsetHorizontal(parent, adapter, view, outRect);
        }
    }

    private void offsetVertical(RecyclerView parent, RecyclerView.Adapter adapter, View child, Rect outRect) {
        if (isShowLastDivider) {
            outRect.set(0, 0, 0, mSpacing);
        } else {
            int count = adapter.getItemCount();
            int position = parent.getChildAdapterPosition(child);
            if (position >= count - 1) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, mSpacing);
            }
        }
    }

    private void offsetHorizontal(RecyclerView parent, RecyclerView.Adapter adapter, View child, Rect outRect) {
        if (isShowLastDivider) {
            outRect.set(0, 0, mSpacing, 0);
        } else {
            int count = adapter.getItemCount();
            int position = parent.getChildAdapterPosition(child);
            if (position >= count - 1) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, mSpacing, 0);
            }
        }
    }
}