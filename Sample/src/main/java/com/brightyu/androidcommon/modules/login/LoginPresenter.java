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

package com.brightyu.androidcommon.modules.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bright.common.utils.SharedPreferencesUtils;
import com.brightyu.androidcommon.entries.User;
import com.brightyu.androidcommon.manager.UserManager;

import java.lang.ref.WeakReference;

/**
 * 登录的Presenter
 */
public class LoginPresenter implements LoginContract.Presenter, UserManager.LoginCallBack {
    private LoginContract.View mLoginView;
    private Context mContext;
    private UserManager mLoginManager;



    public LoginPresenter(Context context, @NonNull LoginContract.View loginView) {
        mLoginView = loginView;
        mContext = context;
        mLoginManager = UserManager.getInstance(context);
        mLoginManager.setLoginCallBack(this);
    }



    @Override
    public void login(String account, String password) {
        String errorMessage = UserManager.checkAccount(mContext, account);
        if (!TextUtils.isEmpty(errorMessage)) {
            mLoginView.showLoginFail(errorMessage);
            return;
        }

        errorMessage = UserManager.checkPassword(mContext, password);
        if (!TextUtils.isEmpty(errorMessage)) {
            mLoginView.showLoginFail(errorMessage);
            return;
        }

        saveUserInfo(account, password);

        mLoginView.showLoging();
        mLoginManager.login(account, password);
    }

    @Override
    public void start() {
        String account = SharedPreferencesUtils.get(mContext, User.Key.ACCOUNT, null);
        String password = SharedPreferencesUtils.get(mContext, User.Key.PASSWORD, null);
        mLoginView.updateUI(false, account, password);
    }

    @Override
    public void onSuccess() {
        mLoginView.showLoginSuccess();
    }

    @Override
    public void onError(Exception e, String message, int type) {
        mLoginView.showLoginFail(message);
    }

    /**
     * 保存用户信息
     */
    private void saveUserInfo(String account, String password) {
        SharedPreferencesUtils.save(mContext, User.Key.ACCOUNT, account);
        SharedPreferencesUtils.save(mContext, User.Key.PASSWORD, password);
    }
}
