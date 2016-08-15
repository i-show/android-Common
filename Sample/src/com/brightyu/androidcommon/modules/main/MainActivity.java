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

package com.brightyu.androidcommon.modules.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bright.common.app.BaseActivity;
import com.bright.common.widget.TopBar;
import com.brightyu.androidcommon.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        topBar.setOnSecretListener(new TopBar.OnSecretListener() {
            @Override
            public void onSecretClick(View v, int count) {
                Log.i(TAG, "onSecretClick:  count = " + count);
            }
        });
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
        Log.i(TAG, "onLeftClick: id = " + v.getId());
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        Log.i(TAG, "onRightClick: id = " + v.getId());
    }

    @Override
    public void onTitleClick(View v) {
        super.onTitleClick(v);
        Log.i(TAG, "onTitleClick: id = " + v.getId());
    }
}
