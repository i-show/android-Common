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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bright.common.R;

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

    private static final int ROTATE_ANIM_DURATION = 180;

    private LinearLayout mContainer;

    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;

    private TextView mHintTextView;
    private View mLine;

    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

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

        mArrowImageView = (ImageView) findViewById(R.id.header_arrow);
        mHintTextView = (TextView) findViewById(R.id.header_hint_text);
        mProgressBar = (ProgressBar) findViewById(R.id.header_progressbar);
        mLine = findViewById(R.id.header_line);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int newState) {
        if (newState == mState && mIsFirst) {
            mIsFirst = true;
            return;
        }

        switch (newState) {
            case STATE_NORMAL:
                mHintTextView.setText(R.string.pulltorefresh_header_normal);
                mArrowImageView.setImageResource(R.drawable.arrow_down);
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }

                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }

                break;

            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.pulltorefresh_header_ready);
                }
                mArrowImageView.setImageResource(R.drawable.arrow_down);
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;

            case STATE_REFRESHING:
                mHintTextView.setText(R.string.pulltorefresh_header_loading);
                mArrowImageView.clearAnimation();
                mArrowImageView.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case STATE_REFRESH_SUCCESS:
                mHintTextView.setText(R.string.pulltorefresh_header_success);
                mArrowImageView.setImageResource(R.drawable.ic_refresh_success);
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;
            case STATE_REFRESH_FAIL:
                mHintTextView.setText(R.string.pulltorefresh_header_fail);
                mArrowImageView.setImageResource(R.drawable.ic_refresh_fail);
                mArrowImageView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        mState = newState;
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
     * Get the header view visible height.
     */
    public int getVisibleHeight() {
        return mContainer.getHeight();
    }


    /**
     * 设置线是否可见
     */
    public void setLineVisibility(int visibility) {
        mLine.setVisibility(visibility);
    }
}
