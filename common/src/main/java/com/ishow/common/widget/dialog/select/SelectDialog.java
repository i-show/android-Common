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

package com.ishow.common.widget.dialog.select;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;


import com.ishow.common.R;
import com.ishow.common.entries.IUnitSelect;
import com.ishow.common.utils.DeviceUtils;

import java.util.List;

/**
 * Created by yuhaiyang on 2016/10/31.
 * 一个统一的从底下弹出的Dialog选择
 */

public class SelectDialog<T extends IUnitSelect> extends Dialog implements AdapterView.OnItemClickListener {
    private SelectAdapter<T> mAdapter;
    private OnSelectedListener mSelectedListener;
    private View mBindView;

    @SuppressWarnings("unused")
    public SelectDialog(Context context) {
        super(context, R.style.Theme_Dialog_Bottom_Transparent);
        mAdapter = new SelectAdapter<>(getContext());
        setCancelable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unit_select);

        ListView list = findViewById(R.id.list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
    }

    public void setData(List<T> data) {
        mAdapter.setData(data);
    }

    public List<T> getData() {
        return mAdapter.getData();
    }

    public View getBindView() {
        return mBindView;
    }

    public void setBindView(View view) {
        mBindView = view;
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        T item = mAdapter.getItem(position);
        notifySelectCityChanged(item);
        dismiss();
    }


    @SuppressWarnings("unchecked")
    private void notifySelectCityChanged(T selected) {
        if (mSelectedListener != null) {
            mSelectedListener.onSelected(selected);
        }
    }

    @SuppressWarnings("unused")
    public void setOnSelectedListener(OnSelectedListener<T> listener) {
        mSelectedListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    public interface OnSelectedListener<T extends IUnitSelect> {
        void onSelected(T t);
    }
}
