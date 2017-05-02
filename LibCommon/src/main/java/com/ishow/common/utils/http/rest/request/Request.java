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

package com.ishow.common.utils.http.rest.request;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ishow.common.entries.KeyValue;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.MediaType;
import com.ishow.common.utils.http.rest.Method;
import com.ishow.common.utils.http.rest.RequestParams;
import com.ishow.common.utils.http.rest.callback.CallBack;
import com.ishow.common.utils.http.rest.config.HttpConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Request
 * <p>
 * 注意：
 * 暂时实现了 Get和Post 如果有其他需求例如：PUT DELETE 可以进行另加
 */
@SuppressWarnings("unused")
public abstract class Request<T extends Request> {
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
     * 增加参数后的
     */
    private String finalUrl;
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
    private RequestParams params;
    /**
     * config
     */
    private HttpConfig httpConfig;

    public Request(Method method) {
        this.method = method;
        headers = new HashMap<>();
        params = new RequestParams();
        httpConfig = Http.getConfig();
    }

    @SuppressWarnings("unchecked")
    public T id(@IntRange(from = 1, to = Integer.MAX_VALUE) long id) {
        this.id = id;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T readTimeOut(@IntRange(from = 1, to = Integer.MAX_VALUE) long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T writeTimeOut(@IntRange(from = 1, to = Integer.MAX_VALUE) long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T connTimeOut(@IntRange(from = 1, to = Integer.MAX_VALUE) long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T tag(@NonNull Object tag) {
        this.tag = tag;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T logTag(@NonNull String logTag) {
        this.logTag = logTag;
        return (T) this;
    }

    @SuppressWarnings("WeakerAccess,unchecked")
    public T mediaType(@NonNull MediaType mediaType) {
        this.mediaType = mediaType;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T url(@NonNull String url) {
        this.url = url;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T headers(@NonNull Map<String, String> headers) {
        this.headers.putAll(headers);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addHeader(@NonNull String key, long value) {
        headers.put(key, String.valueOf(value));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addHeader(@NonNull String key, double value) {
        headers.put(key, String.valueOf(value));
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addHeader(@NonNull String key, @NonNull String value) {
        headers.put(key, value);
        return (T) this;
    }

    // -------- 参数的封装 ----------//
    @SuppressWarnings("unchecked")
    public T addParams(@NonNull String key, @NonNull String value) {
        params.addParams(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addParams(@NonNull String key, long value) {
        params.addParams(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T addParams(@NonNull String key, double value) {
        params.addParams(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T params(@NonNull List<KeyValue> listParams) {
        params.params(listParams);
        return (T) this;
    }

    public RequestParams getParams() {
        return params;
    }


    public long getId() {
        if (id == 0) {
            id = Integer.valueOf(String.valueOf(System.currentTimeMillis()).substring(5));
        }
        return id;
    }

    /**
     * Get Request Read Time Out
     */
    public long getReadTimeOut() {
        return getReadTimeOut(false);
    }

    /**
     * Get Request Read Time Out
     *
     * @param useDefault if readTimeOut = 0 return defualt
     */
    public long getReadTimeOut(boolean useDefault) {
        if (readTimeOut <= 0) {
            return useDefault ? httpConfig.getReadTimeOut() : readTimeOut;
        }
        return readTimeOut;
    }

    /**
     * Get Request Write Time Out
     */
    public long getWriteTimeOut() {
        return getWriteTimeOut(false);
    }

    /**
     * Get Request Write Time Out
     *
     * @param useDefault if writeTimeOut = 0 return defualt
     */
    public long getWriteTimeOut(boolean useDefault) {
        if (writeTimeOut <= 0) {
            return useDefault ? httpConfig.getWriteTimeOut() : writeTimeOut;
        }
        return writeTimeOut;
    }


    /**
     * Get Request Conn Time Out
     */
    public long getConnTimeOut() {
        return getConnTimeOut(false);
    }

    /**
     * Get Request Conn Time Out
     *
     * @param useDefault if connTimeOut = 0 return defualt
     */
    public long getConnTimeOut(boolean useDefault) {
        if (connTimeOut <= 0) {
            return useDefault ? httpConfig.getConnTimeOut() : connTimeOut;
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
        if (headers == null) {
            headers = new HashMap<>();
        }
        return headers;
    }

    public Method getMethod() {
        return method;
    }


    public String getFinalUrl() {
        if (TextUtils.isEmpty(finalUrl)) {
            return url;
        } else {
            return finalUrl;
        }
    }

    public void setFinalUrl(String finalUrl) {
        this.finalUrl = finalUrl;
    }

    public boolean isChangedTimeOut() {
        return (readTimeOut > 0 || writeTimeOut > 0 || connTimeOut > 0);
    }

    public final void execute(CallBack callBack) {
        Http.getExecutor().execute(this, callBack);
    }
}
