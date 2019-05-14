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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ishow.common.R;
import com.ishow.common.utils.StorageUtils;

/**
 * 发送验证码的button
 */
@SuppressWarnings("unused")
public class VerifyCodeButton extends FrameLayout {
    private static final String TAG = "VerifyCodeButton";
    /**
     * 最大时间
     */
    private static final int VERIFY_MAX_TIME = 60;
    /**
     * 开始计时
     */
    private static final int HANDLER_TIME = 1000;
    /**
     * 重新计时
     */
    private static final int HANDLER_RESET_TIME = 1001;
    /**
     * 空闲状态
     */
    private static final int STATE_IDLE = 1000;
    /**
     * 发送状态
     */
    private static final int STATE_SENDING = 1001;
    /**
     * 计时状态
     */
    private static final int STATE_TIMING = 1002;

    /**
     * 进度条
     */
    private ProgressBar mProgressBar;
    /**
     * 时间显示
     */
    private TextView mDisplayView;
    /**
     * 时间监听
     */
    private OnTimingListener mTimingListener;
    private String mTextStr;
    private String mTimingTextStr;

    /**
     * 字体的颜色
     */
    private ColorStateList mTextColor;
    /**
     * 字体的大小
     */
    private int mTextSize;
    /**
     * 当前时间
     */
    private int mCurrentTime;
    private int mMaxTime;
    /**
     * 当前状态
     */
    private int mStatus = STATE_IDLE;
    private String mStatusKey;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_TIME:
                    mCurrentTime--;
                    if (mCurrentTime <= 0) {
                        mHandler.sendEmptyMessage(HANDLER_RESET_TIME);
                    } else {
                        if (mTimingListener != null) {
                            mTimingListener.onTiming(mCurrentTime);
                        }
                        updateTiming();
                        sendEmptyMessageDelayed(HANDLER_TIME, 1000);
                    }
                    break;

                case HANDLER_RESET_TIME:
                    mStatus = STATE_IDLE;
                    mHandler.removeMessages(HANDLER_TIME);
                    mDisplayView.setText(mTextStr);
                    mDisplayView.setVisibility(VISIBLE);
                    mProgressBar.setVisibility(INVISIBLE);
                    mCurrentTime = mMaxTime;
                    setClickable(true);
                    if (mTimingListener != null) {
                        mTimingListener.onTimingEnd();
                    }
                    break;
            }
        }
    };

    public VerifyCodeButton(Context context) {
        this(context, null);
    }

    public VerifyCodeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeButton);
        int padding = a.getDimensionPixelSize(R.styleable.VerifyCodeButton_android_padding, getDefaultPadding());
        mTextStr = a.getString(R.styleable.VerifyCodeButton_text);
        mTimingTextStr = a.getString(R.styleable.VerifyCodeButton_timingText);
        mTextColor = a.getColorStateList(R.styleable.VerifyCodeButton_textColor);
        mTextSize = a.getDimensionPixelSize(R.styleable.VerifyCodeButton_textSize, getDefaultTextSize());
        a.recycle();

        if (mTextColor == null) {
            mTextColor = getDefaultTextColor();
        }

        if (TextUtils.isEmpty(mTextStr)) {
            mTextStr = getResources().getString(R.string.get_verify_code);
        }

        setPadding(padding, padding, padding, padding);
        mProgressBar = getProgressBar();
        mProgressBar.setVisibility(INVISIBLE);
        addView(mProgressBar);

        mDisplayView = getDisplayView();
        mDisplayView.setVisibility(VISIBLE);
        addView(mDisplayView);

        mMaxTime = VERIFY_MAX_TIME;

        final String contextName = context.getClass().getName();
        mStatusKey = contextName.replace(".", "_") + "_" + getId();
    }

    /**
     * 显示Loading
     */
    public void showLoading() {
        // 如果当前是空闲状态
        mStatus = STATE_SENDING;
        if (mTimingListener != null) {
            mTimingListener.onSending();
        }
        mProgressBar.setVisibility(VISIBLE);
        mDisplayView.setVisibility(INVISIBLE);
        setClickable(false);
    }

    /**
     * 开始计时
     */
    public void startTiming() {
        startTiming(VERIFY_MAX_TIME);
    }

    /**
     * 开始计时
     */
    public void startTiming(int maxTime) {
        startTiming(maxTime, maxTime);
    }


    /**
     * 开始计时
     */
    private void startTiming(int maxTime, int currentTime) {
        setClickable(false);
        mMaxTime = maxTime;
        mCurrentTime = currentTime;
        mStatus = STATE_TIMING;
        mProgressBar.setVisibility(INVISIBLE);
        mDisplayView.setVisibility(VISIBLE);
        mHandler.sendEmptyMessage(HANDLER_TIME);

        Status status = new Status();
        status.startDate = System.currentTimeMillis();
        status.remainTime = currentTime;
        status.maxTime = maxTime;

        StorageUtils.with(getContext())
                .param(mStatusKey, JSON.toJSONString(status))
                .save();
    }

    /**
     * 重新计时
     */
    public void reset() {
        mHandler.sendEmptyMessage(HANDLER_RESET_TIME);

        StorageUtils.with(getContext())
                .key(mStatusKey)
                .remove();
    }

    /**
     * 获取时间显示
     */
    private TextView getDisplayView() {
        final int padding = getDefaultPadding();
        TextView textView = new TextView(getContext());
        textView.setIncludeFontPadding(false);
        textView.setLayoutParams(getDefaultParam());
        textView.setGravity(Gravity.CENTER);
        textView.setText(mTextStr);
        textView.setTextColor(mTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        textView.setPadding(padding * 2, padding, padding * 2, padding);
        return textView;
    }

    /**
     * 获取进度条
     */
    private ProgressBar getProgressBar() {
        int padding = getDefaultPadding();
        ProgressBar progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall);
        progressBar.setLayoutParams(getDefaultParam());
        progressBar.setPadding(padding, padding, padding, padding);
        return progressBar;
    }

    /**
     * 获取默认的布局参数
     */
    private LayoutParams getDefaultParam() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    /**
     * 默认padding
     */
    private int getDefaultPadding() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.dp_3);
    }

    /**
     * 默认边距
     */
    private int getDefaultTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        String lastStatus = StorageUtils.with(getContext())
                .key(mStatusKey)
                .get(null);

        Log.i(TAG, "onAttachedToWindow: lastStatus = " + lastStatus);

        if (TextUtils.isEmpty(lastStatus)) {
            return;
        }

        Status status = JSON.parseObject(lastStatus, Status.class);

        int remainTime = status.remainTime - (int) (System.currentTimeMillis() - status.startDate) / 1000;
        Log.i(TAG, "onAttachedToWindow: remainTime = " + remainTime);
        if (remainTime > 0) {
            startTiming(status.maxTime, remainTime);
        } else {
            StorageUtils.with(getContext())
                    .key(mStatusKey)
                    .remove();
        }


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //   当不再显示的的时候移除控制
        mHandler.removeMessages(HANDLER_TIME);
    }

    /**
     * 获取默认的颜色值
     */
    private ColorStateList getDefaultTextColor() {
        return getContext().getResources().getColorStateList(R.color.text_grey_light_normal);
    }

    /**
     * 添加时间监听
     */
    public void setOnTimingListener(OnTimingListener listener) {
        mTimingListener = listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mDisplayView.setEnabled(enabled);
    }

    /**
     * 更新计时器
     */
    private void updateTiming() {
        if (TextUtils.isEmpty(mTimingTextStr)) {
            mDisplayView.setText(getContext().getString(R.string.second_timing, mCurrentTime));
        } else if (mTimingTextStr.contains("%d")) {
            mDisplayView.setText(String.format(mTimingTextStr, mCurrentTime));
        } else if (mTimingTextStr.contains("%s")) {
            mDisplayView.setText(String.format(mTimingTextStr, String.valueOf(mCurrentTime)));
        } else {
            throw new IllegalStateException("timingText must %d or %s");
        }
    }

    /**
     * 时间监听
     */
    @SuppressWarnings("WeakerAccess")
    public interface OnTimingListener {
        /**
         * 正在发送验证码
         */
        void onSending();

        /**
         * 正在倒计时
         *
         * @param time 当前时间
         */
        void onTiming(int time);

        /**
         * 倒计时结束
         */
        void onTimingEnd();
    }


    public static class Status {
        /**
         * 开始计时的日期
         */
        public long startDate;
        /**
         * 剩余时间
         */
        public int remainTime;
        /**
         * 最大时间
         */
        public int maxTime;
    }
}
