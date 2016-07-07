/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

package com.bright.common.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bright.common.R;
import com.bright.common.utils.UnitUtils;
import com.bumptech.glide.Glide;

/**
 * 一个状态显示的View
 */
public class StatusView extends FrameLayout {
    /**
     * 正在加载
     */
    public final static int STATUS_LOADING = 1 << 1;
    /**
     * 加载失败
     */
    public final static int STATUS_ERROR = 1 << 2;

    /**
     * 动画时长
     */
    private static final long ANIT_DURATION = 500;


    private static final int HANDLER_DISMISS = 1000;
    private static final int DISMISS_DELAY = 500;


    private ViewGroup mLoadingView;
    private ViewGroup mErrorView;
    private ObjectAnimator mDismissAni;

    private String mLoadingText;
    private String mErrorText;
    private int mErrorImageRes;
    private int mTextColor;
    private int mTextSize;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_DISMISS:
                    if (mDismissAni == null) {
                        mDismissAni = getDismissAnimator();
                    }
                    mDismissAni.start();
                    break;
            }
        }
    };

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusView);
        mLoadingText = a.getString(R.styleable.StatusView_loadingText);
        mErrorText = a.getString(R.styleable.StatusView_errorText);
        mErrorImageRes = a.getResourceId(R.styleable.StatusView_errorImage, R.drawable.no_picture);
        mTextColor = a.getColor(R.styleable.StatusView_textColor, getDefaultColor());
        mTextSize = a.getColor(R.styleable.StatusView_textSize, getDefaultTextSize());
        a.recycle();

        mLoadingView = initLoadingView();
        hideLoading();
        addView(mLoadingView);
        mErrorView = initErrorView();
        hideError();
        addView(mErrorView);
    }

    protected ViewGroup initLoadingView() {
        return getBaseView(TAG.LOADING_IMAGE, TAG.LOADING_TEXT);
    }

    protected ViewGroup initErrorView() {
        return getBaseView(TAG.ERROR_IMAGE, TAG.ERROR_TEXT);
    }

    public void hideError() {
        mErrorView.setVisibility(GONE);
    }

    public void showError() {
        showError(mErrorText, mErrorImageRes);
    }

    public void showError(@StringRes int errorTextId, int errorImage) {
        String errorText = getContext().getString(errorTextId);
        showError(errorText, errorImage);
    }

    public void showError(String errorText, int errorImage) {
        hideLoading();
        cancelDismiss();
        mErrorView.setVisibility(VISIBLE);
        mErrorText = errorText;
        mErrorImageRes = errorImage;

        TextView tip = (TextView) mErrorView.findViewWithTag(TAG.ERROR_TEXT);
        tip.setText(mErrorText);

        ImageView image = (ImageView) mErrorView.findViewWithTag(TAG.ERROR_IMAGE);
        image.setImageResource(mErrorImageRes);
    }

    private void hideLoading() {
        mLoadingView.setVisibility(GONE);
    }

    public void showLoading() {
        showLoading(null, false);
    }

    public void showLoading(boolean isTextVisibility) {
        showLoading(null, isTextVisibility);
    }

    public void showLoading(String loadingText) {
        showLoading(loadingText, true);
    }

    public void showLoading(@StringRes int text) {
        showLoading(text, true);
    }

    public void showLoading(@StringRes int text, boolean isTextVisibility) {
        String loadStr = getContext().getString(text);
        showLoading(loadStr, isTextVisibility);
    }

    public void showLoading(String text, boolean isTextVisibility) {
        hideError();
        cancelDismiss();
        mLoadingView.setVisibility(VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            mLoadingText = text;
        }

        TextView tip = (TextView) mLoadingView.findViewWithTag(TAG.LOADING_TEXT);
        tip.setVisibility(isTextVisibility ? VISIBLE : GONE);
        tip.setText(mLoadingText);
        ImageView image = (ImageView) mLoadingView.findViewWithTag(TAG.LOADING_IMAGE);
        Glide.with(getContext())
                .load(R.drawable.default_loading)
                .asGif()
                .into(image);
    }

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean animation) {
        if (animation) {
            if (getAlpha() == 1F) {
                mHandler.removeMessages(HANDLER_DISMISS);
                mHandler.sendEmptyMessageDelayed(HANDLER_DISMISS, DISMISS_DELAY);
            }
        } else {
            setAlpha(0f);
        }
    }

    public void cancelDismiss() {
        mHandler.removeMessages(HANDLER_DISMISS);
        setAlpha(1F);
    }

    private ViewGroup getBaseView(String tagImage, String tagText) {
        Context context = getContext();
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);
        View top = new View(context);
        content.addView(top, getBaseLayoutParams(5));

        ImageView image = new ImageView(context);
        image.setImageResource(R.drawable.default_loading);
        image.setScaleType(ImageView.ScaleType.CENTER);
        image.setTag(tagImage);
        content.addView(image);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = UnitUtils.dip2px(getContext(), 5);
        TextView text = new TextView(context);
        text.setGravity(Gravity.CENTER);
        text.setTextColor(mTextColor);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        text.setTag(tagText);
        content.addView(text, lp);

        View bottom = new View(context);
        content.addView(bottom, getBaseLayoutParams(4f));
        return content;
    }

    private LinearLayout.LayoutParams getBaseLayoutParams(float weight) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (weight > 0) {
            lp.weight = weight;
        }
        return lp;
    }

    private ObjectAnimator getDismissAnimator() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, "alpha", 1F, 0F);
        anim.setDuration(ANIT_DURATION);
        return anim;
    }


    public static final class TAG {
        public static final String LOADING_IMAGE = "loading_image";
        public static final String LOADING_TEXT = "loading_text";
        public static final String ERROR_IMAGE = "error_image";
        public static final String ERROR_TEXT = "error_text";
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float alpha = getAlpha();
        return alpha != 0;
    }

    public int getDefaultColor() {
        return getResources().getColor(R.color.text_color_light);
    }

    public int getDefaultTextSize() {
        return getResources().getDimensionPixelSize(R.dimen.G_title);
    }
}
