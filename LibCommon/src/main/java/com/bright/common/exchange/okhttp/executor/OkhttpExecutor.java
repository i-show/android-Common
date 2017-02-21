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

package com.bright.common.exchange.okhttp.executor;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Okhttp的请求
 */

public class OkhttpExecutor extends Executor {
    private OkHttpClient mOkHttpClient;

    @Override
    public void init() {
        mOkHttpClient = new OkHttpClient.Builder()
                .build();
    }

    @Override
    public void execute(com.bright.common.exchange.okhttp.request.Request request) {
        switch (request.getMethod()) {
            case GET:
                executeGet(request);
                break;
        }
    }

    /**
     * Request Get
     */
    private void executeGet(com.bright.common.exchange.okhttp.request.Request request) {
        String url = request.getUrl();
        if (TextUtils.isEmpty(url)) {
            throw new IllegalStateException("need a url");
        }
        url = formatUrl(url, request.getParams());

        // 2. 设置Headers
        Map<String, String> headersMap = request.getHeaders();
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headersMap.keySet()) {
            headersBuilder.add(key, headersMap.get(key));
        }
        Headers headers = headersBuilder.build();

        Request okHttpRequest = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        mOkHttpClient.newCall(okHttpRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("nian", "onResponse: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("nian", "onResponse: ");
            }
        });
    }

}
