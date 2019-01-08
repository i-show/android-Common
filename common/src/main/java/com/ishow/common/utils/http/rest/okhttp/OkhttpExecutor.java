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

package com.ishow.common.utils.http.rest.okhttp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ishow.common.entries.KeyValue;
import com.ishow.common.utils.http.rest.Headers;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.http.rest.MultiBody;
import com.ishow.common.utils.http.rest.RequestParams;
import com.ishow.common.utils.http.rest.callback.CallBack;
import com.ishow.common.utils.http.rest.config.HttpConfig;
import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.executor.Executor;
import com.ishow.common.utils.http.rest.okhttp.cookie.OkCookiesManager;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;
import com.ishow.common.utils.log.LogManager;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Okhttp的请求
 */
public class OkhttpExecutor extends Executor {
    private static final String TAG = "OkhttpExecutor";
    private OkHttpClient mOkHttpClient;
    private OkCookiesManager mCookiesManager;

    @Override
    public void init(Context context) {
        HttpConfig config = Http.getConfig();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Step 1. 设置超时时间
        builder.connectTimeout(config.getConnTimeOut(), TimeUnit.MILLISECONDS);
        builder.readTimeout(config.getReadTimeOut(), TimeUnit.MILLISECONDS);
        builder.writeTimeout(config.getWriteTimeOut(), TimeUnit.MILLISECONDS);
        // Step 2. 设置Cookie
        mCookiesManager = new OkCookiesManager(context, config.getCookieType());
        builder.cookieJar(mCookiesManager);
        mOkHttpClient = builder.build();
    }


    @Override
    public <T> void execute(Request request, CallBack<T> callBack) {

        // Step 1. url
        String url = formatUrl(request);
        request.setFinalUrl(url);
        // Step 2. headers
        okhttp3.Headers headers = makeHeaders(request);
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
                .url(url)
                .headers(headers);

        // Debug this
        debugRequest(request);

        switch (request.getMethod()) {
            case POST:
                RequestBody body = makeBody(request);
                builder.post(body);
                break;
        }

        executeOkHttp(request, builder.build(), callBack);
    }

    @Override
    public void clearCookie(Context context) {
        mCookiesManager.clearCookie();
    }

    @Override
    public void cancel(@NonNull Object tag) {
        try {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }

            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        } catch (Exception e) {
            LogManager.i(TAG, "cancle " + e.toString());
        }
    }

    private <T> void executeOkHttp(@NonNull final Request request,
                                   @NonNull final okhttp3.Request okHttpRequest,
                                   final CallBack<T> callBack) {
        Call call;

        if (request.isChangedTimeOut()) {
            OkHttpClient client = mOkHttpClient.newBuilder()
                    .readTimeout(request.getReadTimeOut(true), TimeUnit.MILLISECONDS)
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
                if (callBack == null) {
                    LogManager.d(request.getLogTag(), "request failed");
                    return;
                }

                if (call.isCanceled()) {
                    sendCanceledResult(request, callBack);
                } else {
                    HttpError error = HttpError.makeError(request);
                    error.setCode(HttpError.ERROR_IO);
                    error.setMessage("io exception");
                    error.setException(e);
                    sendFailed(callBack, error);
                }
            }

            @Override
            public void onResponse(Call call, okhttp3.Response okhttp3Response) throws IOException {
                if (callBack == null) {
                    LogManager.d(request.getLogTag(), "request success");
                    return;
                }

                Response response = Response.makeResponse(request);
                response.setCanceled(call.isCanceled());
                response.setSuccessful(okhttp3Response.isSuccessful());
                response.setCode(okhttp3Response.code());
                response.setBody(okhttp3Response.body().bytes());
                response.setHeaders(parseHeaders(okhttp3Response.headers()));

                if (!isCanceled(request, response, callBack) && isSuccessful(request, response, callBack)) {
                    try {
                        T t = callBack.parseResponse(request, response);
                        callBack.runOnUiThreadSuccessful(response, t);
                    } catch (HttpErrorException e) {
                        HttpError error = e.getHttpError();
                        sendFailed(callBack, error);
                    } catch (Exception e) {
                        HttpError error = HttpError.makeError(request);
                        error.setCode(HttpError.ERROR_TYPE);
                        error.setMessage(e.getMessage());
                        error.setException(e);
                        sendFailed(callBack, error);
                    }
                }
            }
        });
    }


    /**
     * Maker Header
     */
    private okhttp3.Headers makeHeaders(@NonNull Request request) {
        Headers headers = request.getHeaders();
        okhttp3.Headers.Builder builder = new okhttp3.Headers.Builder();

        // 添加默认的Header
        Headers defaultHeader = HttpConfig.getHeaders();
        for (int i = 0, size = defaultHeader.size(); i < size; i++) {
            builder.add(defaultHeader.name(i), defaultHeader.value(i));
        }

        if (headers != null) {
            for (int i = 0, size = headers.size(); i < size; i++) {
                builder.add(headers.name(i), headers.value(i));
            }
        }
        return builder.build();
    }

    /**
     * Maker Body
     */
    private RequestBody makeBody(@NonNull Request request) {
        RequestParams params = request.getParams();
        Object body = params.getBody();
        List<MultiBody> bodyList = params.getBodyList();
        List<KeyValue> normalParams = params.getNormalParams();

        if (body == null && bodyList.isEmpty()) {
            // 只有key value样式的
            FormBody.Builder builder = new FormBody.Builder();

            // 先添加通用参数
            if(request.isAddCommonParams()) {
                for (Map.Entry<String, Object> entry : HttpConfig.getCommonParams().entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    builder.add(key, String.valueOf(value));
                }
            }

            for (KeyValue keyValue : normalParams) {
                builder.add(keyValue.getKey(), String.valueOf(keyValue.getValue()));
            }
            return builder.build();
        } else if (normalParams.isEmpty() && body != null && bodyList.isEmpty()) {
            // 只有json样式或者只有一个File的
            MediaType mediaType = MediaType.parse(request.getMediaType().getBody());
            if (body instanceof String) {
                return RequestBody.create(mediaType, (String) body);
            } else if (body instanceof File) {
                return RequestBody.create(mediaType, (File) body);
            } else if (body instanceof byte[]) {
                return RequestBody.create(mediaType, (byte[]) body);
            }
        } else {
            // 混合的样式的
            MultipartBody.Builder builder = new MultipartBody.Builder();
            //builder.setType(MultipartBody.FORM);
            // 先添加通用参数
            for (Map.Entry<String, Object> entry : HttpConfig.getCommonParams().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                builder.addFormDataPart(key, String.valueOf(value));
            }

            // 1. Normal
            if (!normalParams.isEmpty()) {
                for (KeyValue keyValue : normalParams) {
                    builder.addFormDataPart(keyValue.getKey(), String.valueOf(keyValue.getValue()));
                }
            }
            //  2. Only one body
            if (body != null) {
                MediaType mediaType = MediaType.parse(request.getMediaType().getBody());
                if (body instanceof String) {
                    builder.addPart(RequestBody.create(mediaType, (String) body));
                } else if (body instanceof File) {
                    builder.addPart(RequestBody.create(mediaType, (File) body));
                } else if (body instanceof byte[]) {
                    builder.addPart(RequestBody.create(mediaType, (byte[]) body));
                }
            }

            // 3. MultiBOdy
            for (MultiBody multiBody : bodyList) {
                MediaType mediaType = MediaType.parse(multiBody.getMediaType().getBody());
                Object _body = multiBody.getBody();
                if (_body == null) {
                    builder.addFormDataPart(multiBody.getKey(), multiBody.getName());
                } else if (_body instanceof String) {
                    RequestBody requestBody = RequestBody.create(mediaType, (String) _body);
                    builder.addFormDataPart(multiBody.getKey(), multiBody.getName(), requestBody);
                } else if (_body instanceof File) {
                    RequestBody requestBody = RequestBody.create(mediaType, (File) _body);
                    builder.addFormDataPart(multiBody.getKey(), multiBody.getName(), requestBody);
                } else if (_body instanceof byte[]) {
                    RequestBody requestBody = RequestBody.create(mediaType, (byte[]) _body);
                    builder.addFormDataPart(multiBody.getKey(), multiBody.getName(), requestBody);
                }
            }
            return builder.build();
        }
        return new FormBody.Builder().build();
    }


    private Headers parseHeaders(okhttp3.Headers header) {
        Headers.Builder builder = new Headers.Builder();
        if (header != null) {
            for (int i = 0, size = header.size(); i < size; i++) {
                builder.add(header.name(i), header.value(i));
            }
        }
        return builder.build();
    }
}
