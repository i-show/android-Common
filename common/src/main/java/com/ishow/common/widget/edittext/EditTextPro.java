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

package com.ishow.common.widget.edittext;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.ishow.common.R;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.widget.imageview.PromptImageView;
import com.ishow.common.widget.prompt.IPrompt;
import com.ishow.common.widget.textview.PromptTextView;

/**
 * Created by Bright.Yu on 2017/2/9.
 * 加强版本的EditText
 */
@SuppressWarnings("unused")
public class EditTextPro extends ViewGroup implements View.OnFocusChangeListener, View.OnClickListener {
    private static final String TAG = "EditTextPro";
    /**
     * 左边的View
     */
    private PromptImageView mLeftImageView;
    private PromptTextView mLeftTextView;
    /**
     * 中间的View
     */
    private AppCompatEditText mInputView;
    private ImageView mCancelView;
    /**
     * 右边的View
     */
    private PromptImageView mRightImageView;
    private PromptTextView mRightTextView;
    /**
     * 自定义插入的view
     */
    private View mCustomizeView;
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
    private int mLeftTextStyle;
    private Drawable mLeftTextBackgroundDrawable;

    /**
     * 输入EditText的信息
     */
    private Drawable mInputBackgroundDrawable;
    private int mInputGravity;
    private int mInputTextSize;
    private int mInputTextColor;
    private int mInputHintTextColor;
    private int mInputLines;
    private int mInputMaxLength;
    private int mInputRightMargin;
    private int mInputType;
    private String mInputTextString;
    private String mInputHintString;
    private String mInputDigitsString;
    private int mCancelVisibility;
    private boolean isCancelEnable;

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
    private int mRightImageVisibility;
    /**
     * 顶部的线
     */
    private Paint mTopLinePaint;
    private int mTopLineHeight;
    private int mTopLineNormalColor;
    private int mTopLineFocusColor;
    private int mTopLineVisibility;
    private int mTopLinePaddingStart;
    private int mTopLinePaddingEnd;
    /**
     * 底部的线
     */
    private Paint mBottomLinePaint;
    private int mBottomLineHeight;
    private int mBottomLineNormalColor;
    private int mBottomLineFocusColor;
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
    private int mSuggestCancelWidth;
    private int mDesireInputWidth;
    private int mMinHeight;
    private ColorStateList mTintColor;

    private OnEditTextListener mEditTextListener;

    public EditTextPro(Context context) {
        this(context, null);
    }

    public EditTextPro(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextPro(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextPro);

        mTintColor = a.getColorStateList(R.styleable.EditTextPro_tintColor);

        mLeftImageDrawable = a.getDrawable(R.styleable.EditTextPro_leftImage);
        mLeftImageBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_leftImageBackground);
        mLeftImageVisibility = a.getInt(R.styleable.EditTextPro_leftImageVisibility, View.VISIBLE);
        mLeftImageRightMargin = a.getDimensionPixelSize(R.styleable.EditTextPro_leftImageRightMargin, 0);

        mLeftTextString = a.getString(R.styleable.EditTextPro_leftText);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextSize, getDefaultTipTextSize());
        mLeftTextColor = a.getColor(R.styleable.EditTextPro_leftTextColor, getDefaultTipTextColor());
        mLeftTextRightMargin = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextRightMargin, 0);
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMinWidth, getDefaultTipMinWidth());
        mLeftTextMaxWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMaxWidth, getDefaultTipMaxWidth());
        mLeftTextVisibility = a.getInt(R.styleable.EditTextPro_leftTextVisibility, View.VISIBLE);
        mLeftTextGravity = a.getInt(R.styleable.EditTextPro_leftTextGravity, Gravity.CENTER);
        mLeftTextBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_leftTextBackground);
        mLeftTextStyle = a.getInt(R.styleable.EditTextPro_leftTextStyle, 0);

        mInputBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_inputBackground);
        mInputGravity = a.getInt(R.styleable.EditTextPro_inputGravity, Gravity.CENTER_VERTICAL);
        mInputTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_inputTextSize, getDefaultInputTextSize());
        mInputRightMargin = a.getDimensionPixelSize(R.styleable.EditTextPro_inputRightMargin, 0);
        mInputTextColor = a.getColor(R.styleable.EditTextPro_inputTextColor, getDefaultInputTextColor());
        mInputHintTextColor = a.getColor(R.styleable.EditTextPro_inputHintTextColor, getDefaultInputHintTextColor());
        mInputLines = a.getInt(R.styleable.EditTextPro_inputLines, 0);
        mInputMaxLength = a.getInt(R.styleable.EditTextPro_inputTextMaxLength, 0);
        mInputTextString = a.getString(R.styleable.EditTextPro_inputText);
        mInputHintString = a.getString(R.styleable.EditTextPro_inputHint);
        mInputType = a.getInt(R.styleable.EditTextPro_inputType, InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mInputDigitsString = a.getString(R.styleable.EditTextPro_inputDigits);
        isCancelEnable = a.getBoolean(R.styleable.EditTextPro_cancelEnable, true);

        mRightTextString = a.getString(R.styleable.EditTextPro_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextSize, getDefaultTipTextSize());
        mRightTextColor = a.getColor(R.styleable.EditTextPro_rightTextColor, getDefaultTipTextColor());
        mRightTextVisibility = a.getInt(R.styleable.EditTextPro_rightTextVisibility, View.GONE);
        mRightTextRightMargin = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextRightMargin, 0);
        mRightTextPadding = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextPadding, 0);
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMinWidth, getDefaultTipMinWidth());
        mRightTextMaxWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMaxWidth, getDefaultTipMaxWidth());
        mRightTextRightDrawable = a.getDrawable(R.styleable.EditTextPro_rightTextRightDrawable);
        mRightTextGravity = a.getInt(R.styleable.EditTextPro_rightTextGravity, Gravity.CENTER);
        mRightTextBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_rightTextBackground);

        mRightImageDrawable = a.getDrawable(R.styleable.EditTextPro_rightImage);
        mRightImageBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_rightImageBackground);
        mRightImageVisibility = a.getInt(R.styleable.EditTextPro_rightImageVisibility, View.VISIBLE);

        mTopLineHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_topLineHeight, getDefaultLineHeight());
        mTopLineNormalColor = a.getColor(R.styleable.EditTextPro_topLineNormalColor, getDefaultNormalLineColor());
        mTopLineFocusColor = a.getColor(R.styleable.EditTextPro_topLineFocusColor, getDefaultFocusLineColor());
        mTopLineVisibility = a.getInt(R.styleable.EditTextPro_topLineVisibility, View.GONE);
        mTopLinePaddingStart = a.getDimensionPixelSize(R.styleable.EditTextPro_topLinePaddingStart, 0);
        mTopLinePaddingEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_topLinePaddingEnd, 0);

        mBottomLineHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLineHeight, getDefaultLineHeight());
        mBottomLineNormalColor = a.getColor(R.styleable.EditTextPro_bottomLineNormalColor, getDefaultNormalLineColor());
        mBottomLineFocusColor = a.getColor(R.styleable.EditTextPro_bottomLineFocusColor, getDefaultFocusLineColor());
        mBottomLineVisibility = a.getInt(R.styleable.EditTextPro_bottomLineVisibility, View.VISIBLE);
        mBottomLinePaddingStart = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLinePaddingStart, 0);
        mBottomLinePaddingEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLinePaddingEnd, 0);

        mCustomizeViewId = a.getResourceId(R.styleable.EditTextPro_customizeViewId, 0);
        mCustomizeViewIndex = a.getInt(R.styleable.EditTextPro_customizeViewIndex, -1);
        mCustomizeMarginStart = a.getDimensionPixelSize(R.styleable.EditTextPro_customizeMarginStart, 0);
        mCustomizeMarginEnd = a.getDimensionPixelSize(R.styleable.EditTextPro_customizeMarginEnd, 0);

        mMinHeight = a.getDimensionPixelSize(R.styleable.EditTextPro_android_minHeight, getDefaultMinHeight());
        a.recycle();

        initNecessaryData();
        initView();
    }

    private void initNecessaryData() {
        fixInputType();

        if (!isCancelEnable) {
            mCancelVisibility = GONE;
        } else if (TextUtils.isEmpty(mInputTextString)) {
            mCancelVisibility = INVISIBLE;
        } else {
            mCancelVisibility = VISIBLE;
        }

        mSuggestIconWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_40);
        mSuggestCancelWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_30);

        mTopLinePaint = new Paint();
        mTopLinePaint.setDither(true);
        mTopLinePaint.setAntiAlias(true);
        mTopLinePaint.setColor(mTopLineNormalColor);
        //noinspection SuspiciousNameCombination
        mTopLinePaint.setStrokeWidth(mTopLineHeight);

        mBottomLinePaint = new Paint();
        mBottomLinePaint.setDither(true);
        mBottomLinePaint.setAntiAlias(true);
        mBottomLinePaint.setColor(mBottomLineNormalColor);
        //noinspection SuspiciousNameCombination
        mBottomLinePaint.setStrokeWidth(mBottomLineHeight);
    }

    private void initView() {
        getLeftImageView();
        getLeftTextView();
        getInputView();
        getCancelButton();
        getRightTextView();
        getRightImageView();
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
            mLeftImageView.measure(imageWidthSpec, heightSpec);
            inputWidth = inputWidth - mLeftImageView.getMeasuredWidth() - mLeftImageRightMargin;
        }

        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            mLeftTextView.measure(unspecified, heightSpec);
            inputWidth = inputWidth - mLeftTextView.getMeasuredWidth() - mLeftTextRightMargin;
        }

        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            mRightImageView.measure(imageWidthSpec, heightSpec);
            inputWidth = inputWidth - mRightImageView.getMeasuredWidth();
        }

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            mRightTextView.measure(unspecified, unspecified);
            inputWidth = inputWidth - mRightTextView.getMeasuredWidth() - mRightTextRightMargin;
        }

        if (mCustomizeView != null && mCustomizeView.getVisibility() != View.GONE) {
            measureChild(mCustomizeView, widthSpec, heightSpec);
            inputWidth = inputWidth - mCustomizeView.getMeasuredWidth() - mCustomizeMarginStart - mCustomizeMarginEnd;
        }

        if (mCancelView != null && mCancelView.getVisibility() != View.GONE) {
            inputWidth = inputWidth - mSuggestCancelWidth;
        }
        inputWidth = inputWidth - mInputRightMargin;
        mInputView.measure(MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY), heightSpec);

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

                if (mCancelView != null && mCancelView.getVisibility() != View.GONE) {
                    inputWidth = inputWidth - mSuggestCancelWidth;
                }
                inputWidth = inputWidth - mInputRightMargin;

                final int widthSpec = MeasureSpec.makeMeasureSpec(inputWidth, MeasureSpec.EXACTLY);
                final int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                mInputView.measure(widthSpec, heightSpec);
                final int inputHeight = mInputView.getMeasuredHeight();
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
        left = layoutCustomize(left, index);
        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            int height = mLeftImageView.getMeasuredHeight();
            int height2 = getMeasuredHeight();

            mLeftImageView.layout(left, top, left + mSuggestIconWidth, bottom);
            left = left + mSuggestIconWidth + mLeftImageRightMargin;
            index++;
        }
        left = layoutCustomize(left, index);
        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            int width = mLeftTextView.getMeasuredWidth();
            mLeftTextView.layout(left, top, left + width, bottom);
            left = left + width + mLeftTextRightMargin;
            index++;
        }
        left = layoutCustomize(left, index);

        int inputWidth = mInputView.getMeasuredWidth();
        int inputHeight = mInputView.getMeasuredHeight();
        mInputView.layout(left, top, left + inputWidth, bottom);
        left = left + inputWidth;

        if (mCancelView != null && mCancelView.getVisibility() != View.GONE) {
            mCancelView.layout(left, top, left + mSuggestCancelWidth, bottom);
            left = left + mSuggestCancelWidth;
        }
        left = left + mInputRightMargin;
        index++;
        left = layoutCustomize(left, index);

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            int width = mRightTextView.getMeasuredWidth();
            int height = mRightTextView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            mRightTextView.layout(left, _top, left + width, _top + height);
            left = left + width + mRightTextRightMargin;
            index++;
        }
        left = layoutCustomize(left, index);
        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            mRightImageView.layout(left, top, right, bottom);
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
    public void onFocusChange(View v, boolean hasFocus) {
        mTopLinePaint.setColor(hasFocus ? mTopLineFocusColor : mTopLineNormalColor);
        mBottomLinePaint.setColor(hasFocus ? mBottomLineFocusColor : mBottomLineNormalColor);
        invalidate();
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.cancel) {
            cancel();
        }
    }

    private void cancel() {
        String text = getInputText();
        if (!TextUtils.isEmpty(text)) {
            notifyCancel();
        }
        mInputView.setText(StringUtils.EMPTY);
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
    private EditText getInputView() {
        if (mInputView == null) {
            mInputView = new AppCompatEditText(getContext());
            mInputView.setPadding(0, 0, 0, 0);
            mInputView.setBackground(mInputBackgroundDrawable);
            mInputView.setGravity(mInputGravity);
            mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInputTextSize);
            mInputView.setTextColor(mInputTextColor);
            mInputView.setHintTextColor(mInputHintTextColor);
            mInputView.setOnFocusChangeListener(this);

            mInputView.setInputType(mInputType);


            if (mInputLines > 0) {
                mInputView.setLines(mInputLines);
            }

            if (mInputMaxLength != 0) {
                mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputMaxLength)});
            }

            if (!TextUtils.isEmpty(mInputDigitsString)) {
                mInputView.setKeyListener(DigitsKeyListener.getInstance(mInputDigitsString));
            }

            mInputView.setText(mInputTextString);
            mInputView.setHint(mInputHintString);
            mInputView.addTextChangedListener(new InputWatcher());
            addView(mInputView);
        }

        return mInputView;
    }

    @SuppressWarnings("UnusedReturnValue")
    private ImageView getCancelButton() {

        if (mCancelView == null && isCancelEnable) {
            mCancelView = new ImageView(getContext());
            mCancelView.setId(R.id.cancel);
            mCancelView.setImageResource(R.drawable.ic_cancel);
            mCancelView.setVisibility(mCancelVisibility);
            mCancelView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mCancelView.setOnClickListener(this);
            addView(mCancelView);
        }
        return mCancelView;
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

    @SuppressWarnings("deprecation")
    private int getDefaultInputHintTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_hint);
    }

    /**
     * 获取默认的颜色值
     */
    private int getDefaultNormalLineColor() {
        return getContext().getResources().getColor(R.color.line);
    }

    /**
     * 获取默认的颜色值
     */
    private int getDefaultFocusLineColor() {
        return getContext().getResources().getColor(R.color.color_accent);
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

    private void notifyCancel() {
        if (mEditTextListener != null) {
            mEditTextListener.onCancel();
        }
    }

    public void setOnEditTextListener(OnEditTextListener listener) {
        mEditTextListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnEditTextListener {
        void onCancel();
    }

    public void focus() {
        mInputView.requestFocus();
        showInput();
    }

    public boolean hasFocus() {
        return mInputView != null && mInputView.hasFocus();
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

    public String getInputText() {
        Editable inputText = mInputView.getText();
        if (inputText == null) {
            return StringUtils.EMPTY;
        } else {
            return inputText.toString().trim();
        }
    }

    public void setInputText(@StringRes int textRes) {
        String text = getContext().getString(textRes);
        setInputText(text);
    }

    public void setInputText(CharSequence text) {
        setInputText(text, false);
    }

    public synchronized void setInputText(CharSequence text, boolean selectionEnd) {
        mInputView.setText(text);
        if (selectionEnd) {
            final Editable nowText = mInputView.getText();
            mInputView.setSelection(nowText == null ? 0 : nowText.length());
        }
    }


    public void setInputHint(String text) {
        mInputView.setHint(text);
    }

    public void setInputHint(@StringRes int hint) {
        mInputView.setHint(hint);
    }

    public void setInputTextSize(@DimenRes int resId) {
        float size = getResources().getDimensionPixelSize(resId);
        mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void setInputTextColor(@ColorInt int colorInt) {
        mInputView.setTextColor(colorInt);
    }

    public void addInputWatcher(TextWatcher watcher) {
        mInputView.addTextChangedListener(watcher);
    }

    public void removeInputWatcher(TextWatcher watcher) {
        mInputView.removeTextChangedListener(watcher);
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
    }

    public void setBottomLineVisibility(int visibility) {
        mBottomLineVisibility = visibility;
        postInvalidate();
    }

    public void hideInput() {
        post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null)
                    manager.hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
            }
        });
    }

    public void showInput() {
        post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (manager != null) manager.showSoftInput(mInputView, 0);
            }
        });
    }

    public void setRightImageResource(@DrawableRes int resid) {
        if (mRightImageView != null) {
            mRightImageView.setImageResource(resid);
        }
    }

    public void setRightImageVisibility(int visibility) {
        if (mRightImageView != null) {
            mRightImageView.setVisibility(visibility);
            mRightImageVisibility = visibility;
        }
    }

    public void setRightImageClickListener(View.OnClickListener listener) {

        if (mRightImageView != null) {
            mRightImageView.setOnClickListener(listener);
        }
    }

    public void setRightTextClickListener(View.OnClickListener listener) {

        if (mRightTextView != null) {
            mRightTextView.setOnClickListener(listener);
        }
    }

    private class InputWatcher implements TextWatcher {
        /**
         * 和事佬...
         */
        private String mMediator;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            mMediator = s.toString();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!isCancelEnable) {
                return;
            }
            // 如果是相等 那么和事佬会判读是第一次来 不准通过
            if (TextUtils.equals(mMediator, s.toString())) {
                return;
            }

            if (s.length() == 0) {
                notifyCancel();
                mCancelView.setVisibility(INVISIBLE);
            } else {
                mCancelView.setVisibility(VISIBLE);
            }
        }
    }


    private void fixInputType() {
        if ((mInputType & EditorInfo.TYPE_MASK_CLASS) == EditorInfo.TYPE_CLASS_TEXT) {
            if (mInputLines == 1) {
                mInputType &= ~EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
            } else {
                mInputType |= EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
            }
        }
    }


}
