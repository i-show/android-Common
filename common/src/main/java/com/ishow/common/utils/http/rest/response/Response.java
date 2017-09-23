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

package com.ishow.common.utils.http.rest.response;

import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.Headers;
import com.ishow.common.utils.http.rest.request.Request;

/**
 * Created by bright.yu on 2017/2/27.
 * Response
 */

public class Response {

    /**
     * request is canceled
     */
    private boolean isCanceled;
    /**
     * request is response is successful
     */
    private boolean isSuccessful;

    /**
     * Result code
     */
    private int code;
    /**
     * sync by Request
     */
    private long id;
    /**
     * sync by Request
     */
    private String logtag;
    /**
     * body
     */
    private byte[] body;
    /**
     * Debug 的String
     */
    private String debugString;

    private Headers headers;
    private Response() {
    }

    public byte[] getBody() {
        if (body == null) {
            body = new byte[]{};
        }
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }


    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    private void setLogtag(String logtag) {
        this.logtag = logtag;
    }

    public void setDebugString(String debugString) {
        this.debugString = debugString;
    }

    public String getDebugString() {
        return debugString;
    }


    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    /**
     * 创建Error
     */
    public static Response makeResponse(@NonNull Request request) {
        Response response = new Response();
        response.setId(request.getId());
        response.setLogtag(request.getLogTag());
        return response;
    }
}
