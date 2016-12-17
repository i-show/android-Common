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

package com.brightyu.androidcommon.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bright.common.constant.DefaultColors;
import com.bright.common.utils.StringUtils;
import com.bright.common.utils.UnitUtils;
import com.bright.common.utils.Utils;
import com.brightyu.androidcommon.R;


/**
 * 搜索输入框
 */
public class InputEdit extends LinearLayout implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "InputEdit";
    private View mCancelButton;
    private EditText mInputView;
    private InputMethodManager mInputMethodManager;
    private CallBack mCallBack;
    private long mLastSearchTime;
    private Paint mBottomPaint;

    private String mTipString;
    private String mHintString;
    private int mNormalColor;
    private int mFocusColor;
    private int mLineHeight;
    private int mInputType;

    public InputEdit(Context context) {
        this(context, null);
    }

    public InputEdit(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputEdit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.InputEdit);
        mTipString = a.getString(R.styleable.InputEdit_tip);
        mHintString = a.getString(R.styleable.InputEdit_hint);
        mNormalColor = a.getColor(R.styleable.InputEdit_normalColor, DefaultColors.GERY_LIGHT);
        mFocusColor = a.getColor(R.styleable.InputEdit_focusColor, DefaultColors.GERY_LIGHT);
        mInputType = a.getInt(R.styleable.InputEdit_android_inputType, InputType.TYPE_NULL);
        a.recycle();

        mLineHeight = UnitUtils.dip2px(context, 0.8f);
        mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mBottomPaint = new Paint();
        mBottomPaint.setDither(true);
        mBottomPaint.setColor(mNormalColor);
        mBottomPaint.setStrokeWidth(mLineHeight);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setCurrentView();

        mCancelButton = findViewById(R.id.cancel);
        mCancelButton.setOnClickListener(this);
        mCancelButton.setVisibility(INVISIBLE);

        mInputView = (EditText) findViewById(R.id.input);
        mInputView.addTextChangedListener(new InputWatcher());
        mInputView.setOnFocusChangeListener(this);
        mInputView.setHint(mHintString);
        if (mInputType != InputType.TYPE_NULL) {
            mInputView.setInputType(mInputType);
        }

        if (!TextUtils.isEmpty(mTipString)) {
            TextView tip = (TextView) findViewById(R.id.tip);
            tip.setVisibility(VISIBLE);
            tip.setText(mTipString);
        }
    }

    public void setInputText(String text) {
        mInputView.setText(text);
        if (!TextUtils.isEmpty(text)) {
            mInputView.setSelection(text.length());
        }
    }

    protected void setCurrentView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View root = inflater.inflate(R.layout.widget_input_edit, this, false);
        addView(root, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                String text = mInputView.getText().toString().trim();
                if (mCallBack != null && !TextUtils.isEmpty(text)) {
                    mCallBack.onCancel();
                }
                mInputView.setText(StringUtils.EMPTY);
                break;
        }
    }


    public void hideInput() {
        mInputMethodManager.hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
    }

    public void showInput() {
        post(new Runnable() {
            @Override
            public void run() {
                mInputMethodManager.showSoftInput(mInputView, 0);
            }
        });
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        mBottomPaint.setColor(hasFocus ? mFocusColor : mNormalColor);
        invalidate();
    }

    public void focus() {
        mInputView.requestFocus();
        showInput();
    }

    public boolean hasFocus() {
        return mInputView.hasFocus();
    }

    public String getInputText() {
        return mInputView.getText().toString().trim();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        canvas.drawLine(0, height - mLineHeight, width, height - mLineHeight, mBottomPaint);
    }

    public interface CallBack {

        void onCancel();
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

            if (s.length() == 0) {
                if (mCallBack != null) {
                    mCallBack.onCancel();
                }
                mCancelButton.setVisibility(INVISIBLE);
            } else {
                mCancelButton.setVisibility(VISIBLE);
            }
        }
    }
}
