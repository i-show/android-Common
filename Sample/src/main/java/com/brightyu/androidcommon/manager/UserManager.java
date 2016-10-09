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

package com.brightyu.androidcommon.manager;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bright.common.utils.RegexValidateUtils;
import com.bright.common.utils.SharedPreferencesUtils;
import com.bright.common.utils.Utils;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.entries.User;

import java.lang.ref.WeakReference;
import java.util.Random;

public class UserManager {
    private static final String TAG = "UserManager";

    private Context mContext;
    private LoginCallBack mCallBack;
    private static UserManager sInstance;

    private WeakReference<User> mUser;

    // 暂时用来模拟登录
    private Handler mHandler;

    private UserManager(Context context) {
        mContext = context;
        mHandler = new Handler();
    }


    public static UserManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (UserManager.class) {
                if (sInstance == null) {
                    sInstance = new UserManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public User getUser() {
        if (mUser == null || mUser.get() == null) {
            String jsonString = SharedPreferencesUtils.get(mContext, User.Key.KEY_CACHE_USER, null);
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
    public void login(String account, String password) {
        login(account, password, false);
    }

    /**
     * 登录
     *
     * @param account   用户名
     * @param password  密码
     * @param autoLogin 是否是自动登录
     */
    public void login(String account, String password, boolean autoLogin) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCallBack == null) {
                    Log.i(TAG, "run: call back is null");
                    return;
                }
                int result = new Random().nextInt() % 5;
                if (result == 1) {
                    mCallBack.onError(new NetworkErrorException("Net"), "用户名或密码不对（模拟）", 1);
                } else {
                    mCallBack.onSuccess();
                }
            }
        }, 1000);
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
        return Utils.EMPTY;
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

        return Utils.EMPTY;
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

        return Utils.EMPTY;
    }

    /**
     * 设置登录的CallBack
     */
    public void setLoginCallBack(LoginCallBack callBack) {
        mCallBack = callBack;
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
