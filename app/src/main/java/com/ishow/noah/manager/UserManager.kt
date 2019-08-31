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
import com.google.gson.Gson
import com.ishow.common.utils.RegexValidateUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.StringUtils
import com.ishow.noah.R
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.utils.http.okhttp.interceptor.AppHttpInterceptor

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
        AppHttpInterceptor.token = container?.token?.accessToken
        mUserContainer = container

        StorageUtils.with(context)
            .param(UserContainer.Key.CACHE, JSON.toJSONString(container))
            .save()
    }

    /**
     * 获取用户信息
     */
    fun getUserContainer(
        context: Context?,
        block: ((UserContainer) -> Unit)? = null
    ): UserContainer? {
        if (context == null) {
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
            mUserContainer = Gson().fromJson(cache, UserContainer::class.java)
        }

        if (mUserContainer != null) {
            block?.let { it(mUserContainer!!) }
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
        } else mUserContainer!!.user?.avatar
    }


    /**
     * 获取AccessToken
     */
    fun getAccessToken(context: Context): String? {
        val userContainer = getUserContainer(context) ?: return StringUtils.EMPTY
        val token = userContainer.token ?: return StringUtils.EMPTY
        return token.accessToken
    }

    companion object {
        private const val TAG = "UserManager"

        @Volatile
        private var sInstance: UserManager? = null


        val instance: UserManager
            get() =
                sInstance ?: synchronized(UserManager::class.java) {
                    sInstance
                        ?: UserManager().also { sInstance = it }
                }

        @JvmStatic
        fun setAvatar(context: Context, avatar: String) {
            val manager = instance
            manager.getUserContainer(context) {
                it.user?.avatar = avatar
                manager.setUserContainer(context, it)
            }
        }
    }

}
