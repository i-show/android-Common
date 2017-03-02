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

package com.bright.common.exchange.okhttp;

import android.support.annotation.NonNull;

import com.bright.common.entries.KeyValue;

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
     * 如果是请求json的时候使用的
     */
    private Object body;

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

    public List<KeyValue> getNormalParams() {
        if (normalParams == null) {
            normalParams = new ArrayList<>();
        }
        return normalParams;
    }

    public Object getBody() {
        return body;
    }
}
