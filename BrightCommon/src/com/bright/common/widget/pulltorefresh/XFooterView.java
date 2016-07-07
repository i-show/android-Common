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

package com.bright.common.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bright.common.R;

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

    private static final int ROTATE_ANIM_DURATION = 180;

    private View mLayout;

    private View mProgressBar;
    private TextView mHintView;
    private View mLine;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

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
        mLayout = LayoutInflater.from(context).inflate(R.layout.widget_pulltorefresh_footer, null);
        mLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(mLayout);

        mProgressBar = mLayout.findViewById(R.id.footer_progressbar);
        mHintView = (TextView) mLayout.findViewById(R.id.footer_hint_text);
        mLine = mLayout.findViewById(R.id.footer_line);

        mRotateUpAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /**
     * Set footer view state
     *
     * @param newState
     * @see #STATE_LOADING
     * @see #STATE_NORMAL
     * @see #STATE_READY
     */
    public void setState(int newState) {
        if (newState == mState) return;

        switch (newState) {
            case STATE_NORMAL:
                mHintView.setText(R.string.pulltorefresh_footer_normal);
                mHintView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;

            case STATE_READY:
                if (mState != STATE_READY) {
                    mHintView.setText(R.string.pulltorefresh_footer_ready);
                }
                mHintView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;

            case STATE_LOADING:
                mProgressBar.setVisibility(View.VISIBLE);
                mHintView.setVisibility(View.INVISIBLE);
                break;
            case STATE_END:
                mHintView.setText(R.string.pulltorefresh_footer_end);
                mHintView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;
        }

        mState = newState;
    }

    /**
     * Set footer view bottom margin.
     *
     * @param margin
     */
    public void setBottomMargin(int margin) {
        if (margin < 0) return;
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = margin;
        mLayout.setLayoutParams(lp);
    }

    /**
     * Get footer view bottom margin.
     *
     * @return
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        mHintView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
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
