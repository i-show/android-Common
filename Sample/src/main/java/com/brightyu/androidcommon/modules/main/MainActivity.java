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

package com.brightyu.androidcommon.modules.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.bright.common.app.activity.BaseActivity;
import com.bright.common.utils.log.L;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.YToast;
import com.brightyu.androidcommon.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private long mLastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHandler.sendEmptyMessageDelayed(1, 2000);
        mHandler.sendEmptyMessageDelayed(2, 4000);
        mHandler.sendEmptyMessageDelayed(3, 6000);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        try {
            Intent intent = new Intent("com.yuhaiyang.androidcommon.Test");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i(TAG, "onRightClick: activity is not found");
        }

    }


    @Override
    public void onBackPressed() {
        final long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 2000) {
            super.onBackPressed();
        } else {
            mLastTime = nowTime;
            YToast.show(this, R.string.click_again_to_exit);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "handleMessage: msg.what = " + msg.what);
            switch (msg.what) {
                case 1:
                    L.i(TAG, "has message 1" + mHandler.hasMessages(1));
                    L.i(TAG, "has message 1" + mHandler.hasMessages(2));
                    L.i(TAG, "has message 1" + mHandler.hasMessages(3));
                    break;
                case 2:
                    L.i(TAG, "has message 2" + mHandler.hasMessages(1));
                    L.i(TAG, "has message 2" + mHandler.hasMessages(2));
                    L.i(TAG, "has message 2" + mHandler.hasMessages(3));
                    break;
                case 3:
                    L.i(TAG, "has message 3" + mHandler.hasMessages(1));
                    L.i(TAG, "has message 3" + mHandler.hasMessages(2));
                    L.i(TAG, "has message 3" + mHandler.hasMessages(3));
                    break;
            }
        }
    };
}



