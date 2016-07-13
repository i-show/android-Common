/**
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

package com.bright.common.utils.http.okhttp.request;

import android.text.TextUtils;

import com.bright.common.utils.http.okhttp.OkHttpUtils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.internal.http.HttpMethod;

/**
 * 其他请求类型
 */
public class OtherRequest extends OkHttpRequest {
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");

    private RequestBody requestBody;
    private String method;
    private String content;

    public OtherRequest(RequestBody requestBody, String content, String method, String url, Object tag, Map<String, Object> params, Map<String, Object> headers, int id) {
        super(url, tag, params, headers, id);
        this.requestBody = requestBody;
        this.method = method;
        this.content = content;

    }

    @Override
    protected RequestBody buildRequestBody() {
        if (requestBody == null && TextUtils.isEmpty(content) && HttpMethod.requiresRequestBody(method)) {
            throw new IllegalArgumentException("requestBody and content can not be null in method:" + method);
        }

        if (requestBody == null && !TextUtils.isEmpty(content)) {
            requestBody = RequestBody.create(MEDIA_TYPE_PLAIN, content);
        }

        return requestBody;
    }

    @Override
    protected Request buildRequest(RequestBody requestBody) {
        if (method.equals(OkHttpUtils.METHOD.PUT)) {
            builder.put(requestBody);
        } else if (method.equals(OkHttpUtils.METHOD.DELETE)) {
            if (requestBody == null)
                builder.delete();
            else
                builder.delete(requestBody);
        } else if (method.equals(OkHttpUtils.METHOD.HEAD)) {
            builder.head();
        } else if (method.equals(OkHttpUtils.METHOD.PATCH)) {
            builder.patch(requestBody);
        }

        return builder.build();
    }

}
