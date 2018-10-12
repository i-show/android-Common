/*
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

package com.ishow.noah.modules.account.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.utils.StorageUtils;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.noah.constant.Configure;
import com.ishow.noah.constant.Url;
import com.ishow.noah.entries.UserContainer;
import com.ishow.noah.manager.UserManager;
import com.ishow.noah.utils.http.AppHttpCallBack;

/**
 * 登录的Presenter
 */
class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;

    LoginPresenter(LoginContract.View view) {
        mView = view;
    }

    @Override
    public void start(Context context) {
        String account = StorageUtils.with(context)
                .key(UserContainer.Key.ACCOUNT)
                .get(StringUtils.EMPTY);

        mView.updateUI(account);
        clear(context);
    }

    @Override
    public void login(final Context context, String account, String password) {
        String errorMessage = UserManager.checkAccount(context, account);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(errorMessage, true, 0);
            return;
        }

        errorMessage = UserManager.checkPassword(context, password);
        if (!TextUtils.isEmpty(errorMessage)) {
            mView.showError(errorMessage, true, 0);
            return;
        }

        saveUserInfo(context, account);

        mView.showLoading(null, true);


        JSONObject params = new JSONObject();
        params.put("account", account);
        params.put("password", password);
        params.put("device", Configure.DEVICE);
        params.put("deviceModel", DeviceUtils.model());
        params.put("deviceVersion", DeviceUtils.version());

        Http.post()
                .url(Url.login())
                .params(params.toJSONString())
                .execute(new AppHttpCallBack<UserContainer>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        //mView.dismissLoading(true);
                        mView.showError(error.getMessage(), true, 0);
                    }

                    @Override
                    protected void onSuccess(UserContainer result) {
                        UserManager userManager = UserManager.getInstance();
                        userManager.setUserContainer(context, result);
                        //mView.dismissLoading(true);
                        //mView.showSuccess(null);
                    }
                });

    }


    /**
     * 保存用户信息
     */
    private void saveUserInfo(Context context, String account) {
        StorageUtils.with(context)
                .param(UserContainer.Key.ACCOUNT, account)
                .save();
    }


    private void clear(Context context) {
        StorageUtils.with(context)
                .key(UserContainer.Key.CACHE)
                .remove();
    }
}
