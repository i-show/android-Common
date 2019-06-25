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

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishow.common.R;
import com.ishow.common.utils.log.LogUtils;
import com.ishow.common.widget.spinkit.SpinKitView;

/**
 * 一个状态显示的View
 */
@SuppressWarnings("unused")
public class StatusView extends FrameLayout implements View.OnClickListener {
    private static final String TAG = "StatusView";
    /**
     * 正在加载
     */
    public final static int STATUS_LOADING = 1 << 1;
    /**
     * 加载失败
     */
    public final static int STATUS_ERROR = STATUS_LOADING + 1;
    /**
     * 加载为空
     */
    public final static int STATUS_EMPTY = STATUS_LOADING + 2;

    public enum Which {
        Title, SubTitle, Reload
    }

    private ImageView mIconView;
    private SpinKitView mLoadingView;
    private TextView mTitleView;
    private TextView mSubTitleView;
    private TextView mReloadView;

    private ObjectAnimator mDismissAni;

    private int mLoadingDrawableId;
    private String mLoadingText;
    private ColorStateList mLoadingTextColor;
    private int mLoadingTextSize;

    private int mEmptyDrawableId;
    private String mEmptyText;
    private ColorStateList mEmptyTextColor;
    private int mEmptyTextSize;
    private String mEmptySubText;
    private ColorStateList mEmptySubTextColor;
    private int mEmptySubTextSize;

    private int mErrorDrawableId;
    private String mErrorText;
    private ColorStateList mErrorTextColor;
    private int mErrorTextSize;

    private String mErrorSubText;
    private ColorStateList mErrorSubTextColor;
    private int mErrorSubTextSize;

    private Drawable mReloadTextBackground;
    private String mReloadText;
    private ColorStateList mReloadTextColor;
    private int mReloadTextSize;

    private boolean isInterruptTouchEvent;
    private boolean isTitleClickable;
    private boolean isSubTitleClickable;
    private View mTopWeightView;
    private float mTopWeight;
    private View mBottomWeightView;
    private float mBottomWeight;
    private OnStatusViewListener mOnStatusViewListener;

    public StatusView(Context context) {
        this(context, null);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusView, R.attr.statusStyle, R.style.Default_StatusView);
        mLoadingDrawableId = a.getResourceId(R.styleable.StatusView_loadingImage, -1);
        mLoadingText = a.getString(R.styleable.StatusView_loadingText);
        mLoadingTextColor = a.getColorStateList(R.styleable.StatusView_loadingTextColor);
        mLoadingTextSize = a.getDimensionPixelSize(R.styleable.StatusView_loadingTextSize, getDefaultTextSize());

        mEmptyDrawableId = a.getResourceId(R.styleable.StatusView_emptyImage, -1);
        mEmptyText = a.getString(R.styleable.StatusView_emptyText);
        mEmptyTextColor = a.getColorStateList(R.styleable.StatusView_emptyTextColor);
        mEmptyTextSize = a.getDimensionPixelSize(R.styleable.StatusView_emptyTextSize, getDefaultTextSize());
        mEmptySubText = a.getString(R.styleable.StatusView_emptySubText);
        mEmptySubTextColor = a.getColorStateList(R.styleable.StatusView_emptySubTextColor);
        mEmptySubTextSize = a.getDimensionPixelSize(R.styleable.StatusView_emptySubTextSize, getDefaultSubTextSize());

        mErrorDrawableId = a.getResourceId(R.styleable.StatusView_errorImage, -1);
        mErrorText = a.getString(R.styleable.StatusView_errorText);
        mErrorTextColor = a.getColorStateList(R.styleable.StatusView_errorTextColor);
        mErrorTextSize = a.getDimensionPixelSize(R.styleable.StatusView_errorTextSize, getDefaultTextSize());
        mErrorSubText = a.getString(R.styleable.StatusView_errorSubText);
        mErrorSubTextColor = a.getColorStateList(R.styleable.StatusView_errorSubTextColor);
        mErrorSubTextSize = a.getDimensionPixelSize(R.styleable.StatusView_errorSubTextSize, getDefaultSubTextSize());

        mReloadText = a.getString(R.styleable.StatusView_reloadText);
        mReloadTextColor = a.getColorStateList(R.styleable.StatusView_reloadTextColor);
        mReloadTextSize = a.getDimensionPixelSize(R.styleable.StatusView_reloadTextSize, getDefaultReloadTextSize());
        mReloadTextBackground = a.getDrawable(R.styleable.StatusView_reloadBackground);
        isInterruptTouchEvent = a.getBoolean(R.styleable.StatusView_interruptTouchEvent, true);
        isTitleClickable = a.getBoolean(R.styleable.StatusView_titleClickable, false);
        isSubTitleClickable = a.getBoolean(R.styleable.StatusView_subTitleClickable, false);
        mTopWeight = a.getFloat(R.styleable.StatusView_topWeight, 2.6F);
        mBottomWeight = a.getFloat(R.styleable.StatusView_topWeight, 5F);
        a.recycle();
        checkParams();
        initView();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.widget_status_view, this, true);
        mIconView = findViewById(R.id.icon);
        mLoadingView = findViewById(R.id.status_loading);
        mTitleView = findViewById(R.id.title);
        if (isTitleClickable) mTitleView.setOnClickListener(this);

        mSubTitleView = findViewById(R.id.subTitle);
        if (isSubTitleClickable) mSubTitleView.setOnClickListener(this);
        mReloadView = findViewById(R.id.reload);
        mReloadView.setOnClickListener(this);


        mTopWeightView = findViewById(R.id.topWeight);
        updateWeight(mTopWeightView, mTopWeight);
        mBottomWeightView = findViewById(R.id.bottomWeight);
        updateWeight(mBottomWeightView, mBottomWeight);
    }


    private void checkParams() {
        final ColorStateList defaultTextColor = getDefaultTextColor();
        final ColorStateList defaultSubTextColor = getDefaultSubTextColor();

        if (mLoadingTextColor == null) mLoadingTextColor = defaultSubTextColor;
        if (mErrorTextColor == null) mErrorTextColor = defaultTextColor;
        if (mErrorSubTextColor == null) mErrorSubTextColor = defaultSubTextColor;
        if (mEmptyTextColor == null) mEmptyTextColor = defaultTextColor;
        if (mEmptySubTextColor == null) mEmptySubTextColor = defaultSubTextColor;
        if (mReloadTextColor == null) mReloadTextColor = getDefaultReloadTextColor();
    }


    public void showError() {
        showError(mReloadText, mErrorText, mErrorSubText, mErrorDrawableId);
    }

    public void showError(@StringRes int title, int icon) {
        Context context = getContext();
        String titleString = context.getString(title);
        showError(mReloadText, titleString, mErrorSubText, icon);
    }

    public void showError(@StringRes int title, @StringRes int subTitle, int icon) {
        Context context = getContext();
        String titleString = context.getString(title);
        String subTitleString = context.getString(subTitle);
        showError(mReloadText, titleString, subTitleString, icon);
    }

    public void showError(@StringRes int reload, @StringRes int title, @StringRes int subTitle, int icon) {
        Context context = getContext();
        String reloadString = context.getString(reload);
        String titleString = context.getString(title);
        String subTitleString = context.getString(subTitle);
        showError(reloadString, titleString, subTitleString, icon);
    }

    public void showError(String reload, String title, String subTitle, int icon) {
        setVisibility(VISIBLE);
        mIconView.setImageResource(icon);
        mIconView.setVisibility(VISIBLE);
        stopLoadingAnimation();
        mLoadingView.setVisibility(GONE);

        mTitleView.setTextColor(mErrorTextColor);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mErrorTextSize);
        setText(mTitleView, title);
        mSubTitleView.setTextColor(mErrorSubTextColor);
        mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mErrorSubTextSize);
        setText(mSubTitleView, subTitle);
        mReloadView.setTextColor(mReloadTextColor);
        mReloadView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mReloadTextSize);
        mReloadView.setBackground(mReloadTextBackground);
        setText(mReloadView, reload);

    }

    public void showLoading() {
        showLoading(mLoadingText, mLoadingDrawableId);
    }

    public void showLoading(@StringRes int text) {
        String subTitle = getContext().getString(text);
        showLoading(subTitle, mLoadingDrawableId);
    }

    public void showLoading(String title, @DrawableRes int drawable) {
        setVisibility(VISIBLE);
        mIconView.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
        mTitleView.setTextColor(mLoadingTextColor);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLoadingTextSize);
        setText(mTitleView, title);
        mSubTitleView.setVisibility(GONE);
        mReloadView.setVisibility(GONE);
    }


    public void showEmpty() {
        showEmpty(mEmptyText, mEmptySubText, mEmptyDrawableId);
    }

    public void showEmpty(@StringRes int text) {
        String title = getContext().getString(text);
        showEmpty(title, mEmptySubText, mEmptyDrawableId);
    }

    public void showEmpty(@StringRes int text, @DrawableRes int icon) {
        String title = getContext().getString(text);
        showEmpty(title, mEmptySubText, icon);
    }

    public void showEmpty(@StringRes int text, @StringRes int subText, @DrawableRes int icon) {
        String title = getContext().getString(text);
        String subTitle = getContext().getString(text);
        showEmpty(title, subTitle, icon);
    }

    public void showEmpty(String title, @DrawableRes int icon) {
        showEmpty(title, mEmptySubText, icon);
    }

    public void showEmpty(String title, String subTitle, @DrawableRes int icon) {
        setVisibility(VISIBLE);
        mIconView.setImageResource(icon);
        mIconView.setVisibility(VISIBLE);
        stopLoadingAnimation();
        mLoadingView.setVisibility(GONE);

        mTitleView.setTextColor(mEmptyTextColor);
        mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEmptyTextSize);
        setText(mTitleView, title);

        mSubTitleView.setTextColor(mEmptySubTextColor);
        mSubTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEmptySubTextSize);
        setText(mSubTitleView, subTitle);

        mReloadView.setVisibility(GONE);
    }

    public void dismiss() {
        setVisibility(View.GONE);
    }

    public void cancelDismiss() {
        setVisibility(View.VISIBLE);
    }


    private void setText(TextView view, String text) {
        if (TextUtils.isEmpty(text)) {
            view.setVisibility(GONE);
        } else {
            view.setVisibility(VISIBLE);
        }
        view.setText(text);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (isInterruptTouchEvent) {
            return true;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTitleView) {
            notifyClickTitle();
        } else if (v == mSubTitleView) {
            notifyClickSubTitle();
        } else if (v == mReloadView) {
            notifyReload();
        }
    }

    private void startLoadingAnimation() {
        Drawable drawable = mIconView.getDrawable();
        if (!(drawable instanceof AnimationDrawable)) {
            LogUtils.e(TAG, "drawable is not AnimationDrawable");
            return;
        }

        ((AnimationDrawable) drawable).start();
    }

    private void stopLoadingAnimation() {
        mIconView.clearAnimation();
        Drawable drawable = mIconView.getDrawable();
        if (!(drawable instanceof AnimationDrawable)) {
            LogUtils.e(TAG, "drawable is not AnimationDrawable");
            return;
        }

        ((AnimationDrawable) drawable).stop();
    }

    public void setOnStatusViewListener(OnStatusViewListener callBack) {
        mOnStatusViewListener = callBack;
    }

    private void notifyClickTitle() {
        if (mOnStatusViewListener != null) {
            mOnStatusViewListener.onStatusClick(this, Which.Title);
        }
    }

    private void notifyClickSubTitle() {
        if (mOnStatusViewListener != null) {
            mOnStatusViewListener.onStatusClick(this, Which.SubTitle);
        }
    }

    private void notifyReload() {
        if (mOnStatusViewListener != null) {
            mOnStatusViewListener.onStatusClick(this, Which.Reload);
        }
    }

    public void setTitleClickable(boolean clickable) {
        mTitleView.setOnClickListener(clickable ? this : null);
    }

    public void setSubTitleClickable(boolean clickable) {
        mSubTitleView.setOnClickListener(clickable ? this : null);
    }

    private void updateWeight(View view, float weight) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) lp).weight = weight;
        }
    }

    public void setWeight(@FloatRange(from = 0F) float topWeight, @FloatRange(from = 0F)float bottomWeight) {
        mTopWeight = topWeight;
        updateWeight(mTopWeightView, mTopWeight);
        mBottomWeight = bottomWeight;
        updateWeight(mBottomWeightView, mBottomWeight);
    }

    public interface OnStatusViewListener {
        /**
         * 点击了副标题标题
         */
        void onStatusClick(View v, Which which);
    }

    /**
     * 默认字体大小
     */
    private int getDefaultTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    /**
     * 默认字体颜色
     */
    private ColorStateList getDefaultTextColor() {
        return ContextCompat.getColorStateList(getContext(), R.color.text_grey_normal);
    }

    /**
     * 默认副标题字体大小
     */
    private int getDefaultSubTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.J_title);
    }

    /**
     * 默认副标题字体颜色
     */
    private ColorStateList getDefaultSubTextColor() {
        return ContextCompat.getColorStateList(getContext(), R.color.text_grey_light_more_normal);
    }

    /**
     * 默认重新加载字体大小
     */
    private int getDefaultReloadTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    /**
     * 默认重新加载字体颜色
     */
    private ColorStateList getDefaultReloadTextColor() {
        return ContextCompat.getColorStateList(getContext(), R.color.text_grey_normal);
    }
}
