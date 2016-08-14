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

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.bright.common.utils.RegexValidateUtils;
import com.bright.common.utils.Utils;
import com.brightyu.androidcommon.R;

import java.util.Random;

/**
 * 登录管理器
 */
public class LoginManager {
    private static final String TAG = "LoginManager";
    private Context mContext;
    private CallBack mCallBack;
    private static LoginManager sInstance;

    // 暂时用来模拟登录
    private Handler mHandler;

    private LoginManager(Context context) {
        mContext = context;
        mHandler = new Handler();
    }


    public static LoginManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LoginManager.class) {
                if (sInstance == null) {
                    sInstance = new LoginManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
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

    public static String checkAccount(Context context, String account) {
        if (TextUtils.isEmpty(account)) {
            return context.getString(R.string.login_please_input_account);
        }
        if (!RegexValidateUtils.checkMobileNumber(account)) {
            return context.getString(R.string.login_please_input_correct_account);
        }
        return Utils.EMPTY;
    }

    public static String checkPassword(Context context, String password) {
        if (TextUtils.isEmpty(password)) {
            return context.getString(R.string.login_please_input_password);
        }

        int length = password.length();
        int min = context.getResources().getInteger(R.integer.min_password);
        int max = context.getResources().getInteger(R.integer.max_password);
        if (length < min || length > max) {
            return context.getString(R.string.login_please_input_correct_password, min, max);
        }

        return Utils.EMPTY;
    }

    public static String checkEnsurePassword(Context context, String password, String ensurePassword) {
        if (TextUtils.isEmpty(ensurePassword)) {
            return context.getString(R.string.login_please_input_ensure_password);
        }

        if (!TextUtils.equals(password, ensurePassword)) {
            return context.getString(R.string.register_please_input_right_ensure_password);
        }

        return Utils.EMPTY;
    }


    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public interface CallBack {
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
