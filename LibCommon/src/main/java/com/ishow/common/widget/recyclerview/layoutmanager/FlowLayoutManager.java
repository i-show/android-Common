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

package com.ishow.common.widget.recyclerview.layoutmanager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


public class FlowLayoutManager extends RecyclerView.LayoutManager {
    /**
     * 总共位移的距离
     */
    private int mTotalOffset;
    /**
     * 第一个可见的item position
     */
    private int mFirstVisiblePosition;
    /**
     * 最后一个可见的item 的position
     */
    private int mLastVisiblePosition;
    /**
     * 所有Item的位置信息（下滑时候使用）
     */
    private SparseArray<Rect> mItemRects;

    public FlowLayoutManager() {
        setAutoMeasureEnabled(true);
        mItemRects = new SparseArray<>();
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            return;
        }
        if (getChildCount() == 0 && state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        //初始化区域
        mTotalOffset = 0;
        mFirstVisiblePosition = 0;
        mLastVisiblePosition = getItemCount();

        fill(recycler);
    }

    /**
     * 初始化时调用 填充childView
     */
    private void fill(RecyclerView.Recycler recycler) {
        fill(recycler, 0);
    }


    private int fill(RecyclerView.Recycler recycler, int dy) {
        /*
         * 先移除已经不可见的View
         */
        removeAlreadyOutChild(recycler, dy);

        /*
         * 重新布局 可见的View
         */
        if (dy >= 0) {
            return fillUp(recycler, dy);
        } else {
            return fillDown(recycler, dy);
        }
    }

    /**
     * 移除不可见的View
     */
    private void removeAlreadyOutChild(RecyclerView.Recycler recycler, final int dy) {
        final int childCount = getChildCount();
        // 如果没有Child或者没有移动 那么不需要操作
        if (childCount <= 0 || dy == 0) {
            return;
        }
        final int topOffset = getPaddingTop();
        final int bottomOffset = getPaddingBottom();
        final int recyclerHeight = getHeight();

         /*
         * 当有View填充的时候并且有滑动的时候 那么重新计算滑动后的position
         * dy > 0 为上滑
         * dy < 0 为下滑
         */
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (dy > 0 && isAlreadyScrollUpOut(child, dy, topOffset)) {
                removeAndRecycleView(child, recycler);
                mFirstVisiblePosition++;
            } else if (dy < 0 && isAlreadyScrollDownOut(child, dy, recyclerHeight, bottomOffset)) {
                removeAndRecycleView(child, recycler);
                mLastVisiblePosition--;
            }
        }
    }

    /**
     * 上滑
     */
    private int fillUp(RecyclerView.Recycler recycler, int dy) {
        final int width = getHorizontalSpace();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int lineHeight = 0;

        int firstPosition = mFirstVisiblePosition;
        mLastVisiblePosition = getItemCount() - 1;

        /*
         * 如果已经有View add 那么从可见View的下一个进行排序
         * 不需要对已经排序的重新排序
         */
        if (getChildCount() > 0) {
            View lastView = getChildAt(getChildCount() - 1);
            firstPosition = getPosition(lastView) + 1;
            top = getDecoratedTop(lastView);
            left = getDecoratedRight(lastView);
            lineHeight = Math.max(lineHeight, getDecoratedMeasurementVertical(lastView));
        }

        //顺序addChildView
        for (int i = firstPosition; i <= mLastVisiblePosition; i++) {
            View child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, 0, 0);
            final int[] childSize = getViewSize(child);
            final int childWidth = childSize[0];
            final int childHeight = childSize[1];
            // 当前行
            if (left + childWidth <= width) {
                layoutDecoratedWithMargins(child, left, top, left + childWidth, top + childHeight);

                /*
                 * 保存Rect供逆序layout用
                 * 备注：为什么要 + mTotalOffset;
                 */
                Rect rect = new Rect(left, top + mTotalOffset, left + childWidth, top + childHeight + mTotalOffset);
                mItemRects.put(i, rect);

                left += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            } else {
                /*
                 * 换行操作
                 */
                left = getPaddingLeft();
                top += lineHeight;
                lineHeight = 0;

                //新起一行的时候要判断一下边界
                if (top - dy > getHeight() - getPaddingBottom()) {
                    //越界了 就回收
                    removeAndRecycleView(child, recycler);
                    mLastVisiblePosition = i - 1;
                } else {
                    layoutDecoratedWithMargins(child, left, top, left + childWidth, top + childHeight);

                    Rect rect = new Rect(left, top + mTotalOffset, left + childWidth, top + childHeight + mTotalOffset);
                    mItemRects.put(i, rect);

                    left += childWidth;
                    lineHeight = Math.max(lineHeight, childHeight);
                }
            }
        }

        return checkMoveUpOffset(dy);
    }


    /**
     * 下滑
     */
    private int fillDown(RecyclerView.Recycler recycler, int dy) {
        int lastPosition = getItemCount() - 1;
        mFirstVisiblePosition = 0;
        /*
         * 以顶部第一个不可见的View作为最后一个进行倒序添加View
         */
        if (getChildCount() > 0) {
            View firstView = getChildAt(0);
            lastPosition = getPosition(firstView) - 1;
        }

        for (int i = lastPosition; i >= mFirstVisiblePosition; i--) {
            Rect rect = mItemRects.get(i);

            if (rect.bottom - mTotalOffset - dy < getPaddingTop()) {
                mFirstVisiblePosition = i + 1;
                break;
            } else {
                View child = recycler.getViewForPosition(i);
                addView(child, 0);//将View添加至RecyclerView中，childIndex为1，但是View的位置还是由layout的位置决定
                measureChildWithMargins(child, 0, 0);

                layoutDecoratedWithMargins(child, rect.left, rect.top - mTotalOffset, rect.right, rect.bottom - mTotalOffset);
            }
        }
        return dy;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    /**
     * dy > 0 上滑
     * dy < 0 下滑
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy == 0 || getChildCount() == 0) {
            return 0;
        }

        int realOffset = checkMoveOffset(dy);
        // 如果当前需要移动的距离为0 那么不需要其他操作
        if (realOffset == 0) {
            return 0;
        }

        realOffset = fill(recycler, realOffset);
        mTotalOffset += realOffset;
        offsetChildrenVertical(-realOffset);
        return realOffset;
    }


    private int checkMoveOffset(int dy) {
        /*
         * 下滑 判断距离上边界
         */
        if (dy < 0 && mTotalOffset + dy < 0) {
            return -mTotalOffset;
        }
        /*
         * 上滑 判断下边界
         */
        if (dy > 0) {
            return checkMoveUpOffset(dy);
        }
        return dy;
    }

    /**
     * 通过最后一个显示view和adapter中最后一个view来判断 移动距离
     */
    private int checkMoveUpOffset(final int dy) {

        View lastChild = getChildAt(getChildCount() - 1);
        if (getPosition(lastChild) == getItemCount() - 1) {
            int gap = getHeight() - getPaddingBottom() - getViewBottomWithMargin(lastChild);
            // 如果空出来的距离比要移动的距离大 那么不需要移动
            if (gap == 0 || Math.abs(gap) >= Math.abs(dy)) {
                return 0;
            }

            if (gap > 0) {
                return -gap;
            } else {
                return Math.min(dy, -gap);
            }
        }
        return dy;
    }

    /**
     * 是否已经上滑 出顶部
     *
     * @param dy 滑动的距离
     */
    private boolean isAlreadyScrollUpOut(final View child, int dy, int parentTop) {
        /*
         * 这里往上滑动 dy > 0 ,所以bootom - dy < pTop 就是已经不可见状态
         */
        return getDecoratedBottom(child) - dy < parentTop;
    }

    /**
     * 是否已经下午出底部
     *
     * @param dy 滑动的距离
     */
    private boolean isAlreadyScrollDownOut(final View child, int dy, int parentHeight, int paddingBottom) {
        /*
         * 这里往下滑动 dy < 0 ,所以childTop - dy < height - pTop 就是已经不可见状态
         */
        return getDecoratedTop(child) - dy > parentHeight - paddingBottom;
    }

    /**
     * 获取某个childView在水平方向所占的空间
     */
    @SuppressWarnings("WeakerAccess")
    public int[] getViewSize(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        int[] size = new int[2];
        size[0] = getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
        size[1] = getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
        return size;
    }

    /**
     * 获取 View的bottom 包含margin
     */
    private int getViewBottomWithMargin(View view) {
        if (view == null) {
            return 0;
        }
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedBottom(view) + params.bottomMargin;
    }

    /**
     * 获取某个childView在水平方向所占的空间
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     */
    @SuppressWarnings("WeakerAccess")
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
    }


    @SuppressWarnings("unused")
    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @SuppressWarnings("WeakerAccess")
    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
