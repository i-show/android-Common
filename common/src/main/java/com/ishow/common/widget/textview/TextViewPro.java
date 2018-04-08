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

package com.ishow.common.widget.textview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishow.common.R;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.common.widget.imageview.PromptImageView;
import com.ishow.common.widget.prompt.IPrompt;

/**
 * Created by Bright.Yu on 2017/2/9.
 * 加强版本的TextView
 */
@SuppressWarnings("unused")
public class TextViewPro extends ViewGroup {
    private static final String TAG = "TextViewPro";

    /**
     * 左边的View
     */
    private PromptImageView mLeftImageView;
    private PromptTextView mLeftTextView;
    /**
     * 中间的View
     */
    private TextView mTextView;
    /**
     * 右边的View
     */
    private PromptImageView mRightImageView;
    private PromptImageView mRightImageView2;
    private PromptTextView mRightTextView;

    /**
     * 左侧图片信息
     */
    private Drawable mLeftImageDrawable;
    private Drawable mLeftImageBackgroundDrawable;
    private int mLeftImageVisibility;
    private int mLeftImageRightMargin;

    /**
     * 左侧文本信息
     */
    private String mLeftTextString;
    private int mLeftTextSize;
    private int mLeftTextColor;
    private int mLeftTextVisibility;
    private int mLeftTextMinWidth;
    private int mLeftTextMaxWidth;
    private int mLeftTextRightMargin;
    private int mLeftTextGravity;
    private Drawable mLeftTextBackgroundDrawable;

    /**
     * 输入EditText的信息
     */
    private Drawable mTextBackgroundDrawable;
    private int mTextGravity;
    private int mTextEllipsize;
    private int mTextSize;
    private int mTextColor;
    private int mTextLines;
    private int mTextRightMargin;
    private String mTextString;

    /**
     * 右侧文本信息
     */
    private String mRightTextString;
    private int mRightTextSize;
    private int mRightTextColor;
    private int mRightTextVisibility;
    private int mRightTextRightMargin;
    private int mRightTextMinWidth;
    private int mRightTextMaxWidth;
    private int mRightTextGravity;
    private int mRightTextPadding;
    private Drawable mRightTextBackgroundDrawable;
    private Drawable mRightTextRightDrawable;

    /**
     * 右侧图片信息
     */
    private Drawable mRightImageDrawable;
    private Drawable mRightImageBackgroundDrawable;
    private int mRightImageWidth;
    private int mRightImageHeight;
    private int mRightImageVisibility;

    private Drawable mRightImageDrawable2;
    private Drawable mRightImageBackgroundDrawable2;
    private int mRightImageWidth2;
    private int mRightImageHeight2;
    private int mRightImageVisibility2;
    /**
     * 图片的建议宽度
     */
    private int mSuggestIconWidth;
    private int mDesireTextWidth;


    private int mMinHegiht;

    private ColorStateList mTintColor;

    public TextViewPro(Context context) {
        this(context, null);
    }

    public TextViewPro(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewPro(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPro);

        mTintColor = a.getColorStateList(R.styleable.TextViewPro_tintColor);

        mLeftImageDrawable = a.getDrawable(R.styleable.TextViewPro_leftImage);
        mLeftImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftImageBackground);
        mLeftImageVisibility = a.getInt(R.styleable.TextViewPro_leftImageVisibility, View.VISIBLE);
        mLeftImageRightMargin = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageRightMargin, 0);

        mLeftTextString = a.getString(R.styleable.TextViewPro_leftText);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextSize, getDefaultTipTextSize());
        mLeftTextColor = a.getColor(R.styleable.TextViewPro_leftTextColor, getDefaultTipTextColor());
        mLeftTextRightMargin = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextRightMargin, 0);
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMinWidth, getDefaultTipMinWidth());
        mLeftTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMaxWidth, getDefaultTipMaxWidth());
        mLeftTextVisibility = a.getInt(R.styleable.TextViewPro_leftTextVisibility, View.VISIBLE);
        mLeftTextGravity = a.getInt(R.styleable.TextViewPro_leftTextGravity, Gravity.CENTER);
        mLeftTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftTextBackground);

        mTextString = a.getString(R.styleable.TextViewPro_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_textSize, getDefaultInputTextSize());
        mTextColor = a.getColor(R.styleable.TextViewPro_textColor, getDefaultInputTextColor());
        mTextRightMargin = a.getDimensionPixelSize(R.styleable.TextViewPro_textRightMargin, 0);
        mTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_textBackground);
        mTextGravity = a.getInt(R.styleable.TextViewPro_textGravity, Gravity.CENTER_VERTICAL);
        mTextEllipsize = a.getInt(R.styleable.TextViewPro_textEllipsize, -1);
        mTextLines = a.getInt(R.styleable.TextViewPro_textLines, 0);

        mRightTextString = a.getString(R.styleable.TextViewPro_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextSize, getDefaultTipTextSize());
        mRightTextColor = a.getColor(R.styleable.TextViewPro_rightTextColor, getDefaultTipTextColor());
        mRightTextVisibility = a.getColor(R.styleable.TextViewPro_rightTextVisibility, View.GONE);
        mRightTextRightMargin = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextRightMargin, 0);
        mRightTextPadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPadding, 0);
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMinWidth, getDefaultTipMinWidth());
        mRightTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMaxWidth, getDefaultTipMaxWidth());
        mRightTextRightDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextRightDrawable);
        mRightTextGravity = a.getInt(R.styleable.TextViewPro_rightTextGravity, Gravity.CENTER);
        mRightTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextBackground);

        mRightImageDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage);
        mRightImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImageBackground);
        mRightImageVisibility = a.getInt(R.styleable.TextViewPro_rightImageVisibility, View.VISIBLE);
        mRightImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageWidth, 0);
        mRightImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageHeight, 0);

        mRightImageDrawable2 = a.getDrawable(R.styleable.TextViewPro_rightImage2);
        mRightImageBackgroundDrawable2 = a.getDrawable(R.styleable.TextViewPro_rightImageBackground2);
        mRightImageVisibility2 = a.getInt(R.styleable.TextViewPro_rightImageVisibility2, View.VISIBLE);
        mRightImageWidth2 = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageWidth2, 0);
        mRightImageHeight2 = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageHeight2, 0);

        mMinHegiht = a.getDimensionPixelSize(R.styleable.TextViewPro_android_minHeight, getDefaultMinHeight());
        a.recycle();

        initNecessaryData();
        initView();
    }

    private void initNecessaryData() {
        mSuggestIconWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_40);
    }

    private void initView() {
        getLeftImageView();
        getLeftTextView();
        getTextView();
        getRightTextView();
        getRightImageView();
        getRightImageView2();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(width, heightMeasureSpec);

        final int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        final int imageWidthSpec = MeasureSpec.makeMeasureSpec(mSuggestIconWidth, MeasureSpec.EXACTLY);
        final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int inputWidth = width - getPaddingLeft() - getPaddingRight();

        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            mLeftImageView.measure(imageWidthSpec, heightSpec);
            inputWidth = inputWidth - mLeftImageView.getMeasuredWidth() - mLeftImageRightMargin;
        }

        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            mLeftTextView.measure(unspecified, heightSpec);
            inputWidth = inputWidth - mLeftTextView.getMeasuredWidth() - mLeftTextRightMargin;
        }

        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            if (mRightImageWidth > 0 && mRightImageHeight > 0) {
                mRightImageView.measure(
                        MeasureSpec.makeMeasureSpec(mRightImageWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mRightImageHeight, MeasureSpec.EXACTLY));
            } else {
                mRightImageView.measure(imageWidthSpec, heightSpec);
            }

            inputWidth = inputWidth - mRightImageView.getMeasuredWidth();
        }

        if (mRightImageView2 != null && mRightImageView2.getVisibility() != View.GONE) {
            if (mRightImageWidth2 > 0 && mRightImageHeight2 > 0) {
                mRightImageView2.measure(
                        MeasureSpec.makeMeasureSpec(mRightImageWidth2, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mRightImageHeight2, MeasureSpec.EXACTLY));
            } else {
                mRightImageView2.measure(imageWidthSpec, heightSpec);
            }

            inputWidth = inputWidth - mRightImageView2.getMeasuredWidth();
        }


        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            mRightTextView.measure(unspecified, unspecified);
            inputWidth = inputWidth - mRightTextView.getMeasuredWidth() - mRightTextRightMargin;
        }

        inputWidth = inputWidth - mTextRightMargin;
        mTextView.measure(MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY), heightSpec);

        height = height + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int size = MeasureSpec.getSize(widthMeasureSpec);
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                return size;
        }
        return size;
    }

    private int measureHeight(final int width, int heightMeasureSpec) {
        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int size = MeasureSpec.getSize(heightMeasureSpec);
        switch (mode) {
            case MeasureSpec.EXACTLY:
                return size;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                final int paddingStart = getPaddingStart();
                final int paddingEnd = getPaddingEnd();
                int height = mMinHegiht;

                int inputWidth = width - paddingStart - paddingEnd;

                if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
                    inputWidth = inputWidth - mSuggestIconWidth - mLeftImageRightMargin;
                }

                if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
                    final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    mLeftTextView.measure(unspecified, unspecified);
                    final int leftTextHeight = mLeftTextView.getMeasuredHeight();
                    final int leftTextWidth = mLeftTextView.getMeasuredWidth();
                    inputWidth = inputWidth - leftTextWidth - mLeftTextRightMargin;
                    height = Math.max(height, leftTextHeight);
                }

                if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
                    final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    mRightTextView.measure(unspecified, unspecified);
                    final int rightTextHeight = mRightTextView.getMeasuredHeight();
                    final int rightTextWidth = mRightTextView.getMeasuredWidth();
                    inputWidth = inputWidth - rightTextWidth - mRightTextRightMargin;
                    height = Math.max(height, rightTextHeight);
                }

                if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
                    inputWidth = inputWidth - mSuggestIconWidth;
                }

                if (mRightImageView2 != null && mRightImageView2.getVisibility() != View.GONE) {
                    inputWidth = inputWidth - mSuggestIconWidth;
                }

                inputWidth = inputWidth - mTextRightMargin;

                final int widthSpec = MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY);
                final int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                mTextView.measure(widthSpec, heightSpec);
                final int inputHeight = mTextView.getMeasuredHeight();
                height = Math.max(height, inputHeight);
                return height;
        }
        return size;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingStart();
        int top = getPaddingTop();
        int right = r - l - getPaddingEnd();
        int bottom = b - t - getPaddingBottom();

        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            int height = mLeftImageView.getMeasuredHeight();
            int height2 = getMeasuredHeight();

            mLeftImageView.layout(left, top, left + mSuggestIconWidth, bottom);
            left = left + mSuggestIconWidth + mLeftImageRightMargin;
        }

        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            int width = mLeftTextView.getMeasuredWidth();
            mLeftTextView.layout(left, top, left + width, bottom);
            left = left + width + mLeftTextRightMargin;
        }

        int inputWidth = mTextView.getMeasuredWidth();
        int inputHeight = mTextView.getMeasuredHeight();
        int g = mTextView.getGravity();
        mTextView.layout(left, top, left + inputWidth, bottom);
        left = left + inputWidth;

        left = left + mTextRightMargin;

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            int width = mRightTextView.getMeasuredWidth();
            int height = mRightTextView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            mRightTextView.layout(left, _top, left + width, _top + height);
            left = left + width + mRightTextRightMargin;
        }

        if (mRightImageView2 != null && mRightImageView2.getVisibility() != View.GONE) {
            int width = mRightImageView2.getMeasuredWidth();
            int height = mRightImageView2.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            mRightImageView2.layout(left, _top, left + width, _top + height);
            left = left + width;
        }


        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            int width = mRightImageView.getMeasuredWidth();
            int height = mRightImageView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            mRightImageView.layout(left, _top, left + width, _top + height);
        }

    }


    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getLeftImageView() {
        if (mLeftImageVisibility == View.GONE) {
            Log.i(TAG, "getLeftImageView: is visiable gone just not add");
            return null;
        }

        if (mLeftImageView == null) {
            if (mTintColor != null) {
                mLeftImageDrawable = DrawableCompat.wrap(mLeftImageDrawable);
                DrawableCompat.setTintList(mLeftImageDrawable, mTintColor);
            }

            mLeftImageView = new PromptImageView(getContext());
            mLeftImageView.setId(R.id.leftImage);
            mLeftImageView.setVisibility(VISIBLE);
            mLeftImageView.setImageDrawable(mLeftImageDrawable);
            mLeftImageView.setBackground(mLeftImageBackgroundDrawable);
            mLeftImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            setDefaultPromptState(mLeftImageView);
            addView(mLeftImageView);
        }
        return mLeftImageView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptTextView getLeftTextView() {
        if (mLeftTextVisibility == View.GONE) {
            Log.i(TAG, "getLeftTextView: is visiable gone just not add");
            return null;
        }

        if (mLeftTextView == null) {
            mLeftTextView = new PromptTextView(getContext());
            mLeftTextView.setId(R.id.leftText);
            mLeftTextView.setText(mLeftTextString);
            mLeftTextView.setTextColor(mLeftTextColor);
            mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
            mLeftTextView.setMinWidth(mLeftTextMinWidth);
            mLeftTextView.setMaxWidth(mLeftTextMaxWidth);
            mLeftTextView.setBackground(mLeftTextBackgroundDrawable);
            mLeftTextView.setGravity(mLeftTextGravity);
            setDefaultPromptState(mLeftTextView);
            addView(mLeftTextView);
        }
        return mLeftTextView;
    }


    @SuppressWarnings("UnusedReturnValue")
    private TextView getTextView() {
        if (mTextView == null) {
            mTextView = new AppCompatTextView(getContext());
            setInputEllipsize();
            mTextView.setText(mTextString);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            mTextView.setTextColor(mTextColor);
            mTextView.setBackground(mTextBackgroundDrawable);
            mTextView.setGravity(mTextGravity);
            if (mTextLines > 0) {
                mTextView.setLines(mTextLines);
            }
            addView(mTextView);
        }

        return mTextView;
    }


    @SuppressWarnings("UnusedReturnValue")
    public PromptTextView getRightTextView() {
        if (mRightTextVisibility == View.GONE) {
            Log.i(TAG, "getLeftTextView: is visiable gone just not add");
            return null;
        }

        if (mRightTextView == null) {
            mRightTextView = new PromptTextView(getContext());
            mRightTextView.setId(R.id.rightText);
            mRightTextView.setText(mRightTextString);
            mRightTextView.setGravity(Gravity.CENTER);
            mRightTextView.setTextColor(mRightTextColor);
            mRightTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
            mRightTextView.setMinWidth(mRightTextMinWidth);
            mRightTextView.setMaxWidth(mRightTextMaxWidth);
            mRightTextView.setBackground(mRightTextBackgroundDrawable);
            mRightTextView.setGravity(mRightTextGravity);
            if (mRightTextPadding > 0) {
                //noinspection SuspiciousNameCombination
                mRightTextView.setPadding(mRightTextPadding, mRightTextPadding, mRightTextPadding, mRightTextPadding);
            }
            if (mRightTextRightDrawable != null) {
                if (mTintColor != null) {
                    mRightTextRightDrawable = DrawableCompat.wrap(mRightTextRightDrawable);
                    DrawableCompat.setTintList(mRightTextRightDrawable, mTintColor);
                }

                mRightTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, mRightTextRightDrawable, null);
            }
            setDefaultPromptState(mRightTextView);
            addView(mRightTextView);
        }
        return mRightTextView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getRightImageView() {
        if (mRightImageVisibility == View.GONE) {
            Log.i(TAG, "getLeftImageView: is visiable gone just not add");
            return null;
        }

        if (mRightImageView == null) {

            if (mTintColor != null && mRightImageDrawable != null) {
                mRightImageDrawable = DrawableCompat.wrap(mRightImageDrawable);
                DrawableCompat.setTintList(mRightImageDrawable, mTintColor);
            }

            mRightImageView = new PromptImageView(getContext());
            mRightImageView.setId(R.id.rightImage);
            mRightImageView.setVisibility(mRightImageVisibility);
            if (mRightImageDrawable == null) {
                mRightImageView.setImageResource(R.drawable.ic_arrow_right);
            } else {
                mRightImageView.setImageDrawable(mRightImageDrawable);

            }
            mRightImageView.setBackground(mRightImageBackgroundDrawable);
            mRightImageView.setScaleType(ImageView.ScaleType.CENTER);
            setDefaultPromptState(mRightImageView);
            addView(mRightImageView);
        }
        return mRightImageView;
    }


    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getRightImageView2() {
        if (mRightImageVisibility2 == View.GONE) {
            Log.i(TAG, "getLeftImageView: is visiable gone just not add");
            return null;
        }

        if (mRightImageView2 == null) {

            if (mTintColor != null && mRightImageDrawable2 != null) {
                mRightImageDrawable2 = DrawableCompat.wrap(mRightImageDrawable2);
                DrawableCompat.setTintList(mRightImageDrawable2, mTintColor);
            }

            mRightImageView2 = new PromptImageView(getContext());
            mRightImageView2.setId(R.id.rightImage2);
            mRightImageView2.setVisibility(mRightImageVisibility2);
            if (mRightImageDrawable2 != null) {
                mRightImageView2.setImageDrawable(mRightImageDrawable);
            }
            mRightImageView2.setBackground(mRightImageBackgroundDrawable2);
            mRightImageView2.setScaleType(ImageView.ScaleType.CENTER);
            setDefaultPromptState(mRightImageView2);
            addView(mRightImageView2);
        }
        return mRightImageView2;
    }

    private void setDefaultPromptState(IPrompt prompt) {
        if (prompt == null) {
            Log.i(TAG, "setDefaultPromptState: ");
            return;
        }

        prompt.setPromptMode(IPrompt.PromptMode.NONE);
        prompt.commit();
    }


    private int getDefaultTipTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    private int getDefaultTipMinWidth() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.dp_50);
    }

    private int getDefaultTipMaxWidth() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.dp_120);
    }

    @SuppressWarnings("deprecation")
    private int getDefaultTipTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_light_normal);
    }

    private int getDefaultInputTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    @SuppressWarnings("deprecation")
    private int getDefaultInputTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_normal);
    }


    /**
     * 获取默认的最小高度
     */
    private int getDefaultMinHeight() {
        return getResources().getDimensionPixelSize(R.dimen.girdle_min_h);
    }

    public void setLeftImageVisibility(int visibility) {
        if (mLeftImageView != null) {
            mLeftImageView.setVisibility(visibility);
            mLeftImageVisibility = visibility;
        }
    }

    public void setLeftText(@StringRes int textRes) {
        String text = getContext().getString(textRes);
        setLeftText(text);
    }

    public void setLeftText(String text) {
        mLeftTextString = text;
        if (mLeftTextView != null) {
            mLeftTextView.setText(text);
        }
    }

    public void setLeftTextGravity(int gravity) {
        if (mLeftTextView != null) {
            mLeftTextView.setGravity(gravity);
        }
    }

    public void setLeftTextMinWidth(final int width) {
        if (mLeftTextView != null) {
            mLeftTextView.setMinWidth(width);
            requestLayout();
        }
    }

    public String getText() {
        return mTextView.getText().toString().trim();
    }

    public void setText(@StringRes int textRes) {
        String text = getContext().getString(textRes);
        setText(text);
    }

    public void setText(CharSequence text) {
        mTextView.setText(text);
        requestLayout();
    }

    public void setTextSize(@DimenRes int resId) {
        float size = getResources().getDimensionPixelSize(resId);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        requestLayout();
    }

    public void setTextColor(@ColorInt int colorInt) {
        mTextView.setTextColor(colorInt);
    }


    public void setRightText(@StringRes int textRes) {
        String text = getContext().getString(textRes);
        setRightText(text);
    }

    public void setRightText(String text) {
        mRightTextString = text;
        if (mRightTextView != null) {
            mRightTextView.setText(text);
        }
        mRightTextVisibility = View.VISIBLE;
        requestLayout();
    }


    public void setRightImageResource2(@DrawableRes int resid) {
        if (mRightImageView2 != null) {
            mRightImageView2.setImageResource(resid);
        }
        mRightImageVisibility2 = View.VISIBLE;
        requestLayout();
    }

    public void setRightImageUrl2(String url) {
        setRightImageUrl2(url, ImageLoader.LoaderMode.CENTER_CROP);
    }

    public void setRightImageUrl2(String url, @ImageLoader.LoaderMode int loaderMode) {
        if (mRightImageView2 == null) {
            getRightImageView2();
        }

        ImageLoader.with(getContext())
                .load(url)
                .mode(loaderMode)
                .into(mRightImageView2);

        mRightImageVisibility2 = View.VISIBLE;
        requestLayout();
    }

    public void setRightImageResource(@DrawableRes int resid) {
        if (mRightImageView != null) {
            mRightImageView.setImageResource(resid);
        }
        mRightImageVisibility = View.VISIBLE;
        requestLayout();
    }

    public void setRightImageVisibility(int visibility) {
        if (mRightImageView != null) {
            mRightImageView.setVisibility(visibility);
            mRightImageVisibility = visibility;
        }
    }

    public void setRightImageClickListener(OnClickListener listener) {
        if (mRightImageView != null) {
            mRightImageView.setOnClickListener(listener);
        }
        requestLayout();
    }

    public void setRightTextClickListener(OnClickListener listener) {

        if (mRightTextView != null) {
            mRightTextView.setOnClickListener(listener);
        }
    }


    private void setInputEllipsize() {
        switch (mTextEllipsize) {
            case 1:
                mTextView.setEllipsize(TextUtils.TruncateAt.START);
                break;
            case 2:
                mTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case 3:
                mTextView.setEllipsize(TextUtils.TruncateAt.END);
                break;
        }
    }


}
