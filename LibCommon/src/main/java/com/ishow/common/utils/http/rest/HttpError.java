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

package com.ishow.common.utils.http.rest;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.request.Request;


/**
 * Created by Bright.Yu on 2017/2/15.
 * Http 返回的Error信息
 */
@Keep
public class HttpError {
    /**
     * IOException
     */
    public static final int ERROR_IO = -100;
    /**
     * call is canceled
     */
    public static final int ERROR_CANCELED = -101;
    /**
     * 泛型的Type没有设置
     */
    public static final int ERROR_TYPE = -102;
    /**
     * HttpRequest Id
     */
    private long id;
    /**
     * HttpRequest logtag
     */
    private String logtag;
    /**
     * Error Code
     */
    private int code;
    /**
     * Error Message
     */
    private String message;
    /**
     * Error Message
     */
    private Exception exception;

    private HttpError() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogtag() {
        return logtag;
    }

    @SuppressWarnings("WeakerAccess")
    public void setLogtag(String logtag) {
        this.logtag = logtag;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }


    /**
     * 创建Error
     */
    public static HttpError makeError(@NonNull Request request) {
        HttpError error = new HttpError();
        error.setId(request.getId());
        error.setLogtag(request.getLogTag());
        return error;
    }

    @Override
    public String toString() {
        return "HttpError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", exception=" + exception.toString() +
                '}';
    }
}
