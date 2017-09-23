/*
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

package com.ishow.noahark.modules.init;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.ishow.noahark.manager.UserManager;
import com.ishow.noahark.modules.account.login.LoginActivity;
import com.ishow.common.constant.Shift;
import com.ishow.noahark.constant.Configure;
import com.ishow.noahark.manager.VersionManager;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.main.MainActivity;

/**
 * 引导界面
 */
public class SplashActivity extends AppBaseActivity {
    private static final int HANDLE_GO_NEXT = 1 << Shift.ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new RecycleHandler();
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        // 百度统计
        StatService.start(this);
        // 更新版本信息
        VersionManager.getInstance().init(this);
    }

    /**
     * 是否需要显示升级dialog
     */
    protected boolean needShowUpdateVersionDialog() {
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHandler.sendEmptyMessageDelayed(HANDLE_GO_NEXT, Configure.SPLASH_TIME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeMessages(HANDLE_GO_NEXT);
    }

    @Override
    protected void resetStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View rootView = getWindow().getDecorView();
            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }


    /**
     * 父类中自动回收Handler机制
     */
    @SuppressLint("HandlerLeak")
    private class RecycleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_GO_NEXT:
                    Intent intent;
                    if (UserManager.getInstance().isAutoLogin(SplashActivity.this)) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }
}
