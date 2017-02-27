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
import com.bright.common.entries.KeyValue;
import com.bright.common.exchange.okhttp.callback.CallBack;
import com.bright.common.exchange.okhttp.exception.CanceledException;
import com.bright.common.exchange.okhttp.request.Request;
import com.bright.common.exchange.okhttp.response.Response;
import com.bright.common.utils.StringUtils;
import com.bright.common.utils.log.L;

import java.util.List;
import java.util.Map;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Execuor
 */

public abstract class Executor {

    public abstract void init();

    public abstract <T> void execute(Request request, CallBack<T> callBack);


    /**
     * Format Url
     */
    String formatUrl(String url, List<KeyValue> paramList) {
        String paramsString = buildParams(paramList);
        if (TextUtils.isEmpty(paramsString)) {
            return url;
        }

        if (url.contains("?")) {
            return url + "&" + paramsString;
        } else {
            return url + "?" + paramsString;
        }
    }

    /**
     * parsms to string
     */
    private String buildParams(List<KeyValue> paramList) {
        if (paramList == null || paramList.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        for (KeyValue param : paramList) {
            String key = param.getKey();
            Object value = param.getValue();

            if (value instanceof String) {
                builder.append(key);
                builder.append("&");
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
    protected void debugRequest(final Request request) {
        L.d(request.getLogTag(), StringUtils.plusString("=================== ", request.getMethod(), ":", request.getId(), " ==================="));
        L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " URL     = " + request.getUrl()));

        Map<String, String> headers = request.getHeaders();
        if (headers != null && headers.size() > 0) {
            L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " HEADERS = " + headers.toString()));
        }

        L.d(request.getLogTag(), StringUtils.plusString(request.getId(), " TIMEOUT = " + request.getConnTimeOut()));
    }


    /**
     * 请求是否已经被取消掉
     */
    protected boolean isCanceled(@NonNull Response response, @NonNull CallBack callBack) {
        boolean canceled = response.isCanceled();
        if (canceled) {
            sendCanceledReuslt(response.getId(), callBack);
        }
        return canceled;
    }

    protected void sendCanceledReuslt(long id, @NonNull CallBack callBack) {
        HttpError error = new HttpError();
        error.setCode(HttpError.ERROR_CANCELED);
        error.setMessage("call is canceled");
        error.setException(new CanceledException());
        callBack.runOnUiThreadFailed(id, error);
    }

    /**
     * 请求是否成功
     */
    protected boolean isSuccessful(@NonNull Response response, @NonNull CallBack callBack) {
        boolean successful = response.isSuccessful();
        if (!successful) {
            sendResponseCodeErrorResult(response.getId(), response.getCode(), callBack);
        }
        return successful;

    }

    protected void sendResponseCodeErrorResult(long id, int code, @NonNull CallBack callBack) {
        HttpError error = new HttpError();
        error.setCode(code);
        error.setMessage("request failed , reponse's code is :" + code);
        error.setException(new IllegalStateException());
        callBack.runOnUiThreadFailed(id, error);
    }


}
