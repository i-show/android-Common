/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bright.common.R;
import com.bright.common.utils.UnitUtils;
import com.bright.common.widget.imageview.PromptImageView;
import com.bright.common.widget.prompt.IPrompt;
import com.bright.common.widget.textview.PromptTextView;

/**
 * Created by Bright.Yu on 2017/2/9.
 * 加强版本的EditText
 */
@SuppressWarnings("unused")
public class EditTextPro extends ViewGroup implements View.OnFocusChangeListener {
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

    /**
     * 左侧文本信息
     */
    private String mLeftTextString;
    private int mLeftTextSize;
    private int mLeftTextColor;
    private int mLeftTextVisibility;
    private int mLeftTextMinWidth;
    private int mLeftTextMaxWidth;
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
    private String mInputHintString;
    private boolean mInputEditable;

    /**
     * 右侧文本信息
     */
    private String mRightTextString;
    private int mRightTextSize;
    private int mRightTextColor;
    private int mRightTextVisibility;
    private int mRightTextMinWidth;
    private int mRightTextMaxWidth;
    private Drawable mRightTextBackgroundDrawable;

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
    /**
     * 最底部的线
     */
    private int mNormalColor;
    private int mFocusColor;
    private int mBottomLineVisibility;

    private int mMinHegiht;
    private int mBottomLineHegiht;

    private Paint mBottomLinePaint;

    public EditTextPro(Context context) {
        this(context, null);
    }

    public EditTextPro(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditTextPro(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextPro);
        mLeftImageDrawable = a.getDrawable(R.styleable.EditTextPro_leftImage);
        mLeftImageBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_leftImageBackground);
        mLeftImageVisibility = a.getInt(R.styleable.EditTextPro_leftImageVisibility, View.VISIBLE);

        mLeftTextString = a.getString(R.styleable.EditTextPro_leftText);
        mLeftTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextSize, getDefaultTipTextSize());
        mLeftTextColor = a.getColor(R.styleable.EditTextPro_leftTextColor, getDefaultTipTextColor());
        mLeftTextMinWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMinWidth, getDefaultTipMinWidth());
        mLeftTextMaxWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_leftTextMinWidth, getDefaultTipMaxWidth());
        mLeftTextVisibility = a.getInt(R.styleable.EditTextPro_leftTextVisibility, getDefaultTipTextColor());
        mLeftTextBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_leftTextBackground);

        mInputBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_inputBackground);
        mInputGravity = a.getInt(R.styleable.EditTextPro_inputGravity, Gravity.CENTER_VERTICAL);
        mInputTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_inputTextSize, getDefaultInputTextColor());
        mInputTextColor = a.getColor(R.styleable.EditTextPro_inputTextColor, getDefaultInputTextColor());
        mInputHintTextColor = a.getColor(R.styleable.EditTextPro_inputHintTextColor, getDefaultInputHintTextColor());
        mInputLines = a.getInt(R.styleable.EditTextPro_inputLines, 1);
        mInputMaxLength = a.getInt(R.styleable.EditTextPro_inputTextMaxLenght, 0);
        mInputHintString = a.getString(R.styleable.EditTextPro_inputHint);
        mInputEditable = a.getBoolean(R.styleable.EditTextPro_editable, true);

        mRightTextString = a.getString(R.styleable.EditTextPro_rightText);
        mRightTextSize = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextSize, getDefaultTipTextSize());
        mRightTextColor = a.getColor(R.styleable.EditTextPro_rightTextColor, getDefaultTipTextColor());
        mRightTextVisibility = a.getColor(R.styleable.EditTextPro_rightTextVisibility, View.GONE);
        mRightTextMinWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMinWidth, getDefaultTipMinWidth());
        mRightTextMaxWidth = a.getDimensionPixelSize(R.styleable.EditTextPro_rightTextMinWidth, getDefaultTipMaxWidth());
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
        mSuggestIconWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_45);
        mSuggestCancelWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dp_15);
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
        final int height = measureHeight(width, heightMeasureSpec);
        final int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        final int imageWidthSpec = MeasureSpec.makeMeasureSpec(mSuggestIconWidth, MeasureSpec.EXACTLY);
        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            mLeftImageView.measure(imageWidthSpec, heightSpec);
        }

        if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
            mRightImageView.measure(imageWidthSpec, heightSpec);
        }

        int test = UnitUtils.dip2px(height);

        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        final int mode = MeasureSpec.getMode(widthMeasureSpec);
        final int size = MeasureSpec.getSize(widthMeasureSpec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                throw new IllegalStateException("need not set  width MeasureSpec.UNSPECIFIED");
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
                    inputWidth = inputWidth - mSuggestIconWidth;
                }

                if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
                    final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    mLeftTextView.measure(unspecified, unspecified);
                    final int leftTextHeight = mLeftTextView.getMeasuredHeight();
                    final int leftTextWidth = mLeftTextView.getMeasuredWidth();
                    inputWidth = inputWidth - leftTextWidth;
                    height = Math.max(height, leftTextHeight);
                }

                if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
                    final int unspecified = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                    mRightTextView.measure(unspecified, unspecified);
                    final int rightTextHeight = mRightTextView.getMeasuredHeight();
                    final int rightTextWidth = mRightTextView.getMeasuredWidth();
                    inputWidth = inputWidth - rightTextWidth;
                    height = Math.max(height, rightTextHeight);
                }

                if (mRightImageView != null && mRightImageView.getVisibility() != View.GONE) {
                    inputWidth = inputWidth - mSuggestIconWidth;
                }

                if (!mInputEditable) {
                    inputWidth = inputWidth - mSuggestCancelWidth;
                }

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
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        left = getPaddingStart();
        top = getPaddingTop();
        right = right - getPaddingEnd();
        bottom = bottom - getPaddingBottom();

        if (mLeftImageView != null && mLeftImageView.getVisibility() != View.GONE) {
            int height = mLeftImageView.getMeasuredHeight();
            int height2 = getMeasuredHeight();

            mLeftImageView.layout(left, top, left + mSuggestIconWidth, bottom);
            left = left + mSuggestIconWidth;
        }

        if (mLeftTextView != null && mLeftTextView.getVisibility() != View.GONE) {
            int width = mLeftTextView.getMeasuredWidth();
            mLeftTextView.layout(left, top, left + width, bottom);
            left = left + width;
        }

        int inputWidth = mInputView.getMeasuredWidth();
        mInputView.layout(left, top, left + inputWidth, bottom);
        left = left + inputWidth;

        if (mInputEditable && mCancelView != null) {
            mCancelView.layout(left, top, left + mSuggestIconWidth, bottom);
            left = left + mSuggestIconWidth;
        }

        if (mRightTextView != null && mRightTextView.getVisibility() != View.GONE) {
            int width = mRightTextView.getMeasuredWidth();
            mLeftTextView.layout(left, top, left + width, bottom);
            left = left + width;
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
            mLeftTextView.setGravity(Gravity.CENTER);
            mLeftTextView.setTextColor(mLeftTextColor);
            mLeftTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
            mLeftTextView.setMinWidth(mLeftTextMinWidth);
            mLeftTextView.setMaxWidth(mLeftTextMaxWidth);
            mLeftTextView.setBackground(mLeftTextBackgroundDrawable);
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
            mInputView.setLines(mInputLines);
            mInputView.setHintTextColor(mInputHintTextColor);
            mInputView.setOnFocusChangeListener(this);
            if (mInputMaxLength != 0) {
                mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputMaxLength)});
            }
            addView(mInputView);
        }

        return mInputView;
    }

    private ImageView getCancelButton() {
        if (!mInputEditable) {
            Log.i(TAG, "getCancelButton: can not editable ");
            return null;
        }


        if (mCancelView == null) {
            mCancelView = new ImageView(getContext());
            mCancelView.setImageResource(R.drawable.ic_cancel);
            mCancelView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
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
            mRightImageView = new PromptImageView(getContext());
            mRightImageView.setId(R.id.rightImage);
            mRightImageView.setVisibility(mRightImageVisibility);
            mRightImageView.setImageDrawable(mRightImageDrawable);
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

        prompt.setPromptMode(IPrompt.MODE_NONE);
        prompt.commit();
    }


    private int getDefaultTipTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.G_title);
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
        return UnitUtils.dip2px(0.8f);
    }


}
