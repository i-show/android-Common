/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.common.widget.dialog.picker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.ishow.common.R;
import com.ishow.common.entries.IUnitPicker;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.widget.TopBar;
import com.ishow.common.widget.pickview.PickerView;

import java.util.List;

/**
 * Created by yuhaiyang on 2016/10/31.
 * Picker 选择的Dialog
 */

public class PickerDialog<T extends IUnitPicker> extends Dialog implements TopBar.OnTopBarListener {
    private PickerDialogAdapter<T> mAdapter;
    private OnPickedListener mPickedListener;
    private PickerView mPickerView;
    private int mCurrentPosition;

    public PickerDialog(Context context) {
        super(context, R.style.Theme_Dialog_Bottom_Transparent);
        mAdapter = new PickerDialogAdapter<>(getContext());
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unit_picker);

        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);

        mPickerView = (PickerView) findViewById(R.id.picker);
        mPickerView.setAdapter(mAdapter);
        mPickerView.setCurrentPosition(mCurrentPosition);
    }

    public void setData(List<T> data) {
        mAdapter.setData(data);
    }

    public void setCurrentPosition(int position) {
        mCurrentPosition = position;
        if (mPickerView != null) {
            mPickerView.setCurrentPosition(mCurrentPosition);
        }
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = DeviceUtils.INSTANCE.getScreenSize()[0];
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }


    private void notifySelectCityChanged(T selected) {
        if (mPickedListener != null) {
            mPickedListener.onSelectPicked(selected);
        }
    }

    public void setOnSelectedListener(OnPickedListener<T> listener) {
        mPickedListener = listener;
    }

    public interface OnPickedListener<T extends IUnitPicker> {
        void onSelectPicked(T t);
    }

    @Override
    public void onLeftClick(View v) {
        dismiss();
    }

    @Override
    public void onRightClick(View v) {
        int position = mPickerView.getCurrentPosition();
        notifySelectCityChanged(mAdapter.getItem(position));
        dismiss();
    }

    @Override
    public void onTitleClick(View v) {

    }


}
