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

package com.ishow.common.utils.http.rest.config;

import android.support.annotation.Keep;
import android.text.TextUtils;

import com.ishow.common.utils.http.rest.Cookie;
import com.ishow.common.utils.http.rest.Headers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Http的配置
 * <p>
 * 扩展：
 * 1. HTTPS 配置
 */
@Keep
@SuppressWarnings("unused")
public class HttpConfig {
    /**
     * 默认的回调时间
     */
    @SuppressWarnings("WeakerAccess")
    public static final long DEFAULT_TIME_OUT_MILLISECONDS = 6_000L;

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
     * 保存Cookie的类型
     */
    @Cookie.Type
    private int cookieType;

    /**
     * Request headers
     */
    private static Headers.Builder headersBuilder;

    private static Map<String, Object> mCommonParams;

    @SuppressWarnings("WeakerAccess")
    public HttpConfig() {
        cookieType = Cookie.Type.FILE;
    }


    public long getReadTimeOut() {
        if (readTimeOut <= 0) {
            readTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        }
        return readTimeOut;
    }

    public void setReadTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public long getWriteTimeOut() {
        if (writeTimeOut <= 0) {
            writeTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        }
        return writeTimeOut;
    }

    public void setWriteTimeOut(long writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
    }

    public long getConnTimeOut() {
        if (connTimeOut <= 0) {
            connTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        }
        return connTimeOut;
    }

    public void setConnTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
    }


    @Cookie.Type
    public int getCookieType() {
        return cookieType;
    }

    public void setCookieType(@Cookie.Type int cookieType) {
        this.cookieType = cookieType;
    }

    /**
     * 获取默认的Configure
     */
    public static HttpConfig getDefaultConfig() {
        HttpConfig config = new HttpConfig();
        config.readTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        config.writeTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        config.connTimeOut = DEFAULT_TIME_OUT_MILLISECONDS;
        // 默认的Type
        config.cookieType = Cookie.Type.FILE;
        return config;
    }

    public static void clearHeaders() {
        if (headersBuilder != null) {
            headersBuilder.clear();
        }
    }

    public static void addHeader(String name, String values) {
        if (headersBuilder == null) {
            headersBuilder = new Headers.Builder();
        }
        headersBuilder.add(name, values);
    }

    public static void setHeader(String name, String values) {
        if (headersBuilder == null) {
            headersBuilder = new Headers.Builder();
        }
        headersBuilder.set(name, values);
    }

    public static String getHeader(String name) {
        if (headersBuilder == null) {
            return null;
        }

        return headersBuilder.get(name);
    }

    public static Headers getHeaders() {
        if (headersBuilder == null) {
            headersBuilder = new Headers.Builder();
        }
        return headersBuilder.build();
    }

    public static void addCommonParams(String key, Object value) {
        if (TextUtils.isEmpty(key) || value == null) {
            return;
        }

        if (mCommonParams == null) {
            mCommonParams = new HashMap<>();
        }
        mCommonParams.put(key, value);
    }

    public static Map<String, Object> getCommonParams(){
        if (mCommonParams == null) {
            mCommonParams = new HashMap<>();
        }
        return mCommonParams;
    }
}
