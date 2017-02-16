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

import java.util.Map;

import okhttp3.MediaType;

/**
 * Request
 */
public abstract class Request {
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
    private Map<String, Object> headers;


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

    public abstract void execute();
}
