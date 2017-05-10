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

import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.MediaType;

import java.io.File;

/**
 * Created by bright.yu on 2017/3/2.
 * 多包上传
 */

public class MultiBody {
    /**
     * Like image， file
     */
    private String type;
    /**
     * Like hello.jpe
     */
    private String name;
    /**
     * body
     */
    private Object body;

    /**
     * mediaTpye；
     */
    private MediaType mediaType;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(@NonNull String body) {
        setBody(body, MediaType.JSON);
    }

    @SuppressWarnings("WeakerAccess")
    public void setBody(@NonNull String body, @NonNull MediaType mediaType) {
        this.body = body;
        this.mediaType = mediaType;
    }

    public void setBody(@NonNull File body) {
        setBody(body, MediaType.STREAM);
    }

    @SuppressWarnings("WeakerAccess")
    public void setBody(@NonNull File body, @NonNull MediaType mediaType) {
        this.body = body;
        this.mediaType = mediaType;
    }

    public void setBody(@NonNull byte[] body) {
        this.body = body;
    }

    public MediaType getMediaType() {
        return mediaType;
    }


}
