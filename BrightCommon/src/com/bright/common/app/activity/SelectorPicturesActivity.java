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

package com.bright.common.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bright.common.R;
import com.bright.common.app.BaseActivity;
import com.bright.common.app.fragment.SelectorPicturesFragment;
import com.bright.common.model.SelectorPicture;
import com.bright.common.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 多图选择
 */
public class SelectorPicturesActivity extends BaseActivity implements View.OnClickListener, SelectorPicturesFragment.Callback {
    private static final String TAG = "MultiImageActivity";
    private ArrayList<String> mResultList = new ArrayList<>();
    private String mResultString;
    private int mDefaultCount;
    private int mMode;

    private TextView mCompleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 多选照片的主题如果要更新其中的东西请在App里面复写此主题
        setTheme(R.style.Theme_MultiSelectorImage);
        setContentView(R.layout.activity_multi_choice_pictures);
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(SelectorPicture.Key.EXTRA_SELECT_COUNT, SelectorPicture.Key.DEAFULT_MAX_COUNT);
        mMode = intent.getIntExtra(SelectorPicture.Key.EXTRA_SELECT_MODE, SelectorPicture.Key.MODE_MULTI);
        if (mMode == SelectorPicture.Key.MODE_MULTI && intent.hasExtra(SelectorPicture.Key.EXTRA_DEFAULT_SELECTED_LIST)) {
            mResultList = intent.getStringArrayListExtra(SelectorPicture.Key.EXTRA_DEFAULT_SELECTED_LIST);
        }

        Bundle bundle = new Bundle();
        bundle.putInt(SelectorPicture.Key.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(SelectorPicture.Key.EXTRA_SELECT_MODE, mMode);
        bundle.putStringArrayList(SelectorPicture.Key.EXTRA_DEFAULT_SELECTED_LIST, mResultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.content, SelectorPicturesFragment.newInstance(bundle))
                .commit();
    }

    @Override
    protected void initViews() {
        super.initViews();
        // 返回
        ImageView back = (ImageView) findViewById(R.id.back);
        DrawerArrowDrawable icon = new DrawerArrowDrawable(this);
        icon.setColor(Color.WHITE);
        icon.setProgress(1);
        back.setImageDrawable(icon);
        back.setOnClickListener(this);

        // 完成按钮
        mCompleteButton = (TextView) findViewById(R.id.complete);
        mCompleteButton.setOnClickListener(this);
        updateComplete();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        // Lib中不能使用switch
        if (id == R.id.back) {
            setResult(RESULT_CANCELED);
            finish();
        } else if (id == R.id.complete) {
            setResult();
        }
    }

    @Override
    public void onSingleImageSelected(String path) {
        mResultString = path;

        updateComplete();
    }

    @Override
    public void onImageSelectedStateChanged(String path, boolean selected) {
        if (selected) {
            mResultList.add(path);
        } else {
            mResultList.remove(path);
        }
        updateComplete();
    }


    @Override
    public void onCameraShot(File file) {
        if (file == null) {
            Log.i(TAG, "onCameraShot: file is null");
            return;
        }
        Intent data = new Intent();
        mResultList.add(file.getAbsolutePath());
        data.putStringArrayListExtra(SelectorPicture.Key.EXTRA_RESULT, mResultList);
        setResult(RESULT_OK, data);
        finish();
    }


    private void setResult() {
        switch (mMode) {
            case SelectorPicture.Key.MODE_SINGLE:
                if (TextUtils.isEmpty(mResultString)) {
                    Log.i(TAG, "setResult: mResultString is null or empty");
                    return;
                }

                // 返回已选择的图片数据
                Intent singleData = new Intent();
                singleData.putExtra(SelectorPicture.Key.EXTRA_RESULT, mResultString);
                setResult(RESULT_OK, singleData);
                finish();
                break;
            case SelectorPicture.Key.MODE_MULTI:
                if (mResultList == null || mResultList.size() == 0) {
                    Log.i(TAG, "setResult: mResultList is null or empty");
                    return;
                }

                // 返回已选择的图片数据
                Intent multiData = new Intent();
                multiData.putStringArrayListExtra(SelectorPicture.Key.EXTRA_RESULT, mResultList);
                setResult(RESULT_OK, multiData);
                finish();

                break;
        }
    }

    /**
     * 更新完成按钮
     */
    private void updateComplete() {

        switch (mMode) {
            case SelectorPicture.Key.MODE_SINGLE:
                mCompleteButton.setText(R.string.complete);
                if (TextUtils.isEmpty(mResultString)) {
                    mCompleteButton.setEnabled(false);
                } else {
                    mCompleteButton.setEnabled(true);
                }
                break;
            case SelectorPicture.Key.MODE_MULTI:
                if (mResultList == null || mResultList.size() <= 0) {
                    mCompleteButton.setText(R.string.complete);
                    mCompleteButton.setEnabled(false);
                } else {
                    String count = StringUtils.plusString("(", mResultList.size(), "/", mDefaultCount + ")");
                    mCompleteButton.setText(getString(R.string.link_complete, count));
                    mCompleteButton.setEnabled(true);
                }
                break;
        }


    }


}
