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

package com.brightyu.androidcommon.modules.account.login;

import android.content.Context;
import android.text.TextUtils;

import com.ishow.common.utils.SharedPreferencesUtils;
import com.brightyu.androidcommon.entries.User;
import com.brightyu.androidcommon.manager.UserManager;

/**
 * 登录的Presenter
 */
class LoginPresenter implements UserManager.LoginCallBack, LoginContract.Presenter {
    private UserManager mUserManager;
    private LoginContract.View mView;

    LoginPresenter(LoginContract.View view) {
        mView = view;
        mUserManager = UserManager.getInstance();
    }

    @Override
    public void login(Context context, String account, String password) {
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

        saveUserInfo(context, account, password);

        mView.showLoading(null, true);
        mUserManager.login(context, account, password, this);
    }


    @Override
    public void onSuccess() {
        mView.dismissLoading(true);
        mView.showSuccess(null);
    }

    @Override
    public void onError(Exception e, String message, int type) {
        mView.dismissLoading(true);
        mView.showError(message, true, 0);
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo(Context context, String account, String password) {
        SharedPreferencesUtils.save(context, User.Key.ACCOUNT, account);
        SharedPreferencesUtils.save(context, User.Key.PASSWORD, password);
    }

    @Override
    public void start(Context context) {
        String account = SharedPreferencesUtils.get(context, User.Key.ACCOUNT, null);
        String password = SharedPreferencesUtils.get(context, User.Key.PASSWORD, null);
        mView.updateUI(false, account, password);
        clear(context);
    }

    @Override
    public void stop(Context context) {

    }

    private void clear(Context context) {
        SharedPreferencesUtils.remove(context, User.Key.AUTO_LOGIN);
    }
}
