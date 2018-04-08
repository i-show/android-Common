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

package com.ishow.noah.modules.sample.webview.loading;

import android.os.Bundle;
import android.webkit.WebView;

import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseActivity;

/**
 * Created by yuhaiyang on 2017/6/26.
 * LoadingWebView测试
 */

public class SampleLoadingWebViewActivity extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_loading_webview);
    }

    @Override
    protected void initViews() {
        super.initViews();
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("http://www.qq.com/");
    }
}
