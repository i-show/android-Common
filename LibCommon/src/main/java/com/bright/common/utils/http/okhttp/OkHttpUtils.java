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

package com.bright.common.utils.http.okhttp;

import com.bright.common.utils.StringUtils;
import com.bright.common.utils.debug.DEBUG;
import com.bright.common.utils.http.okhttp.builder.GetBuilder;
import com.bright.common.utils.http.okhttp.builder.HeadBuilder;
import com.bright.common.utils.http.okhttp.builder.OtherRequestBuilder;
import com.bright.common.utils.http.okhttp.builder.PostFileBuilder;
import com.bright.common.utils.http.okhttp.builder.PostFormBuilder;
import com.bright.common.utils.http.okhttp.builder.PostStringBuilder;
import com.bright.common.utils.http.okhttp.callback.CallBack;
import com.bright.common.utils.http.okhttp.callback.EmptyCallBack;
import com.bright.common.utils.http.okhttp.request.OkHttpRequest;
import com.bright.common.utils.http.okhttp.request.RequestCall;
import com.bright.common.utils.http.okhttp.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * OKHttp
 */
public class OkHttpUtils {
    /**
     * 默认的回调时间
     */
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private static final String TAG = "OkHttpUtils";
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public Platform getPlatform() {
        return mPlatform;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public void execute(final RequestCall requestCall, CallBack callback) {
        if (callback == null) {
            callback = new EmptyCallBack();
        }
        final OkHttpRequest okHttpRequest = requestCall.getOkHttpRequest();
        final Request request = requestCall.getRequest();
        final CallBack finalCallback = callback;
        final int id = okHttpRequest.getId();
        // 设置Debug的Tag
        finalCallback.setLogTag(okHttpRequest.getLogTag());

        debug(requestCall, okHttpRequest, request, id);

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (call.isCanceled()) {
                    finalCallback.sendFailResult(call, new CanceledException(), null, 0, id);
                } else {
                    finalCallback.sendFailResult(call, e, null, 0, id);
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                finalCallback.parseResponse(call, response, id);
            }
        });
    }


    public void cancelTag(Object tag) {
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
    }


    private void debug(RequestCall requestCall, OkHttpRequest okHttpRequest, Request request, final int id) {
        DEBUG.d(okHttpRequest.getLogTag(), StringUtils.plusString("=================== ", request.method(), ":", id, " ==================="));
        DEBUG.d(okHttpRequest.getLogTag(), StringUtils.plusString(id, " URL     = " + request.url().toString()));

        Headers headers = request.headers();
        if (headers != null && headers.size() > 0) {
            DEBUG.d(okHttpRequest.getLogTag(), StringUtils.plusString(id, " HEADERS = " + headers.toString()));
        }
        DEBUG.d(okHttpRequest.getLogTag(), StringUtils.plusString(id, " TIMEOUT = " + requestCall.getConnTimeOut()));
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null) {
                DEBUG.d(okHttpRequest.getLogTag(), StringUtils.plusString(id, " TYPE    = " + mediaType.toString()));
                if (isText(mediaType)) {
                    DEBUG.d(okHttpRequest.getLogTag(), StringUtils.plusString(id, " PARAMS  = " + bodyToString(request)));
                }
            }
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml") ||
                    mediaType.subtype().equals("x-www-form-urlencoded"))
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }


    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

