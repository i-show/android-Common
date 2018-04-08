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

package com.ishow.noah.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.utils.IntentUtils;
import com.ishow.common.utils.SharedPreferencesUtils;
import com.ishow.common.utils.log.LogManager;
import com.ishow.noah.R;
import com.ishow.noah.entries.Version;
import com.ishow.noah.manager.VersionManager;

/**
 * Created by yuhaiyang on 2017/8/1.
 * 版本信息的Dialog
 */

public class VersionDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "VersionDialog";
    private TextView mMessage;
    private CheckBox mIgnore;
    private View mCancelView;
    private View mSubmitView;

    public VersionDialog(@NonNull Context context) {
        super(context, R.style.Theme_Dialog_Transparent);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_version);

        mMessage = findViewById(R.id.message);
        mIgnore = findViewById(R.id.ignore);

        mCancelView = findViewById(R.id.cancel);
        mCancelView.setOnClickListener(this);

        mSubmitView = findViewById(R.id.submit);
        mSubmitView.setOnClickListener(this);

        init();
    }

    private void init() {
        Version version = VersionManager.getInstance().getVersion(getContext());
        if (version == null) {
            Log.i(TAG, "init: verison is null");
            dismiss();
            return;
        }


        mMessage.setText(version.getDescription(getContext()));

        if (version.isForceUpdate()) {
            mCancelView.setVisibility(View.GONE);
            mIgnore.setVisibility(View.GONE);
            mSubmitView.setBackgroundResource(R.drawable.white_alpha_0_9_5_corner_bottom_16dp_bg);
        } else {
            mIgnore.setVisibility(View.VISIBLE);
            mCancelView.setVisibility(View.VISIBLE);
            mCancelView.setBackgroundResource(R.drawable.white_alpha_0_9_5_corner_bottom_left_16dp_bg);
            mSubmitView.setBackgroundResource(R.drawable.white_alpha_0_9_5_corner_bottom_right_16dp_bg);

        }

    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window == null) {
            LogManager.i(TAG, "window is null");
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (DeviceUtils.getScreenSize()[0] * 0.8f);
        window.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                ignoreVersion();
                dismiss();
                break;
            case R.id.submit:
                Version version = VersionManager.getInstance().getVersion(getContext());
                IntentUtils.goToBrowser(getContext(), version.getDownloadPath());
                dismiss();
                break;
        }
    }


    /**
     * 忽略当前版本
     */
    private void ignoreVersion() {
        SharedPreferencesUtils.save(getContext(), Version.Key.IGNORE_NOW, true);

        if (!mIgnore.isChecked()) {
            return;
        }

        Version version = VersionManager.getInstance().getVersion(getContext());
        if (version == null) {
            Log.i(TAG, "init: verison is null");
            return;
        }

        SharedPreferencesUtils.save(getContext(), Version.Key.IGNORE_VERSION, JSON.toJSONString(version));
    }
}
