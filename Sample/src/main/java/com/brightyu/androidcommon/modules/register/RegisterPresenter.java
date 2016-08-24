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

package com.brightyu.androidcommon.modules.register;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.login.LoginManager;

import java.util.Random;

/**
 * 注册的Presenter
 */
public class RegisterPresenter implements RegisterContract.Presenter {

    private RegisterContract.View mView;
    private Context mContext;

    //  用来模拟网络连接的Handler
    private Handler mHandler;

    public RegisterPresenter(Context context, RegisterContract.View view) {
        mContext = context;
        mView = view;
        mHandler = new Handler();
    }

    @Override
    public void start() {
        // TODO nothing
    }

    @Override
    public void register(String name, String verifyCode, String password, String ensurePassword) {
        String errorMessage = LoginManager.checkAccount(mContext, name);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showRegisterFail(errorMessage);
            return;
        }

        if (TextUtils.isEmpty(verifyCode)) {
            mView.showRegisterFail(mContext.getString(R.string.register_please_input_verify_code));
            return;
        }

        errorMessage = LoginManager.checkPassword(mContext, password);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showRegisterFail(errorMessage);
            return;
        }

        errorMessage = LoginManager.checkEnsurePassword(mContext, password, ensurePassword);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showRegisterFail(errorMessage);
            return;
        }

        mView.showRegistering();

        // 模拟注册
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int result = new Random().nextInt();
                if (result % 2 == 0) {
                    mView.showRegisterSuccess();
                } else {
                    mView.showRegisterFail("手机号码已经注册过，请直接登录");
                }
            }
        }, 3000);
    }

    @Override
    public void sendVerifiyCode() {
        // 模拟发送验证码
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                int result = new Random().nextInt();
                if (result % 2 == 0) {
                    mView.showSendVerifySuccess();
                } else {
                    mView.showSendVerifyFail("请求超时");
                }
            }
        }, 3000);
    }
}
