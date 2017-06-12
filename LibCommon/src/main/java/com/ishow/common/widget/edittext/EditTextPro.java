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
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.ishow.common.R;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.UnitUtils;
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
    private EnableEditText mInputView;
    private ImageView mCancelView;
    /**
     * 右边的View
     */
    private PromptImageView mRightImageView;
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
    private Drawable mInputBackgroundDrawable;
    private int mInputGravity;
    private int mInputTextSize;
    private int mInputTextColor;
    private int mInputHintTextColor;
    private int mInputLines;
    private int mInputMaxLength;
    private int mInputRightMargin;
    private int mInputType;
    private String mInputHintString;
    private boolean mInputEditable;

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
     * 图片的建议宽度
     */
    private int mSuggestIconWidth;
    private int mSuggestCancelWidth;
    private int mDesireInputWidth;
    /**
     * 最底部的线
     */
    private int mNormalColor;
    private int mFocusColor;
    private int mBottomLineVisibility;
    private int mCancelVisibility;

    private int mMinHegiht;
    private int mBottomLineHegiht;

    private ColorStateList mTintColor;

    private Paint mBottomLinePaint;
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

        mInputBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_inputBackground);
        mInputGravity = a.getInt(R.styleable.EditTextPro_inputGravity, Gravity.CENTER_VERTICAL);
        mInputTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_inputTextSize, getDefaultInputTextSize());
        mInputRightMargin = a.getDimensionPixelSize(R.styleable.EditTextPro_inputRightMargin, 0);
        mInputTextColor = a.getColor(R.styleable.EditTextPro_inputTextColor, getDefaultInputTextColor());
        mInputHintTextColor = a.getColor(R.styleable.EditTextPro_inputHintTextColor, getDefaultInputHintTextColor());
        mInputLines = a.getInt(R.styleable.EditTextPro_inputLines, 1);
        mInputMaxLength = a.getInt(R.styleable.EditTextPro_inputTextMaxLenght, 0);
        mInputHintString = a.getString(R.styleable.EditTextPro_inputHint);
        mInputEditable = a.getBoolean(R.styleable.EditTextPro_editable, true);
        mCancelVisibility = a.getInt(R.styleable.EditTextPro_cancelVisibility, View.INVISIBLE);
        mInputType = a.getInt(R.styleable.EditTextPro_android_inputType, InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        mRightTextString = a.getString(R.styleable.EditTextPro_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextSize, getDefaultTipTextSize());
        mRightTextColor = a.getColor(R.styleable.EditTextPro_rightTextColor, getDefaultTipTextColor());
        mRightTextVisibility = a.getColor(R.styleable.EditTextPro_rightTextVisibility, View.GONE);
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

        mNormalColor = a.getColor(R.styleable.EditTextPro_normalColor, getDefaultNormalColor());
        mFocusColor = a.getColor(R.styleable.EditTextPro_focusColor, getDefaultFocusColor());
        mBottomLineHegiht = a.getDimensionPixelSize(R.styleable.EditTextPro_bottomLineHegiht, getDefaultBottomLineHeight());
        mBottomLineVisibility = a.getInt(R.styleable.EditTextPro_bottomLineVisibility, View.VISIBLE);

        mMinHegiht = a.getDimensionPixelSize(R.styleable.EditTextPro_android_minHeight, getDefaultMinHeight());
        a.recycle();

        initNecessaryData();
        initView();
    }

    private void initNecessaryData() {
        mSuggestIconWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_40);
        mSuggestCancelWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_30);
        mBottomLinePaint = new Paint();
        mBottomLinePaint.setDither(true);
        mBottomLinePaint.setAntiAlias(true);
        mBottomLinePaint.setColor(mNormalColor);
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
            mRightImageView.measure(imageWidthSpec, heightSpec);
            inputWidth = inputWidth - mRightImageView.getMeasuredWidth();
        }

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            mRightTextView.measure(unspecified, unspecified);
            inputWidth = inputWidth - mRightTextView.getMeasuredWidth() - mRightTextRightMargin;
        }

        if (mInputEditable && mCancelView != null && mCancelView.getVisibility() != View.GONE) {
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

                if (mInputEditable && mCancelView != null && mCancelView.getVisibility() != View.GONE) {
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

        int inputWidth = mInputView.getMeasuredWidth();
        int inputHeight = mInputView.getMeasuredHeight();
        int g = mInputView.getGravity();
        mInputView.layout(left, top, left + inputWidth, bottom);
        left = left + inputWidth;

        if (mInputEditable && mCancelView != null && mCancelView.getVisibility() != View.GONE) {
            mCancelView.layout(left, top, left + mSuggestCancelWidth, bottom);
            left = left + mSuggestCancelWidth;
        }
        left = left + mInputRightMargin;

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            int width = mRightTextView.getMeasuredWidth();
            int height = mRightTextView.getMeasuredHeight();
            int _top = (getMeasuredHeight() - height) / 2;
            mRightTextView.layout(left, _top, left + width, _top + height);
            left = left + width + mRightTextRightMargin;
        }

        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            mRightImageView.layout(left, top, right, bottom);
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mBottomLinePaint.setColor(hasFocus ? mFocusColor : mNormalColor);
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
        String text = mInputView.getText().toString().trim();
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

        if (mBottomLineVisibility == VISIBLE) {
            canvas.drawLine(0, height - mBottomLineHegiht, width, height, mBottomLinePaint);
        }
    }

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


    private EditText getInputView() {
        if (mInputView == null) {
            mInputView = new EnableEditText(getContext());
            mInputView.setBackground(mInputBackgroundDrawable);
            mInputView.setEditable(mInputEditable);
            mInputView.setGravity(mInputGravity);
            mInputView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mInputTextSize);
            mInputView.setTextColor(mInputTextColor);
            mInputView.setHint(mInputHintString);
            mInputView.setHintTextColor(mInputHintTextColor);
            mInputView.setOnFocusChangeListener(this);
            mInputView.addTextChangedListener(new InputWatcher());
            mInputView.setInputType(mInputType);
            mInputView.setLines(mInputLines);
            if (mInputMaxLength != 0) {
                mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputMaxLength)});
            }
            addView(mInputView);
        }

        return mInputView;
    }

    private ImageView getCancelButton() {
        if (!mInputEditable && mCancelVisibility == View.GONE) {
            Log.i(TAG, "getCancelButton: can not editable ");
            return null;
        }


        if (mCancelView == null) {
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
    @SuppressWarnings("deprecation")
    private int getDefaultNormalColor() {
        return getContext().getResources().getColor(R.color.grey_deep_10);
    }

    /**
     * 获取默认的颜色值
     */
    @SuppressWarnings("deprecation")
    private int getDefaultFocusColor() {
        return getContext().getResources().getColor(R.color.color_accent);
    }

    /**
     * 获取默认的最小高度
     */
    private int getDefaultMinHeight() {
        return getResources().getDimensionPixelSize(R.dimen.girdle_min_h);
    }

    /**
     * 获取默认的最小高度
     */
    private int getDefaultBottomLineHeight() {
        return 1;
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
        return mInputView.getText().toString().trim();
    }

    public void setInputText(@StringRes int textRes) {
        String text = getContext().getString(textRes);
        setInputText(text);
    }

    public void setInputText(String text) {
        mInputView.setText(text);
        if (!TextUtils.isEmpty(text)) {
            mInputView.setSelection(text.length());
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

    public void setInputEditable(boolean editable) {
        if (mInputView != null) {
            mInputView.setEditable(editable);
            if (!editable) {
                mCancelVisibility = View.GONE;
                mCancelView.setVisibility(mCancelVisibility);
            } else {
                mCancelVisibility = mInputView.getText().length() > 0 ? VISIBLE : GONE;
                mCancelView.setVisibility(mCancelVisibility);
            }
        }
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
                manager.hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
            }
        });
    }

    public void showInput() {
        post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.showSoftInput(mInputView, 0);
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
            // 如果是相等 那么和事佬会判读是第一次来 不准通过
            if (TextUtils.equals(mMediator, s.toString())) {
                return;
            }

            if (mCancelVisibility == View.GONE && !mInputEditable) {
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

}
