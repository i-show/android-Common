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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishow.common.R;
import com.ishow.common.utils.UnitUtils;
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
     * 左侧图片信息
     */
    private PromptImageView mLeftImageView;
    private Drawable mLeftImageDrawable;
    private Drawable mLeftImageBackgroundDrawable;
    private int mLeftImageWidth;
    private int mLeftImageHeight;
    private int mLeftImageVisibility;
    private int mLeftImageMarginStart;
    private int mLeftImageMarginEnd;
    /**
     * 左侧文本信息
     */
    private PromptTextView mLeftTextView;
    private String mLeftTextString;
    private int mLeftTextSize;
    private int mLeftTextColor;
    private int mLeftTextVisibility;
    private int mLeftTextMinWidth;
    private int mLeftTextMaxWidth;
    private int mLeftTextMarginStart;
    private int mLeftTextMarginEnd;
    private int mLeftTextGravity;
    private int mLeftTextStyle;
    private Drawable mLeftTextBackgroundDrawable;
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
     * 自定义插入的view
     */
    private View mCustomizeView;

    /**
     * 输入EditText的信息
     */
    private Drawable mTextBackgroundDrawable;
    private int mTextGravity;
    private int mTextEllipsize;
    private int mTextSize;
    private int mTextColor;
    private int mTextLines;
    private int mTextMarginStart;
    private int mTextMarginEnd;
    private String mTextString;

    /**
     * 右侧文本信息
     */
    private String mRightTextString;
    private int mRightTextSize;
    private int mRightTextVisibility;
    private int mRightTextMarginStart;
    private int mRightTextMarginEnd;
    private int mRightTextMinWidth;
    private int mRightTextMaxWidth;
    private int mRightTextGravity;
    private int mRightTextPadding;
    private int mRightTextDrawablePadding;
    private int mRightTextPaddingHorizontal;
    private int mRightTextPaddingVertical;
    private ColorStateList mRightTextColor;
    private Drawable mRightTextBackgroundDrawable;
    private Drawable mRightTextRightDrawable;
    private Drawable mRightTextLeftDrawable;

    /**
     * 右侧图片信息
     */
    private Drawable mRightImageDrawable;
    private Drawable mRightImageBackgroundDrawable;
    private int mRightImageWidth;
    private int mRightImageHeight;
    private int mRightImageMarginStart;
    private int mRightImageMarginEnd;
    private int mRightImageVisibility;

    private Drawable mRightImage2Drawable;
    private Drawable mRightImage2BackgroundDrawable;
    private int mRightImage2Width;
    private int mRightImage2Height;
    private int mRightImage2MarginStart;
    private int mRightImage2MarginEnd;
    private int mRightImage2Visibility;

    private Paint mTopLinePaint;
    private int mTopLineHeight;
    private int mTopLineColor;
    private int mTopLineVisibility;
    private int mTopLinePaddingStart;
    private int mTopLinePaddingEnd;

    private Paint mBottomLinePaint;
    private int mBottomLineHeight;
    private int mBottomLineColor;
    private int mBottomLineVisibility;
    private int mBottomLinePaddingStart;
    private int mBottomLinePaddingEnd;

    /**
     * 自定义插入View
     */
    private int mCustomizeViewId;
    /**
     * 自定义View的位置， -1 为最后
     */
    private int mCustomizeViewIndex;
    private int mCustomizeMarginStart;
    private int mCustomizeMarginEnd;

    /**
     * 图片的建议宽度
     */
    private int mSuggestIconWidth;
    private int mDesireTextWidth;


    private int mMinHeight;

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
        mLeftImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageWidth, -1);
        mLeftImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageHeight, -1);
        mLeftImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftImageBackground);
        mLeftImageVisibility = a.getInt(R.styleable.TextViewPro_leftImageVisibility, View.VISIBLE);
        mLeftImageMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageMarginStart, 0);
        mLeftImageMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_leftImageMarginEnd, 0);

        mLeftTextString = a.getString(R.styleable.TextViewPro_leftText);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextSize, getDefaultTipTextSize());
        mLeftTextColor = a.getColor(R.styleable.TextViewPro_leftTextColor, getDefaultTipTextColor());
        mLeftTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMarginStart, 0);
        mLeftTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMarginEnd, 0);
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMinWidth, getDefaultTipMinWidth());
        mLeftTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_leftTextMaxWidth, getDefaultTipMaxWidth());
        mLeftTextVisibility = a.getInt(R.styleable.TextViewPro_leftTextVisibility, View.VISIBLE);
        mLeftTextGravity = a.getInt(R.styleable.TextViewPro_leftTextGravity, Gravity.CENTER);
        mLeftTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_leftTextBackground);
        mLeftTextStyle = a.getInt(R.styleable.TextViewPro_leftTextStyle, 0);

        mTextString = a.getString(R.styleable.TextViewPro_text);
        mTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_textSize, getDefaultInputTextSize());
        mTextColor = a.getColor(R.styleable.TextViewPro_textColor, getDefaultInputTextColor());
        mTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_textMarginStart, 0);
        mTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_textMarginEnd, 0);
        mTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_textBackground);
        mTextGravity = a.getInt(R.styleable.TextViewPro_textGravity, Gravity.CENTER_VERTICAL);
        mTextEllipsize = a.getInt(R.styleable.TextViewPro_textEllipsize, -1);
        mTextLines = a.getInt(R.styleable.TextViewPro_textLines, 0);

        mRightTextString = a.getString(R.styleable.TextViewPro_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextSize, getDefaultTipTextSize());
        mRightTextColor = a.getColorStateList(R.styleable.TextViewPro_rightTextColor);
        mRightTextVisibility = a.getInt(R.styleable.TextViewPro_rightTextVisibility, View.GONE);
        mRightTextMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMarginStart, 0);
        mRightTextMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMarginEnd, 0);
        mRightTextPadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPadding, 0);
        mRightTextPaddingHorizontal = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPaddingHorizontal, 0);
        mRightTextPaddingVertical = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextPaddingVertical, 0);
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMinWidth, getDefaultTipMinWidth());
        mRightTextMaxWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextMaxWidth, getDefaultTipMaxWidth());
        mRightTextDrawablePadding = a.getDimensionPixelSize(R.styleable.TextViewPro_rightTextDrawablePadding, 5);
        mRightTextLeftDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextLeftDrawable);
        mRightTextRightDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextRightDrawable);
        mRightTextGravity = a.getInt(R.styleable.TextViewPro_rightTextGravity, Gravity.CENTER);
        mRightTextBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightTextBackground);

        mRightImageDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage);
        mRightImageBackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImageBackground);
        mRightImageVisibility = a.getInt(R.styleable.TextViewPro_rightImageVisibility, View.VISIBLE);
        mRightImageWidth = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageWidth, 0);
        mRightImageHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageHeight, 0);
        mRightImageMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageMarginStart, 0);
        mRightImageMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImageMarginEnd, 0);

        mRightImage2Drawable = a.getDrawable(R.styleable.TextViewPro_rightImage2);
        mRightImage2BackgroundDrawable = a.getDrawable(R.styleable.TextViewPro_rightImage2Background);
        mRightImage2Visibility = a.getInt(R.styleable.TextViewPro_rightImage2Visibility, View.GONE);
        mRightImage2Width = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2Width, 0);
        mRightImage2Height = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2Height, 0);
        mRightImage2MarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2MarginStart, 0);
        mRightImage2MarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_rightImage2MarginEnd, 0);

        mTopLineHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_topLineHeight, getDefaultLineHeight());
        mTopLineColor = a.getColor(R.styleable.TextViewPro_topLineColor, getDefaultLineColor());
        mTopLineVisibility = a.getInt(R.styleable.TextViewPro_topLineVisibility, View.GONE);
        mTopLinePaddingStart = a.getDimensionPixelSize(R.styleable.TextViewPro_topLinePaddingStart, 0);
        mTopLinePaddingEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_topLinePaddingEnd, 0);

        mBottomLineHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLineHeight, getDefaultLineHeight());
        mBottomLineColor = a.getColor(R.styleable.TextViewPro_bottomLineColor, getDefaultLineColor());
        mBottomLineVisibility = a.getInt(R.styleable.TextViewPro_bottomLineVisibility, View.VISIBLE);
        mBottomLinePaddingStart = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLinePaddingStart, 0);
        mBottomLinePaddingEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_bottomLinePaddingEnd, 0);

        mCustomizeViewId = a.getResourceId(R.styleable.TextViewPro_customizeViewId, 0);
        mCustomizeViewIndex = a.getInt(R.styleable.TextViewPro_customizeViewIndex, -1);
        mCustomizeMarginStart = a.getDimensionPixelSize(R.styleable.TextViewPro_customizeMarginStart, 0);
        mCustomizeMarginEnd = a.getDimensionPixelSize(R.styleable.TextViewPro_customizeMarginEnd, 0);

        mMinHeight = a.getDimensionPixelSize(R.styleable.TextViewPro_android_minHeight, getDefaultMinHeight());
        a.recycle();

        if (mRightTextColor == null) {
            mRightTextColor = getDefaultTipTextColorStateList();
        }

        initNecessaryData();
        initView();
    }

    private void initNecessaryData() {
        mSuggestIconWidth = UnitUtils.INSTANCE.dip2px(40);
        mTopLinePaint = new Paint();
        mTopLinePaint.setDither(true);
        mTopLinePaint.setAntiAlias(true);
        mTopLinePaint.setColor(mTopLineColor);
        //noinspection SuspiciousNameCombination
        mTopLinePaint.setStrokeWidth(mTopLineHeight);

        mBottomLinePaint = new Paint();
        mBottomLinePaint.setDither(true);
        mBottomLinePaint.setAntiAlias(true);
        mBottomLinePaint.setColor(mBottomLineColor);
        //noinspection SuspiciousNameCombination
        mBottomLinePaint.setStrokeWidth(mBottomLineHeight);
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
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mCustomizeViewId > 0) {
            mCustomizeView = findViewById(mCustomizeViewId);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(width, heightMeasureSpec);

        final int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        final int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        final int imageWidthSpec = MeasureSpec.makeMeasureSpec(mSuggestIconWidth, MeasureSpec.EXACTLY);
        final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        int inputWidth = width - getPaddingLeft() - getPaddingRight();

        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            if (mLeftImageWidth > 0 && mLeftImageHeight > 0) {
                mLeftImageView.measure(getExactlyMeasureSpec(mLeftImageWidth), getExactlyMeasureSpec(mLeftImageHeight));
            } else if (mLeftImageWidth > 0) {
                mLeftImageView.measure(getExactlyMeasureSpec(mLeftImageWidth), heightSpec);
            } else if (mLeftImageHeight > 0) {
                mLeftImageView.measure(imageWidthSpec, getExactlyMeasureSpec(mLeftImageHeight));
            } else {
                mLeftImageView.measure(imageWidthSpec, heightSpec);
            }
            inputWidth = inputWidth - mLeftImageView.getMeasuredWidth() - mLeftImageMarginStart - mLeftImageMarginEnd;
        }

        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            mLeftTextView.measure(unspecified, heightSpec);
            inputWidth = inputWidth - mLeftTextView.getMeasuredWidth() - mLeftTextMarginStart - mLeftTextMarginEnd;
        }

        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            if (mRightImageWidth > 0 && mRightImageHeight > 0) {
                mRightImageView.measure(getExactlyMeasureSpec(mRightImageWidth), getExactlyMeasureSpec(mRightImageHeight));
            } else if (mRightImageWidth > 0) {
                mRightImageView.measure(getExactlyMeasureSpec(mRightImageWidth), heightSpec);
            } else if (mRightImageHeight > 0) {
                mRightImageView.measure(imageWidthSpec, getExactlyMeasureSpec(mRightImageHeight));
            } else {
                mRightImageView.measure(imageWidthSpec, heightSpec);
            }

            inputWidth = inputWidth - mRightImageView.getMeasuredWidth() - mRightImageMarginStart - mRightImageMarginEnd;
        }

        if (mRightImageView2 != null && mRightImageView2.getVisibility() != View.GONE) {
            if (mRightImage2Width > 0 && mRightImage2Height > 0) {
                mRightImageView2.measure(
                        MeasureSpec.makeMeasureSpec(mRightImage2Width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(mRightImage2Height, MeasureSpec.EXACTLY));
            } else {
                mRightImageView2.measure(imageWidthSpec, heightSpec);
            }

            if (mRightImage2Width > 0 && mRightImage2Height > 0) {
                mRightImageView2.measure(getExactlyMeasureSpec(mRightImage2Width), getExactlyMeasureSpec(mRightImage2Height));
            } else if (mRightImage2Width > 0) {
                mRightImageView2.measure(getExactlyMeasureSpec(mRightImage2Width), heightSpec);
            } else if (mRightImage2Height > 0) {
                mRightImageView2.measure(imageWidthSpec, getExactlyMeasureSpec(mRightImage2Height));
            } else {
                mRightImageView2.measure(imageWidthSpec, heightSpec);
            }

            inputWidth = inputWidth - mRightImageView2.getMeasuredWidth() - mRightImage2MarginStart - mRightImage2MarginEnd;
        }


        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            mRightTextView.measure(unspecified, unspecified);
            inputWidth = inputWidth - mRightTextView.getMeasuredWidth() - mRightTextMarginStart - mRightTextMarginEnd;
        }

        if (mCustomizeView != null && mCustomizeView.getVisibility() != View.GONE) {
            measureChild(mCustomizeView, widthSpec, heightSpec);
            inputWidth = inputWidth - mCustomizeView.getMeasuredWidth() - mCustomizeMarginStart - mCustomizeMarginEnd;
        }

        inputWidth = inputWidth - mTextMarginStart - mTextMarginEnd;
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
                int height = mMinHeight;

                int inputWidth = width - paddingStart - paddingEnd;

                if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
                    if (mLeftImageWidth > 0) {
                        inputWidth = inputWidth - mLeftImageWidth - mLeftImageMarginStart - mLeftImageMarginEnd;
                    } else {
                        inputWidth = inputWidth - mSuggestIconWidth - mLeftImageMarginStart - mLeftImageMarginEnd;
                    }
                }

                if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
                    final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    mLeftTextView.measure(unspecified, unspecified);
                    final int leftTextHeight = mLeftTextView.getMeasuredHeight();
                    final int leftTextWidth = mLeftTextView.getMeasuredWidth();
                    inputWidth = inputWidth - leftTextWidth - mLeftTextMarginStart - mLeftTextMarginEnd;
                    height = Math.max(height, leftTextHeight);
                }

                if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
                    final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    mRightTextView.measure(unspecified, unspecified);
                    final int rightTextHeight = mRightTextView.getMeasuredHeight();
                    final int rightTextWidth = mRightTextView.getMeasuredWidth();
                    inputWidth = inputWidth - rightTextWidth - mRightTextMarginStart - mRightTextMarginEnd;
                    height = Math.max(height, rightTextHeight);
                }

                if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
                    if (mRightImageWidth > 0) {
                        inputWidth = inputWidth - mRightImageWidth - mRightImageMarginStart - mRightImageMarginEnd;
                    } else {
                        inputWidth = inputWidth - mSuggestIconWidth - mRightImageMarginStart - mRightImageMarginEnd;
                    }
                }

                if (mRightImageView2 != null && mRightImageView2.getVisibility() != View.GONE) {
                    if (mRightImage2Width > 0) {
                        inputWidth = inputWidth - mRightImage2Width - mRightImage2MarginStart - mRightImage2MarginEnd;
                    } else {
                        inputWidth = inputWidth - mSuggestIconWidth - mRightImage2MarginStart - mRightImage2MarginEnd;
                    }
                }

                inputWidth = inputWidth - mTextMarginStart - mTextMarginEnd;

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
        int index = 0;
        int lastLeft = left;
        left = layoutCustomize(left, index);
        index = lastLeft == left ? index : index + 1;


        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            int width = mLeftImageView.getMeasuredWidth();
            int height = mLeftImageView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            left = left + mLeftImageMarginStart;
            mLeftImageView.layout(left, _top, left + width, _top + height);
            left = left + width + mLeftImageMarginEnd;
            index++;
        }
        lastLeft = left;
        left = layoutCustomize(left, index);
        index = lastLeft == left ? index : index + 1;

        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            int width = mLeftTextView.getMeasuredWidth();
            left = left + mLeftTextMarginStart;
            mLeftTextView.layout(left, top, left + width, bottom);
            left = left + width + mLeftTextMarginEnd;
            index++;
        }

        lastLeft = left;
        left = layoutCustomize(left, index);
        index = lastLeft == left ? index : index + 1;

        left = left + mTextMarginStart;
        int inputWidth = mTextView.getMeasuredWidth();
        int inputHeight = mTextView.getMeasuredHeight();
        int g = mTextView.getGravity();
        mTextView.layout(left, top, left + inputWidth, bottom);
        left = left + inputWidth + mTextMarginEnd;
        index++;

        lastLeft = left;
        left = layoutCustomize(left, index);
        index = lastLeft == left ? index : index + 1;

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            int width = mRightTextView.getMeasuredWidth();
            int height = mRightTextView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            left = left + mRightTextMarginStart;
            mRightTextView.layout(left, _top, left + width, _top + height);
            left = left + width + mRightTextMarginEnd;
            index++;
        }

        lastLeft = left;
        left = layoutCustomize(left, index);
        index = lastLeft == left ? index : index + 1;

        if (mRightImageView2 != null && mRightImageView2.getVisibility() != View.GONE) {
            int width = mRightImageView2.getMeasuredWidth();
            int height = mRightImageView2.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            left = left + mRightImage2MarginStart;
            mRightImageView2.layout(left, _top, left + width, _top + height);
            left = left + width + mRightImage2MarginEnd;
            index++;
        }

        // 倒数第二个不需要进行处理index了， 因为最后一个的index 肯定为-1
        left = layoutCustomize(left, index);

        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            int width = mRightImageView.getMeasuredWidth();
            int height = mRightImageView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            left = left + mRightImageMarginStart;
            mRightImageView.layout(left, _top, left + width, _top + height);
        }

        index = -1;
        layoutCustomize(left, index);
    }

    /**
     * 设置自定义View的位置
     */
    private int layoutCustomize(int left, int index) {
        if (index != mCustomizeViewIndex || mCustomizeView == null || mCustomizeView.getVisibility() == View.GONE) {
            return left;
        }
        left = left + mCustomizeMarginStart;
        int width = mCustomizeView.getMeasuredWidth();
        int height = mCustomizeView.getMeasuredHeight();
        int _top = (getMeasuredHeight() - height) / 2;
        mCustomizeView.layout(left, _top, left + width, _top + height);
        left = left + width + mCustomizeMarginEnd;
        return left;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (mTopLineVisibility == VISIBLE) {
            //noinspection SuspiciousNameCombination
            canvas.drawLine(mTopLinePaddingStart, mTopLineHeight, width - mTopLinePaddingEnd, mTopLineHeight, mTopLinePaint);
        }

        if (mBottomLineVisibility == VISIBLE) {
            //noinspection SuspiciousNameCombination
            canvas.drawLine(mBottomLinePaddingStart, height - mBottomLineHeight, width - mBottomLinePaddingEnd, height - mBottomLineHeight, mBottomLinePaint);
        }
    }


    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getLeftImageView() {
        if (mLeftImageVisibility == View.GONE) {
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
            mLeftTextView.setTypeface(Typeface.defaultFromStyle(mLeftTextStyle));
            mLeftTextView.setIncludeFontPadding(false);
            setDefaultPromptState(mLeftTextView);
            addView(mLeftTextView);
        }
        return mLeftTextView;
    }


    @SuppressWarnings("UnusedReturnValue")
    public TextView getTextView() {
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
            mRightTextView.setIncludeFontPadding(false);
            if (mRightTextPadding > 0) {
                //noinspection SuspiciousNameCombination
                mRightTextView.setPadding(mRightTextPadding, mRightTextPadding, mRightTextPadding, mRightTextPadding);
            } else if (mRightTextPaddingVertical > 0 || mRightTextPaddingHorizontal > 0) {
                //noinspection SuspiciousNameCombination
                mRightTextView.setPadding(mRightTextPaddingHorizontal, mRightTextPaddingVertical, mRightTextPaddingHorizontal, mRightTextPaddingVertical);
            }

            if (mRightTextRightDrawable != null || mRightTextLeftDrawable != null) {
                if (mTintColor != null && mRightTextRightDrawable != null) {
                    mRightTextRightDrawable = DrawableCompat.wrap(mRightTextRightDrawable);
                    DrawableCompat.setTintList(mRightTextRightDrawable, mTintColor);
                }

                if (mTintColor != null && mRightTextLeftDrawable != null) {
                    mRightTextLeftDrawable = DrawableCompat.wrap(mRightTextLeftDrawable);
                    DrawableCompat.setTintList(mRightTextLeftDrawable, mTintColor);
                }

                mRightTextView.setCompoundDrawablesWithIntrinsicBounds(mRightTextLeftDrawable, null, mRightTextRightDrawable, null);
                mRightTextView.setCompoundDrawablePadding(mRightTextDrawablePadding);
            }
            setDefaultPromptState(mRightTextView);
            addView(mRightTextView);
        }
        return mRightTextView;
    }

    @SuppressWarnings("UnusedReturnValue")
    public PromptImageView getRightImageView() {
        if (mRightImageVisibility == View.GONE) {
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
        if (mRightImage2Visibility == View.GONE) {
            return null;
        }

        if (mRightImageView2 == null) {

            if (mTintColor != null && mRightImage2Drawable != null) {
                mRightImage2Drawable = DrawableCompat.wrap(mRightImage2Drawable);
                DrawableCompat.setTintList(mRightImage2Drawable, mTintColor);
            }

            mRightImageView2 = new PromptImageView(getContext());
            mRightImageView2.setId(R.id.rightImage2);
            mRightImageView2.setVisibility(mRightImage2Visibility);
            if (mRightImage2Drawable != null) {
                mRightImageView2.setImageDrawable(mRightImageDrawable);
            }
            mRightImageView2.setBackground(mRightImage2BackgroundDrawable);
            mRightImageView2.setScaleType(ImageView.ScaleType.CENTER);
            setDefaultPromptState(mRightImageView2);
            addView(mRightImageView2);
        }
        return mRightImageView2;
    }

    private void setDefaultPromptState(IPrompt prompt) {
        if (prompt == null) {
            return;
        }

        prompt.setPromptMode(IPrompt.PromptMode.NONE);
        prompt.commit();
    }


    private int getDefaultTipTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    private int getDefaultTipMinWidth() {
        return UnitUtils.INSTANCE.dip2px(50);
    }

    private int getDefaultTipMaxWidth() {
        return UnitUtils.INSTANCE.dip2px(120);
    }

    private int getDefaultTipTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_light_normal);
    }

    private ColorStateList getDefaultTipTextColorStateList() {
        return ContextCompat.getColorStateList(getContext(), R.color.text_grey_light_normal);
    }

    private int getDefaultInputTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    private int getDefaultInputTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_normal);
    }

    private int getDefaultLineColor() {
        return getContext().getResources().getColor(R.color.line);
    }

    /**
     * 线的高度
     */
    private int getDefaultLineHeight() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.default_line_height);
    }

    /**
     * 获取默认的最小高度
     */
    private int getDefaultMinHeight() {
        return getResources().getDimensionPixelSize(R.dimen.default_pro_height);
    }

    public void setLeftImageVisibility(int visibility) {
        if (mLeftImageView != null) {
            mLeftImageView.setVisibility(visibility);
            mLeftImageVisibility = visibility;
        }
    }

    public void setLeftImageSelected(boolean selected) {
        if (mLeftImageView != null) {
            mLeftImageView.setSelected(selected);
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

    public void setTextMovementMethod(MovementMethod movement) {
        mTextView.setMovementMethod(movement);
    }

    public void setTextHighlightColor(int color) {
        mTextView.setHighlightColor(color);
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

    public void setRightTextSelected(boolean selected) {
        if (mRightTextView != null) {
            mRightTextView.setSelected(selected);
        }
    }

    public void setRightTextBackgroundResources(@DrawableRes int backgroundResources) {
        if (mRightTextView != null) {
            mRightTextView.setBackgroundResource(backgroundResources);
        }
    }

    public void setRightImageResource2(@DrawableRes int resid) {
        if (mRightImageView2 != null) {
            mRightImageView2.setImageResource(resid);
        }
        mRightImage2Visibility = View.VISIBLE;
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

        mRightImage2Visibility = View.VISIBLE;
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

    /**
     * 设置右侧按钮是否可见
     */
    public void setRightTextVisibility(int visibility) {
        if (mRightTextView != null) {
            mRightTextView.setVisibility(visibility);
            mRightTextVisibility = visibility;
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

    /**
     * 获取精确的MeasureSpec
     */
    private int getExactlyMeasureSpec(int size) {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
    }
}
