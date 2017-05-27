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

package com.ishow.common.utils.http.rest.executor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ishow.common.utils.http.rest.Cookie;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.entries.KeyValue;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.http.rest.RequestParams;
import com.ishow.common.utils.http.rest.callback.CallBack;
import com.ishow.common.utils.http.rest.exception.CanceledException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;
import com.ishow.common.utils.log.L;

import java.util.List;
import java.util.Map;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Execuor
 */

public abstract class Executor {
    /**
     * 初始化
     */
    public abstract void init(Context context);

    /**
     * 执行
     */
    public abstract <T> void execute(Request request, CallBack<T> callBack);

    /**
     * 移除Cookie
     */
    public abstract void clearCookie(Context context);

    /**
     * Format Url
     */
    protected String formatUrl(Request request) {
        String url = request.getUrl();
        if (TextUtils.isEmpty(url)) {
            throw new IllegalStateException("need a url");
        }

        String paramsString = buildParams(request);
        if (TextUtils.isEmpty(paramsString)) {
            return url;
        }

        if (url.contains("?")) {
            return StringUtils.plusString(url, "&", paramsString);
        } else {
            return StringUtils.plusString(url, "?", paramsString);
        }
    }

    /**
     * parsms to string
     */
    private String buildParams(Request request) {
        List<KeyValue> paramList = request.getParams().getNormalParams();

        switch (request.getMethod()) {
            case POST:
                // POST的时候放到里 Body里面
                return StringUtils.EMPTY;
            default:
                return buildParams(paramList);
        }
    }


    private String buildParams(@NonNull List<KeyValue> paramList) {
        if (paramList.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        for (KeyValue param : paramList) {
            String key = param.getKey();
            Object value = param.getValue();

            if (value instanceof String) {
                builder.append("&");
                builder.append(key);
                builder.append("=");
                builder.append(String.valueOf(value));
            }
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * Debug this
     * <p>
     * TODO ：
     * 1. 请求的参数和Mediatype打印
     */
    @SuppressWarnings("WeakerAccess")
    protected void debugRequest(@NonNull final Request request) {
        L.d(request.getLogTag(), StringUtils.plusString("=================== ", request.getMethod(), ":", request.getId(), " ==================="));
        L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " URL     = ", request.getFinalUrl()));

        Map<String, String> headers = request.getHeaders();
        if (headers != null && headers.size() > 0) {
            L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " HEADERS = ", headers.toString()));
        }
        L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " TIMEOUT = ", request.getConnTimeOut(true)));

        RequestParams params = request.getParams();

        String normalParsms = buildParams(params.getNormalParams());
        if (!TextUtils.isEmpty(normalParsms)) {
            L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " PARAMS = ", normalParsms));
        }

        Object body = params.getBody();
        if (body != null && body instanceof String) {
            L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " PARAMS = ", body.toString()));
        }
    }


    /**
     * 请求是否已经被取消掉
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean isCanceled(@NonNull Request request, @NonNull Response response, @NonNull CallBack callBack) {
        boolean canceled = response.isCanceled();
        if (canceled) {
            sendCanceledReuslt(request, callBack);
        }
        return canceled;
    }

    @SuppressWarnings("WeakerAccess")
    protected void sendCanceledReuslt(@NonNull Request request, @NonNull CallBack callBack) {
        HttpError error = HttpError.makeError(request);
        error.setCode(HttpError.ERROR_CANCELED);
        error.setMessage("call is canceled");
        error.setException(new CanceledException());
        callBack.runOnUiThreadFailed(error);
    }

    /**
     * 请求是否成功
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean isSuccessful(@NonNull Request request, @NonNull Response response, @NonNull CallBack callBack) {
        boolean successful = response.isSuccessful();
        if (!successful) {
            sendResponseCodeErrorResult(request, response, callBack);
        }
        return successful;

    }

    @SuppressWarnings("WeakerAccess")
    protected void sendResponseCodeErrorResult(@NonNull Request request, @NonNull Response response, @NonNull CallBack callBack) {
        HttpError error = HttpError.makeError(request);
        error.setCode(response.getCode());
        error.setMessage("request failed , reponse's code is :" + response.getCode());
        error.setException(new IllegalStateException());
        callBack.runOnUiThreadFailed(error);
    }

    /**
     * 取消请求
     */
    public abstract void cancle(@NonNull Object tag);
}
