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

package com.ishow.noah.manager


import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.ishow.common.utils.RegexValidateUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.StringUtils
import com.ishow.noah.R
import com.ishow.noah.entries.UserContainer

/**
 * Created by yuhaiyang on 2018/8/8.
 * 用户信息管理
 */
class UserManager private constructor() {

    private var mUserContainer: UserContainer? = null


    /**
     * 设置UserContainer
     */
    fun setUserContainer(context: Context, container: UserContainer?) {
        mUserContainer = container
        StorageUtils.with(context)
                .param(UserContainer.Key.CACHE, JSON.toJSONString(container))
                .save()
    }

    /**
     * 获取用户信息
     */
    fun getUserContainer(context: Context?): UserContainer? {
        if(context == null){
            return mUserContainer
        }

        if (mUserContainer == null) {
            val cache = StorageUtils.with(context)
                    .key(UserContainer.Key.CACHE)
                    .get(StringUtils.EMPTY)

            if (TextUtils.isEmpty(cache)) {
                Log.i(TAG, "getUser: no user")
                return null
            }
            mUserContainer = JSON.parseObject(cache, UserContainer::class.java)
        }
        return mUserContainer
    }


    /**
     * 获取头像
     */
    fun getAvatar(context: Context): String? {
        if (mUserContainer == null) {
            val cache = StorageUtils.with(context)
                    .key(UserContainer.Key.CACHE)
                    .get()

            if (TextUtils.isEmpty(cache)) {
                Log.i(TAG, "getUser: no user")
                return null
            }
            mUserContainer = JSON.parseObject(cache, UserContainer::class.java)
        }
        return if (mUserContainer == null) {
            StringUtils.EMPTY
        } else mUserContainer!!.user.avatar

    }

    /**
     * 获取Accessoken
     */
    fun getAccessToken(context: Context): String {
        val userContainer = getUserContainer(context) ?: return StringUtils.EMPTY
        val token = userContainer.token ?: return StringUtils.EMPTY
        return token.accessToken
    }

    companion object {
        private val TAG = "UserManager"

        @Volatile
        private var sInstance: UserManager? = null


        val instance: UserManager
            get() {
                if (sInstance == null) {
                    synchronized(UserManager::class.java) {
                        if (sInstance == null) {
                            sInstance = UserManager()
                        }
                    }
                }
                return sInstance!!
            }

        /**
         * 检测账户是否有效
         */
        fun checkAccount(context: Context, account: String): String {
            if (TextUtils.isEmpty(account)) {
                return context.getString(R.string.login_please_input_account)
            }
            return if (!RegexValidateUtils.checkMobileNumber(account)) {
                context.getString(R.string.login_please_input_correct_account)
            } else StringUtils.EMPTY
        }

        /**
         * 检测密码是否有效
         */
        fun checkPassword(context: Context, password: String): String {
            if (TextUtils.isEmpty(password)) {
                return context.getString(R.string.login_please_input_password)
            }

            val length = password.length
            val min = context.resources.getInteger(R.integer.min_password)
            val max = context.resources.getInteger(R.integer.max_password)
            return if (length < min || length > max) {
                context.getString(R.string.login_please_input_correct_password, min.toString(), max.toString())
            } else StringUtils.EMPTY

        }

        /**
         * 检测再次输入的密码是否有效
         */
        fun checkEnsurePassword(context: Context, password: String, ensurePassword: String): String {
            if (TextUtils.isEmpty(ensurePassword)) {
                return context.getString(R.string.please_input_ensure_password)
            }

            return if (!TextUtils.equals(password, ensurePassword)) {
                context.getString(R.string.please_input_right_ensure_password)
            } else StringUtils.EMPTY
        }
    }
}
