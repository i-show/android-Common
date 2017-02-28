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

package com.bright.common.exchange.okhttp.request;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bright.common.entries.KeyValue;
import com.bright.common.exchange.okhttp.Http;
import com.bright.common.exchange.okhttp.Method;
import com.bright.common.exchange.okhttp.callback.CallBack;
import com.bright.common.exchange.okhttp.config.HttpConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;

/**
 * Request
 */
@SuppressWarnings("unused")
public abstract class Request {
    /**
     * Debug tag
     */
    private static final String DEFAULT_DEBUG_TAG = "HTTP";
    /**
     * Id
     */
    private long id;
    /**
     * Request readTimeOut
     */
    private long readTimeOut;
    /**
     * Request writeTimeOut
     */
    private long writeTimeOut;
    /**
     * Request connTimeOut
     */
    private long connTimeOut;
    /**
     * Request url
     */
    private String url;
    /**
     * Request tag
     * You can cancel the request by tag
     */
    private Object tag;
    /**
     * Request logtag
     * You can easy to view log
     */
    private String logTag;
    /**
     * Request mediaType
     */
    private MediaType mediaType;
    /**
     * Request headers
     */
    private Map<String, String> headers;

    /**
     * Request method
     */
    private Method method;

    /**
     * params
     */
    private List<KeyValue> params;

    private HttpConfig mHttpConfig;

    public Request(Method method) {
        headers = new HashMap<>();
        params = new ArrayList<>();
        this.method = method;

        mHttpConfig = Http.getInstance().getHttpConfig();
    }

    public Request id(@IntRange(from = 1, to = Integer.MAX_VALUE) long id) {
        this.id = id;
        return this;
    }

    public Request readTimeOut(@IntRange(from = 1, to = Integer.MAX_VALUE) long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public Request writeTimeOut(@IntRange(from = 1, to = Integer.MAX_VALUE) long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }


    public Request connTimeOut(@IntRange(from = 1, to = Integer.MAX_VALUE) long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public Request tag(@NonNull Object tag) {
        this.tag = tag;
        return this;
    }

    public Request logTag(@NonNull String logTag) {
        this.logTag = logTag;
        return this;
    }

    public Request mediaType(@NonNull MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public Request url(@NonNull String url) {
        this.url = url;
        return this;
    }

    public Request addHeader(@NonNull String key, long value) {
        headers.put(key, String.valueOf(value));
        return this;
    }

    public Request addHeader(@NonNull String key, double value) {
        headers.put(key, String.valueOf(value));
        return this;
    }

    public Request addHeader(@NonNull String key, @NonNull String value) {
        headers.put(key, value);
        return this;
    }

    // -------- 参数的封装 ----------//
    public void addParams(@NonNull String key, @NonNull String value) {
        params.add(new KeyValue(key, value));
    }

    public void addParams(@NonNull String key, long value) {
        params.add(new KeyValue(key, String.valueOf(value)));
    }

    public void addParams(@NonNull String key, double value) {
        params.add(new KeyValue(key, String.valueOf(value)));
    }

    public List<KeyValue> getParams() {
        if (params == null) {
            params = new ArrayList<>();
        }
        return params;
    }


    public long getId() {
        if (id == 0) {
            id = Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(5));
        }
        return id;
    }

    public long getReadTimeOut() {
        return getReadTimeOut(false);
    }

    public long getReadTimeOut(boolean useDefault) {
        if (readTimeOut <= 0) {
            return useDefault ? mHttpConfig.getReadTimeOut() : readTimeOut;
        }
        return readTimeOut;
    }

    public long getWriteTimeOut() {
        return getWriteTimeOut(false);
    }

    public long getWriteTimeOut(boolean useDefault) {
        if (writeTimeOut <= 0) {
            return useDefault ? mHttpConfig.getWriteTimeOut() : writeTimeOut;
        }
        return writeTimeOut;
    }


    public long getConnTimeOut() {
        return getConnTimeOut(false);
    }

    public long getConnTimeOut(boolean useDefault) {
        if (connTimeOut <= 0) {
            return useDefault ? mHttpConfig.getConnTimeOut() : connTimeOut;
        }
        return connTimeOut;
    }


    public String getUrl() {
        return url;
    }

    public Object getTag() {
        return tag;
    }

    public String getLogTag() {
        if (TextUtils.isEmpty(logTag)) {
            logTag = DEFAULT_DEBUG_TAG;
        }
        return logTag;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isChangedTimeOut() {
        return (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0);
    }

    public final void execute(CallBack callBack) {
        Http.getInstance().getExecutor().execute(this, callBack);
    }
}
