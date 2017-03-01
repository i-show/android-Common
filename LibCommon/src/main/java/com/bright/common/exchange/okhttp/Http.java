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

package com.bright.common.exchange.okhttp;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.bright.common.exchange.okhttp.config.HttpConfig;
import com.bright.common.exchange.okhttp.executor.Executor;
import com.bright.common.exchange.okhttp.executor.OkhttpExecutor;
import com.bright.common.exchange.okhttp.request.GetRequest;
import com.bright.common.exchange.okhttp.request.PostRequest;


/**
 * Created by Bright.Yu on 2017/2/20.
 * 请求的封装
 */

public class Http {
    private static Http mInstance;
    private Executor mExecutor;
    private HttpConfig mHttpConfig;
    private Resources mResources;

    private Http() {
    }

    public static Http getInstance() {
        if (mInstance == null) {
            synchronized (Http.class) {
                if (mInstance == null) {
                    mInstance = new Http();
                }
            }
        }
        return mInstance;
    }

    public void init(@NonNull Context context) {
        init(context, new OkhttpExecutor(), HttpConfig.getDefaultConfig());
    }

    public void init(@NonNull Context context, @NonNull Executor executor) {
        init(context, executor, HttpConfig.getDefaultConfig());
    }

    public void init(@NonNull Context context, @NonNull HttpConfig config) {
        init(context, new OkhttpExecutor(), config);
    }

    public void init(@NonNull Context context, @NonNull Executor executor, @NonNull HttpConfig config) {
        mExecutor = executor;
        mExecutor.init();
        mHttpConfig = config;
        mResources = context.getApplicationContext().getResources();
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

    public static HttpConfig getConfig() {
        return getInstance().mHttpConfig;
    }

    public static Resources getResources() {
        return getInstance().mResources;
    }
}
