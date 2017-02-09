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
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bright.common.R;
import com.bright.common.widget.imageview.PromptImageView;
import com.bright.common.widget.textview.PromptTextView;

/**
 * Created by Bright.Yu on 2017/2/9.
 * 加强版本的EditText
 */
@SuppressWarnings("unused")
public class EditTextPro extends ViewGroup {
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
    private Drawable mRightTextBackgroundDrawable;

    /**
     * 右侧图片信息
     */
    private Drawable mRightImageDrawable;
    private Drawable mRightImageBackgroundDrawable;
    private int mRightImageVisibility;

    /**
     * 最底部的线
     */
    private int mNormalColor;
    private int mFocusColor;
    private int mBottomLineVisibility;

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
        mRightTextBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_rightTextBackground);

        mRightImageDrawable = a.getDrawable(R.styleable.EditTextPro_rightImage);
        mRightImageBackgroundDrawable = a.getDrawable(R.styleable.EditTextPro_rightImageBackground);
        mRightImageVisibility = a.getInt(R.styleable.EditTextPro_rightImageVisibility, View.VISIBLE);

        mNormalColor = a.getColor(R.styleable.EditTextPro_normalColor, getDefaultNormalColor());
        mFocusColor = a.getColor(R.styleable.EditTextPro_focusColor, getDefaultFocusColor());
        mBottomLineVisibility = a.getInt(R.styleable.EditTextPro_bottomLineVisibility, View.VISIBLE);
        a.recycle();

        initView();
    }

    private void initView() {
        getLeftImageView();
        getLeftTextView();
        getInputView();
    }

    public PromptImageView getLeftImageView() {

        if (mLeftImageVisibility == View.GONE) {
            Log.i(TAG, "getLeftImageView: is visiable gone just not add");
            return null;
        }

        if (mLeftImageView == null) {
            mLeftImageView = new PromptImageView(getContext());
            mLeftImageView.setId(R.id.leftImage);
            mLeftImageView.setVisibility(mLeftImageVisibility);
            mLeftImageView.setImageDrawable(mLeftImageDrawable);
            mLeftImageView.setBackground(mLeftImageBackgroundDrawable);
            mLeftImageView.setScaleType(ImageView.ScaleType.CENTER);
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
            mLeftTextView.setLines(1);
            mLeftTextView.setEllipsize(TextUtils.TruncateAt.END);
            mLeftTextView.setBackground(mLeftTextBackgroundDrawable);
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
            if (mInputMaxLength != 0) {
                mInputView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mInputMaxLength)});
            }
            addView(mInputView);
        }

        return mInputView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    private int getDefaultTipTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.G_title);
    }

    private int getDefaultTipTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_light_normal);
    }

    private int getDefaultInputTextSize() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.H_title);
    }

    private int getDefaultInputTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_normal);
    }

    private int getDefaultInputHintTextColor() {
        return getContext().getResources().getColor(R.color.text_grey_hint);
    }

    /**
     * 获取默认的颜色值
     */
    private int getDefaultNormalColor() {
        return getContext().getResources().getColor(R.color.grey_deep_10);
    }

    /**
     * 获取默认的颜色值
     */
    private int getDefaultFocusColor() {
        return getContext().getResources().getColor(R.color.color_accent);
    }


}
