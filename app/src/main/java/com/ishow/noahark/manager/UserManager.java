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

package com.ishow.noahark.manager;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.RegexValidateUtils;
import com.ishow.common.utils.SharedPreferencesUtils;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.noahark.R;
import com.ishow.noahark.constant.Url;
import com.ishow.noahark.entries.User;
import com.ishow.noahark.utils.http.AppHttpCallBack;

import java.lang.ref.WeakReference;
import java.util.Random;

public class UserManager {
    private static final String TAG = "UserManager";

    private static UserManager sInstance;

    private WeakReference<User> mUser;

    // 暂时用来模拟登录
    private Handler mHandler = new Handler();

    private UserManager() {
    }


    public static UserManager getInstance() {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager();
                }
            }
        }
        return sInstance;
    }

    public User getUser(Context context) {
        if (mUser == null || mUser.get() == null) {
            String jsonString = SharedPreferencesUtils.get(context, User.Key.KEY_CACHE_USER, null, true);
            if (TextUtils.isEmpty(jsonString)) {
                Log.i(TAG, "getUser: no user");
                return null;
            }
            User user = JSON.parseObject(jsonString, User.class);
            mUser = new WeakReference<>(user);
        }

        return mUser.get();
    }


    /**
     * 登录
     *
     * @param account  用户名
     * @param password 密码
     */
    public void login(Context context, String account, String password, final LoginCallBack callBack) {
        login(context, account, password, false, callBack);
    }

    /**
     * 登录
     *
     * @param account   用户名
     * @param password  密码
     * @param autoLogin 是否是自动登录
     */
    public void login(final Context context, String account, String password, boolean autoLogin, final LoginCallBack callBack) {

        Http.post()
                .url(Url.login())
                .addParams("phone", account)
                .addParams("password", password)
                .execute(new AppHttpCallBack<String>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        callBack.onError(error.getException(), error.getMessage(), error.getCode());
                    }

                    @Override
                    protected void onSuccess(String result) {
                        callBack.onSuccess();
                    }
                });

    }

    /**
     * 检测账户是否有效
     */
    public static String checkAccount(Context context, String account) {
        if (TextUtils.isEmpty(account)) {
            return context.getString(R.string.login_please_input_account);
        }
        if (!RegexValidateUtils.checkMobileNumber(account)) {
            return context.getString(R.string.login_please_input_correct_account);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 检测密码是否有效
     */
    public static String checkPassword(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            return context.getString(R.string.login_please_input_password);
        }

        int length = password.length();
        int min = context.getResources().getInteger(R.integer.min_password);
        int max = context.getResources().getInteger(R.integer.max_password);
        if (length < min || length > max) {
            return context.getString(R.string.login_please_input_correct_password, String.valueOf(min), String.valueOf(max));
        }

        return StringUtils.EMPTY;
    }

    /**
     * 检测再次输入的密码是否有效
     */
    public static String checkEnsurePassword(Context context, String password, String ensurePassword) {
        if (TextUtils.isEmpty(ensurePassword)) {
            return context.getString(R.string.please_input_ensure_password);
        }

        if (!TextUtils.equals(password, ensurePassword)) {
            return context.getString(R.string.please_input_right_ensure_password);
        }

        return StringUtils.EMPTY;
    }

    public boolean isAutoLogin(Context context) {
        return SharedPreferencesUtils.get(context, User.Key.AUTO_LOGIN, false);
    }

    public interface LoginCallBack {
        /**
         * 登录成功
         */
        void onSuccess();

        /**
         * @param e       失败信息
         * @param message 失败信息
         * @param type    失败类型（看后台有没有这个东西了）
         */
        void onError(Exception e, String message, int type);
    }
}
