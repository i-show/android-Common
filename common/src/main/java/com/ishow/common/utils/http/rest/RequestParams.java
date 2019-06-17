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

import androidx.annotation.NonNull;

import com.ishow.common.entries.KeyValue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bright.yu on 2017/3/1.
 * 请求参数
 */
@SuppressWarnings("unused")
public class RequestParams {
    /**
     * 普通的参数 KeyValue样式的
     */
    private List<KeyValue> normalParams;
    /**
     * 如果是请求json,或者单个文件的时候使用的
     */
    private Object body;
    /**
     * 多个文件
     */
    private List<MultiBody> bodyList;

    public RequestParams() {
        normalParams = new ArrayList<>();
    }

    public void addParams(@NonNull String key, @NonNull String value) {
        normalParams.add(new KeyValue(key, value));
    }

    public void addParams(@NonNull String key, long value) {
        normalParams.add(new KeyValue(key, String.valueOf(value)));
    }

    public void addParams(@NonNull String key, double value) {
        normalParams.add(new KeyValue(key, String.valueOf(value)));
    }

    public void addParams(@NonNull String key, File value) {
        if (value == null) {
            return;
        }
        MultiBody body = new MultiBody();
        body.setKey(key);
        body.setName(value.getName());
        body.setBody(value);

        if (bodyList == null) {
            bodyList = new ArrayList<>();
        }

        bodyList.add(body);
    }

    public void addParams(@NonNull String key, byte[] value) {
        if (value == null) {
            return;
        }
        MultiBody body = new MultiBody();
        body.setKey(key);
        body.setName(key);
        body.setBody(value);

        if (bodyList == null) {
            bodyList = new ArrayList<>();
        }

        bodyList.add(body);
    }

    public void params(@NonNull List<KeyValue> params) {
        normalParams.addAll(params);
    }

    public void params(@NonNull String body) {
        this.body = body;
    }

    public void params(@NonNull File body) {
        this.body = body;
    }

    public void params(@NonNull byte[] body) {
        this.body = body;
    }

    public
    @NonNull
    List<KeyValue> getNormalParams() {
        if (normalParams == null) {
            normalParams = new ArrayList<>();
        }
        return normalParams;
    }

    /**
     * Get request body
     */
    public Object getBody() {
        return body;
    }

    /**
     * Return multibody
     */
    public
    @NonNull
    List<MultiBody> getBodyList() {
        if (bodyList == null) {
            bodyList = new ArrayList<>();
        }
        return bodyList;
    }
}
