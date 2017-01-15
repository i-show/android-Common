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

package com.brightyu.androidcommon.modules.sample.photo.select;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bright.common.utils.image.select.SelectPhotoUtils;
import com.bright.common.widget.TopBar;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;

/**
 * Created by Bright.Yu on 2017/1/15.
 * 选择图片
 */

public class SampleSelectPhotoActivity extends AppBaseActivity implements View.OnClickListener {

    private SelectPhotoUtils mSelectPhotoUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_select_photo);
        mSelectPhotoUtils = new SelectPhotoUtils(this, SelectPhotoUtils.SelectMode.SINGLE);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);

        View single = findViewById(R.id.sample_select_photo_single);
        single.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sample_select_photo_single:
                mSelectPhotoUtils.select(1, 1);
                break;
        }
    }
}
