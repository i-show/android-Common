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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.common.R;

/**
 * Header
 */
public class XHeaderView extends LinearLayout {
    /**
     * 正常状态
     */
    public final static int STATE_NORMAL = 0;
    /**
     * 准备状态
     */
    public final static int STATE_READY = 1;
    /**
     * 正在刷新
     */
    public final static int STATE_REFRESHING = 2;
    /**
     * 刷新成功
     */
    public final static int STATE_REFRESH_SUCCESS = 3;
    /**
     * 刷新失败
     */
    public final static int STATE_REFRESH_FAIL = 4;

    private static final int ROTATE_ANIM_DURATION = 300;

    private LinearLayout mContainer;

    private ImageView mIconVIew;
    private TextView mTextView;
    private View mLine;

    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private Animation mRotateLoading;

    private boolean mIsFirst;

    public XHeaderView(Context context) {
        this(context, null);
    }

    public XHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }


    private void initView(Context context) {
        // Initial set header view height 0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.widget_pulltorefresh_header, null);
        addView(mContainer, lp);
        setGravity(Gravity.BOTTOM);

        mIconVIew = (ImageView) findViewById(R.id.pull_to_refresh_header_image);
        mTextView = (TextView) findViewById(R.id.pull_to_refresh_header_text);
        mLine = findViewById(R.id.header_line);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        mRotateLoading = new RotateAnimation(0, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateLoading.setDuration(ROTATE_ANIM_DURATION * 2);
        mRotateLoading.setRepeatCount(Animation.INFINITE);
        mRotateLoading.setFillAfter(false);
    }

    public void setState(int newState) {
        if (newState == mState && mIsFirst) {
            mIsFirst = true;
            return;
        }

        switch (newState) {
            case STATE_NORMAL:
                mTextView.setText(R.string.pulltorefresh_header_normal);
                mIconVIew.setImageResource(R.drawable.ic_pulltorefresh_arrow);
                mIconVIew.setVisibility(View.VISIBLE);
                if (mState == STATE_READY) {
                    mIconVIew.startAnimation(mRotateDownAnim);
                }

                if (mState == STATE_REFRESHING) {
                    mIconVIew.clearAnimation();
                }

                break;

            case STATE_READY:
                if (mState != STATE_READY) {
                    mIconVIew.clearAnimation();
                    mIconVIew.startAnimation(mRotateUpAnim);
                    mTextView.setText(R.string.pulltorefresh_header_ready);
                }
                mIconVIew.setImageResource(R.drawable.ic_pulltorefresh_arrow);
                mIconVIew.setVisibility(View.VISIBLE);
                break;

            case STATE_REFRESHING:
                mIconVIew.clearAnimation();
                mIconVIew.setImageResource(R.drawable.ic_pulltorefresh_loading);
                mIconVIew.startAnimation(mRotateLoading);
                mTextView.setText(R.string.pulltorefresh_header_loading);
                requestLayout();
                break;
            case STATE_REFRESH_SUCCESS:
                mTextView.setText(R.string.pulltorefresh_header_success);
                mIconVIew.setImageResource(R.drawable.ic_pulltorefresh_refresh_success);
                mIconVIew.setVisibility(View.VISIBLE);
                break;
            case STATE_REFRESH_FAIL:
                mTextView.setText(R.string.pulltorefresh_header_fail);
                mIconVIew.setImageResource(R.drawable.ic_pulltorefresh_refresh_fail);
                mIconVIew.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

        mState = newState;
    }

    /**
     * Get the header view visible height.
     */
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

    /**
     * Set the header view visible height.
     */
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * 设置线是否可见
     */
    public void setLineVisibility(int visibility) {
        mLine.setVisibility(visibility);
    }
}
