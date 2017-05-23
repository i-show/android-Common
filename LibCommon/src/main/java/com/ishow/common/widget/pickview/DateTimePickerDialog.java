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

package com.ishow.common.widget.pickview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.ishow.common.R;
import com.ishow.common.utils.ScreenUtils;
import com.ishow.common.widget.TopBar;


/**
 * 日期选择对话框
 */
public class DateTimePickerDialog extends Dialog implements TopBar.OnTopBarListener {
    private static final String TAG = "DateTimePickerDialog";
    private DateTimePicker mPicker;
    private OnSelectDateListener mListener;
    private long mTime;
    private int mStyle;
    private String mTopBarString;

    public DateTimePickerDialog(Context context) {
        this(context, DateTimePicker.Style.DATE_TIME);
    }

    @SuppressWarnings("WeakerAccess")
    public DateTimePickerDialog(Context context, int type) {
        super(context, R.style.Dialog_DateTimePicker);
        mStyle = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_time_picker);
        TopBar topBar = (TopBar) findViewById(R.id.topbar);
        topBar.setOnTopBarListener(this);

        mPicker = (DateTimePicker) findViewById(R.id.picker);
        mPicker.setStyle(mStyle);
        if (mTime != 0) {
            mPicker.setCurrentDate(mTime);
        }
        if (!TextUtils.isEmpty(mTopBarString)) {
            topBar.setText(mTopBarString);
        }
    }


    public void setTitle(@StringRes int resid) {
        mTopBarString = getContext().getString(resid);
    }

    public void setDate(long time) {
        if (time == 0) {
            return;
        }
        mTime = time;
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window == null) {
            Log.i(TAG, "show: window is null");
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = ScreenUtils.getScreenSize()[0];
        int height = ScreenUtils.getScreenSize()[1];
        if (width > height) {
            //noinspection SuspiciousNameCombination
            lp.width = height;
        } else {
            lp.width = width;
        }
        lp.gravity = Gravity.BOTTOM;
        window.setWindowAnimations(R.style.Animation_Windows_Bottom);
        window.setAttributes(lp);
    }

    @Override
    public void onLeftClick(View v) {
        dismiss();
    }

    @Override
    public void onRightClick(View v) {
        if (mListener != null) {
            mListener.onSelected(mPicker.getCurrentTimeInMillis());
        }
        dismiss();
    }

    @Override
    public void onTitleClick(View v) {

    }

    @SuppressWarnings("unused")
    public void setSelectDateListener(OnSelectDateListener listener) {
        mListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnSelectDateListener {
        void onSelected(long time);
    }
}