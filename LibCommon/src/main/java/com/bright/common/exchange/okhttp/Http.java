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

import com.bright.common.exchange.okhttp.executor.Executor;
import com.bright.common.exchange.okhttp.request.GetRequest;

import okhttp3.OkHttpClient;

/**
 * Created by Bright.Yu on 2017/2/20.
 * 请求的封装
 */

public class Http {
    private static Http mInstance;
    private OkHttpClient mOkHttpClient;
    private Executor mExecutor;

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

    public void init() {

    }

    public OkHttpClient getHttpClient() {
        return mOkHttpClient;
    }

    /**
     * @return
     */
    public GetRequest get() {
        return new GetRequest();
    }

    public Executor getExecutor() {
        return mExecutor;
    }
}
