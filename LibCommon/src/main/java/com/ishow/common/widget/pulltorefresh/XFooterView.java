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

package com.ishow.common.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.common.R;

/**
 * Footer加载
 */
public class XFooterView extends LinearLayout {
    /**
     * 普通状态
     */
    public final static int STATE_NORMAL = 0;
    /**
     * 准备状态
     */
    public final static int STATE_READY = 1;
    /**
     * 加载状态
     */
    public final static int STATE_LOADING = 2;
    /**
     * 加载完毕
     */
    public final static int STATE_END = 3;

    private static final int ROTATE_ANIM_DURATION = 300;

    private View mLayout;

    private TextView mLoadMoreTextView;
    private ImageView mLoadMoreLoadingView;
    private View mLine;

    private Animation mRotateLoading;

    private int mState = STATE_NORMAL;

    public XFooterView(Context context) {
        super(context);
        initView(context);
    }

    public XFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mLayout = LayoutInflater.from(context).inflate(R.layout.widget_pulltorefresh_footer, this, false);
        mLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(mLayout);

        mLoadMoreTextView = (TextView) mLayout.findViewById(R.id.pull_to_refesh_footer_text);
        mLoadMoreLoadingView = (ImageView) mLayout.findViewById(R.id.pull_to_refesh_footer_loading);
        mLine = mLayout.findViewById(R.id.footer_line);

        mRotateLoading = new RotateAnimation(0, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateLoading.setDuration(ROTATE_ANIM_DURATION * 2);
        mRotateLoading.setRepeatCount(Animation.INFINITE);
        mRotateLoading.setFillAfter(false);
        setNormalStatus();
    }

    /**
     * Set footer view state
     *
     * @see #STATE_LOADING
     * @see #STATE_NORMAL
     * @see #STATE_READY
     */
    public void setState(int newState) {
        if (newState == mState) return;

        switch (newState) {
            case STATE_NORMAL:
                setNormalStatus();
                break;

            case STATE_READY:
                mLoadMoreLoadingView.setVisibility(View.GONE);
                mLoadMoreTextView.setText(R.string.pulltorefresh_footer_ready);
                break;

            case STATE_LOADING:
                setLoadingStatus();
                break;
            case STATE_END:
                mLoadMoreLoadingView.setVisibility(View.GONE);
                mLoadMoreTextView.setText(R.string.pulltorefresh_footer_end);
                break;
        }

        mState = newState;
    }

    /**
     * Get footer view bottom margin.
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * Set footer view bottom margin.
     */
    public void setBottomMargin(int margin) {
        if (margin < 0) return;
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = margin;
        mLayout.setLayoutParams(lp);
    }

    /**
     * setNormalStatus status
     */
    public void setNormalStatus() {
        mLoadMoreLoadingView.setVisibility(View.GONE);
        mLoadMoreTextView.setText(R.string.pulltorefresh_footer_normal);
    }

    /**
     * setLoadingStatus status
     */
    public void setLoadingStatus() {
        mLoadMoreLoadingView.clearAnimation();
        mLoadMoreLoadingView.startAnimation(mRotateLoading);
        mLoadMoreLoadingView.setVisibility(View.VISIBLE);
        mLoadMoreTextView.setText(R.string.pulltorefresh_footer_loading);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(lp);
    }

    /**
     * 设置线是否可见
     */
    public void setLineVisibility(int visibility) {
        mLine.setVisibility(visibility);
    }
}
