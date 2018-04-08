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

package com.ishow.noah.manager;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.RegexValidateUtils;
import com.ishow.common.utils.StorageUtils;
import com.ishow.common.utils.StringUtils;
import com.ishow.noah.R;
import com.ishow.noah.entries.Token;
import com.ishow.noah.entries.UserContainer;

public class UserManager {
    private static final String TAG = "UserManager";

    private static UserManager sInstance;

    private UserContainer mUserContainer;

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


    /**
     * 设置UserContainer
     */
    public void setUserContainer(Context context, UserContainer container){
        mUserContainer = container;
        StorageUtils.with(context)
                .param(UserContainer.Key.CACHE, JSON.toJSONString(container))
                .save();
    }

    /**
     * 获取用户信息
     */
    @SuppressWarnings("WeakerAccess")
    public UserContainer getUserContainer(Context context){
        if(mUserContainer == null){
            String cache = StorageUtils.with(context)
                    .key(UserContainer.Key.CACHE)
                    .get(null);

            if (TextUtils.isEmpty(cache)) {
                Log.i(TAG, "getUser: no user");
                return null;
            }
            mUserContainer = JSON.parseObject(cache, UserContainer.class);
        }
        return mUserContainer;
    }


    /**
     * 获取头像
     */
    public String getAvatar(Context context){
        if(mUserContainer == null){
            String cache = StorageUtils.with(context)
                    .key(UserContainer.Key.CACHE)
                    .get(null);

            if (TextUtils.isEmpty(cache)) {
                Log.i(TAG, "getUser: no user");
                return null;
            }
            mUserContainer = JSON.parseObject(cache, UserContainer.class);
        }
        if(mUserContainer == null){
            return StringUtils.EMPTY;
        }

        return mUserContainer.getUser().getAvatar();
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

    /**
     * 获取Accessoken
     */
    public String getAccessToken(Context context){
        UserContainer userContainer = getUserContainer(context);
        if(userContainer == null){
            return StringUtils.EMPTY;
        }
        Token token = userContainer.getToken();
        if(token == null){
            return StringUtils.EMPTY;
        }
        return token.getAccessToken();
    }
}
