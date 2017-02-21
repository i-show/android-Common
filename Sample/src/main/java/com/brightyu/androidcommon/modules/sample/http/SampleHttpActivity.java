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

package com.brightyu.androidcommon.modules.sample.http;

import android.os.Bundle;
import android.view.View;

import com.bright.common.exchange.okhttp.Http;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;

/**
 * Created by bright.yu on 2017/2/21.
 * Http测试类
 */
public class SampleHttpActivity extends AppBaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_http);
        Http.getInstance().init();
    }

    @Override
    protected void initViews() {
        super.initViews();
        View view = findViewById(R.id.sample_select_http_get);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sample_select_http_get:
                testGet();
                break;
        }
    }

    private void testGet() {
        Http.get().url("https://www.baidu.com/")
                .execute();
    }
}
