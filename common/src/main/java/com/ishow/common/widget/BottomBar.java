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
package com.ishow.common.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ishow.common.R;

/**
 * 最底部的一条对应TopBar
 */
public class BottomBar extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "BottomBar";

    /**
     * 可放大多少
     */
    private static final float ZOOM = 1.05f;
    /**
     * 选中切换的监听
     */
    private OnBottomBarListener mBottomBarListener;
    /**
     * 选中的ID
     */
    private int mSelectedId;
    /**
     * 不能进行点选的ID 一般只有一个
     */
    private int mCanNotSelectedId;
    /**
     * 上次选中时间
     */
    private long mLastSelectedTime;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BottomBar);
        mSelectedId = a.getResourceId(R.styleable.BottomBar_selectedChild, View.NO_ID);
        mCanNotSelectedId = a.getResourceId(R.styleable.BottomBar_cannotSelectedChild, View.NO_ID);

        int orientation = a.getInt(R.styleable.BottomBar_android_orientation, -1);

        if (orientation == -1) {
            setOrientation(HORIZONTAL);
        }
        a.recycle();
        init();
    }

    private void init() {
        // 设置Child View的监听
        setOnHierarchyChangeListener(new PassThroughHierarchyChangeListener());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mSelectedId != View.NO_ID) {
            setSelectedStateForView(mSelectedId, true);
        }
    }

    /**
     * 获取当前选中的ID
     */
    @SuppressWarnings("unused")
    public int getSelectedId() {
        return mSelectedId;
    }

    /**
     * 设置当前选中的ID
     */
    public void setSelectedId(@IdRes int id) {
        setSelectedId(id, false);
    }


    /**
     * 设置当前选中的ID
     */
    public void setSelectedId(@IdRes int id, boolean force) {
        final long nowTime = System.currentTimeMillis();
        if (nowTime - mLastSelectedTime < 300) {
            return;
        }
        mLastSelectedTime = nowTime;

        if (id == mCanNotSelectedId) {
            Log.i(TAG, "setSelectedId: id is can not selected id");
            return;
        }

        // 如果2个id相等 那么就返回
        if (id == mSelectedId && !force) {
            Log.i(TAG, "setSelectedId: is same id && not force");
            return;
        }

        // 1. 把 将要选中的View的状态修改为true
        if (id != -1) {
            setSelectedStateForView(id, true);
        }
        // 1. 把当前选中的View的状态修改为false
        setSelectedStateForView(mSelectedId, false);

        mSelectedId = id;
        if (mBottomBarListener != null) {
            // 计算选中了第几个
            int count = getChildCount();
            int index = 0;
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                if (mSelectedId == view.getId()) {
                    index = i;
                }
            }
            mBottomBarListener.onSelectedChanged(this, mSelectedId, index);
        }
    }

    /**
     * 更新选中View的状态
     */
    private void setSelectedStateForView(int viewId, boolean checked) {
        View view = findViewById(viewId);
        if (view == null) {
            Log.i(TAG, "setSelectedStateForView: unknow view");
            return;
        }
        view.setSelected(checked);
        zoomView(view, checked);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (mBottomBarListener != null) {
            mBottomBarListener.onClickChild(v, id == mSelectedId);
        }

        setSelectedId(id);

    }

    /**
     * 设置切换事件
     */
    public void setOnSelectedChangedListener(OnBottomBarListener listener) {
        mBottomBarListener = listener;
    }


    /**
     * 切换事件
     */
    public interface OnBottomBarListener {
        /**
         * @param parent   点击的View的父类
         * @param selectId 选中View 的ID
         * @param index    选中View的位置
         */
        void onSelectedChanged(ViewGroup parent, @IdRes int selectId, int index);

        /**
         * 点击子View
         *
         * @param v          子view
         * @param isSameView 是否是同一个View
         */
        void onClickChild(View v, boolean isSameView);
    }

    /**
     * 布局更改的Listener
     */
    private class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {

        @Override
        public void onChildViewAdded(View parent, View child) {
            child.setOnClickListener(BottomBar.this);
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            child.setOnClickListener(null);
        }
    }


    private void zoomView(final View view, boolean checked) {
        if (view == null) {
            Log.i(TAG, "zoomView: view is null");
            return;
        }

        final float start = checked ? 1f : ZOOM;
        final float end = checked ? ZOOM : 1f;

        view.clearAnimation();
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.setDuration(100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float zoom = (float) animation.getAnimatedValue();
                view.setScaleX(zoom);
                view.setScaleY(zoom);
            }
        });
        animator.start();

    }
}
