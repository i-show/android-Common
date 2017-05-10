/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.common.utils.http.rest;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.config.HttpConfig;
import com.ishow.common.utils.http.rest.executor.Executor;
import com.ishow.common.utils.http.rest.executor.OkhttpExecutor;
import com.ishow.common.utils.http.rest.request.GetRequest;
import com.ishow.common.utils.http.rest.request.PostRequest;


/**
 * Created by Bright.Yu on 2017/2/20.
 * 请求的封装
 */
@SuppressWarnings("unused")
public class Http {
    private static Http mInstance;
    private Executor mExecutor;
    private HttpConfig mHttpConfig;
    private Resources mResources;

    private Http() {
    }

    private static Http getInstance() {
        if (mInstance == null) {
            synchronized (Http.class) {
                if (mInstance == null) {
                    mInstance = new Http();
                }
            }
        }
        return mInstance;
    }

    public static void init(@NonNull Context context) {
        init(context, new OkhttpExecutor(), HttpConfig.getDefaultConfig());
    }

    public static void init(@NonNull Context context, @NonNull Executor executor) {
        init(context, executor, HttpConfig.getDefaultConfig());
    }

    public static void init(@NonNull Context context, @NonNull HttpConfig config) {
        init(context, new OkhttpExecutor(), config);
    }

    public static void init(@NonNull Context context, @NonNull Executor executor, @NonNull HttpConfig config) {
        Http http = Http.getInstance();
        http.mResources = context.getApplicationContext().getResources();
        http.mHttpConfig = config;
        // Warning： executor 必须在config后面初始化
        http.mExecutor = executor;
        http.mExecutor.init();
    }


    public static GetRequest get() {
        return new GetRequest();
    }

    public static PostRequest post() {
        return new PostRequest();
    }

    public static Executor getExecutor() {
        return getInstance().mExecutor;
    }

    /**
     * 获取配置信息
     */
    public static HttpConfig getConfig() {
        return getInstance().mHttpConfig;
    }

    /**
     * 获取Resources， 用来获取资源信息
     */
    public static Resources getResources() {
        return getInstance().mResources;
    }

    /**
     * 根据Tag来取消请求
     */
    public static void cancel(@NonNull Object tag) {
        if (getInstance().mExecutor != null) {
            getExecutor().cancle(tag);
        }
    }
}
