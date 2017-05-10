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

package com.ishow.common.utils.http.rest.request;

import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.MediaType;
import com.ishow.common.utils.http.rest.Method;

import java.io.File;

/**
 * Created by Bright.Yu on 2017/2/16.
 * Post Request
 */

@SuppressWarnings("unused")
public class PostRequest extends Request<PostRequest> {

    public PostRequest() {
        super(Method.POST);
    }

    /**
     * Add String params
     */
    public PostRequest params(@NonNull String body) {
        return params(body, MediaType.JSON);
    }

    /**
     * Add String params
     */
    public PostRequest params(@NonNull String body, @NonNull MediaType mediaType) {
        mediaType(mediaType);
        getParams().params(body);
        return this;
    }

    /**
     * Add file params, Default is json
     */
    public PostRequest params(@NonNull File body) {
        return params(body, MediaType.STREAM);
    }

    /**
     * Add file params, Default is json
     */
    public PostRequest params(@NonNull File body, @NonNull MediaType mediaType) {
        mediaType(mediaType);
        getParams().params(body);
        return this;
    }

    /**
     * Add byte[] params, Default is json
     */
    public PostRequest params(@NonNull byte[] body) {
        getParams().params(body);
        return this;
    }

}
