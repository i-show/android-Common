/**
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

package com.ishow.noah.modules.base;


import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ishow.common.utils.WebViewUtils;
import com.ishow.common.utils.log.LogManager;
import com.ishow.common.widget.TopBar;
import com.ishow.noah.BuildConfig;
import com.ishow.noah.R;

/**
 * Created by Bright.Yu on 2016/8/9.
 */
public class OnlyWebActivity extends AppBaseActivity {
    private static final String TAG = "OnlyWebActivity";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_CONTENT = "key_content";

    private String mTitleString;
    private String mUrl;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_web);
    }


    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        Intent intent = getIntent();
        mTitleString = intent.getStringExtra(KEY_TITLE);
        mUrl = intent.getStringExtra(KEY_CONTENT);
        if (BuildConfig.DEBUG) LogManager.i(TAG, "initNecessaryData: mUrl = " + mUrl);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
        topBar.setText(mTitleString);

        mWebView = (WebView) findViewById(R.id.web);
        WebViewUtils.init(this, mWebView);
        //载入js
        mWebView.loadUrl(mUrl);
        //载入js
        mWebView.setWebViewClient(new WebClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
