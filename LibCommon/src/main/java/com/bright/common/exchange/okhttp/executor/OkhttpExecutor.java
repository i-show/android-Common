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

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bright.common.entries.HttpError;
import com.bright.common.exchange.okhttp.callback.CallBack;
import com.bright.common.exchange.okhttp.request.Request;
import com.bright.common.exchange.okhttp.response.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

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
    public <T> void execute(com.bright.common.exchange.okhttp.request.Request request, CallBack<T> callBack) {
        switch (request.getMethod()) {
            case GET:
                executeGet(request, callBack);
                break;
        }
    }

    /**
     * Request Get
     */
    private <T> void executeGet(final Request request, final CallBack<T> callBack) {
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

        okhttp3.Request okHttpRequest = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers)
                .build();

        // Debug this
        debugRequest(request);
        executeOkHttp(request, okHttpRequest, callBack);
    }

    private <T> void executeOkHttp(@NonNull final Request request,
                                   @NonNull final okhttp3.Request okHttpRequest,
                                   @NonNull final CallBack<T> callBack) {
        Call call;

        if (request.isChangedTimeOut()) {
            OkHttpClient client = mOkHttpClient.newBuilder()
                    .readTimeout(request.getConnTimeOut(true), TimeUnit.MILLISECONDS)
                    .writeTimeout(request.getWriteTimeOut(true), TimeUnit.MILLISECONDS)
                    .connectTimeout(request.getConnTimeOut(true), TimeUnit.MILLISECONDS)
                    .build();
            call = client.newCall(okHttpRequest);
        } else {
            call = mOkHttpClient.newCall(okHttpRequest);
        }

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    sendCanceledReuslt(request, callBack);
                } else {
                    HttpError error = HttpError.makeError(request);
                    error.setCode(HttpError.ERROR_IO);
                    error.setMessage("io exception");
                    error.setException(e);
                    callBack.runOnUiThreadFailed(error);
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response okhttp3Response) throws IOException {
                Response response = Response.makeResponse(request);
                response.setCanceled(call.isCanceled());
                response.setSuccessful(okhttp3Response.isSuccessful());
                response.setCode(okhttp3Response.code());
                response.setBody(okhttp3Response.body().bytes());

                if (!isCanceled(request, response, callBack) && isSuccessful(request, response, callBack)) {
                    T t = callBack.parseResponse(response);
                    callBack.runOnUiThreadSuccessful(request.getId(), t);
                }
            }
        });
    }


}
