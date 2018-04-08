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

package com.ishow.noah.modules.account.register;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.noah.R;
import com.ishow.noah.constant.Url;
import com.ishow.noah.manager.UserManager;
import com.ishow.noah.utils.http.AppHttpCallBack;

import java.util.Random;

/**
 * 注册的Presenter
 */
class RegisterPresenter implements RegisterContract.Presenter {


    //  用来模拟网络连接的Handler
    private Handler mHandler = new Handler();
    private RegisterContract.View mView;

    RegisterPresenter(RegisterContract.View view) {
        mView = view;
    }


    @Override
    public void register(Context context, String name, String verifyCode, String password, String ensurePassword) {
        String errorMessage = UserManager.checkAccount(context, name);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(errorMessage, true, 0);
            return;
        }

        if (TextUtils.isEmpty(verifyCode)) {
            mView.showError(context.getString(R.string.register_please_input_verify_code), true, 0);
            return;
        }

        errorMessage = UserManager.checkPassword(context, password);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(errorMessage, true, 0);
            return;
        }

        errorMessage = UserManager.checkEnsurePassword(context, password, ensurePassword);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(errorMessage, true, 0);
            return;
        }

        mView.showLoading(null, true);

        JSONObject params = new JSONObject();
        params.put("verifyCode", verifyCode);
        params.put("phone", name);
        params.put("password", password);

        Http.post()
                .url(Url.register())
                .params(params.toString())
                .execute(new AppHttpCallBack<String>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        mView.dismissLoading(true);
                        mView.showError(error.getMessage(), true, 1);
                    }

                    @Override
                    protected void onSuccess(String result) {
                        mView.dismissLoading(true);
                        mView.showSuccess(null);
                    }
                });
    }

    @Override
    public void sendVerifiyCode(Context context, String phoneNumber) {
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
