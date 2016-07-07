/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.widget.recyclerview.itemdecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.util.Log;
import android.view.View;

public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = "LinearItemDecoration";
    /**
     * 没有进行限制
     */
    private static final int NOT_SET = -1;
    private Drawable mDivider;
    private int mDividerSize;
    private int mOrientation;
    private boolean showLastDivier;

    public LinearItemDecoration(Context context, @DrawableRes int divider, @DimenRes int dividerSize, int orientation) {
        mDivider = context.getResources().getDrawable(divider);
        mDividerSize = context.getResources().getDimensionPixelSize(dividerSize);
        repairDividerSize();

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
        showLastDivier = show;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        // 减一 不画最后一个
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        // 减一 不画最后一个
        for (int i = 0; i < childCount - 1; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
        int position = parent.getChildAdapterPosition(view);
        int count = parent.getLayoutManager().getItemCount();
        if (!showLastDivier && position == count - 1) {
            outRect.set(0, 0, 0, 0);
        } else if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerSize);
        } else {
            outRect.set(0, 0, mDividerSize, 0);
        }
    }

    private void repairDividerSize() {
        if (mDivider == null) {
            Log.i(TAG, "repairDividerSize: mDivier is null");
            return;
        }

        if (mDividerSize != NOT_SET) {
            Log.i(TAG, "repairDividerSize already set just use");
            return;
        }

        if (mOrientation == LinearLayoutManager.VERTICAL) {
            mDividerSize = mDivider.getIntrinsicWidth();
        } else {
            mDividerSize = mDivider.getIntrinsicHeight();
        }
    }
}