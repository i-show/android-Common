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

package com.bright.common.entries;

import com.bright.common.utils.StringUtils;

/**
 * Created by Bright.Yu on 2017/2/15.
 * Http 返回的Error信息
 */

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

    public HttpError() {
        this(ERROR_IO, StringUtils.EMPTY);
    }

    public HttpError(Exception e) {
        this(ERROR_IO, StringUtils.EMPTY, e);
    }

    public HttpError(int code, String message) {
        this(code, message, new Exception(message));
    }

    public HttpError(String message, Exception e) {
        this(ERROR_IO, message, e);
    }

    public HttpError(int code, String message, Exception exception) {
        this.code = code;
        this.message = message;
        this.exception = exception;
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
}
