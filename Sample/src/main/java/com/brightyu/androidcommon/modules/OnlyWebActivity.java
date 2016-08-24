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

package com.brightyu.androidcommon.modules;

import android.os.Bundle;
import android.webkit.WebView;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;

/**
 * Created by Bright.Yu on 2016/8/9.
 */
public class OnlyWebActivity extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_web);
        WebView web = (WebView) findViewById(R.id.web);
        web.loadUrl("http://ulivetest.ahgxtx.com:8070/weixin/cctv5Live?channelType=CCTV&channelNameMapping=CCTV5HD&openId=o_uqQuNUXF0qr6pXKMkF8R7s9Pdg");
    }
}
