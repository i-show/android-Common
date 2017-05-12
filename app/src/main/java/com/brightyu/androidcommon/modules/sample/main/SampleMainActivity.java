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

package com.brightyu.androidcommon.modules.sample.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;
import com.brightyu.androidcommon.modules.sample.edittextpro.SampleEditTextProActivity;
import com.brightyu.androidcommon.modules.sample.http.SampleHttpActivity;
import com.brightyu.androidcommon.modules.sample.imageloader.SampleImageLoaderActivity;
import com.brightyu.androidcommon.modules.sample.permission.SamplePermissionActivity;
import com.brightyu.androidcommon.modules.sample.photo.select.SampleSelectPhotoActivity;
import com.brightyu.androidcommon.modules.sample.pickview.SamplePickerActivity;
import com.ishow.common.widget.pulltorefresh.XListView;

/**
 * 测试Demo
 */
public class SampleMainActivity extends AppBaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        // 滚轮选择器
        View view = findViewById(R.id.sample_pick_view);
        view.setOnClickListener(this);
        // 图片选择器
        view = findViewById(R.id.sample_select_photo);
        view.setOnClickListener(this);
        // 权限设置
        view = findViewById(R.id.sample_select_permission);
        view.setOnClickListener(this);
        // EditTextPro
        view = findViewById(R.id.sample_select_edittextpro);
        view.setOnClickListener(this);
        // Http
        view = findViewById(R.id.sample_select_http);
        view.setOnClickListener(this);

        // ImageLoader
        view = findViewById(R.id.sample_select_imageloader);
        view.setOnClickListener(this);


        TestAdapter adapter = new TestAdapter(this);
        XListView listView = (XListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.sample_pick_view:
                intent = new Intent(this, SamplePickerActivity.class);
                startActivity(intent);
                break;
            case R.id.sample_select_photo:
                intent = new Intent(this, SampleSelectPhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.sample_select_permission:
                intent = new Intent(this, SamplePermissionActivity.class);
                startActivity(intent);
                break;
            case R.id.sample_select_edittextpro:
                intent = new Intent(this, SampleEditTextProActivity.class);
                startActivity(intent);
                break;
            case R.id.sample_select_http:
                intent = new Intent(this, SampleHttpActivity.class);
                startActivity(intent);
                break;
            case R.id.sample_select_imageloader:
                intent = new Intent(this, SampleImageLoaderActivity.class);
                startActivity(intent);
                break;
        }
    }

}
