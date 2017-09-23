/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.noahark.entries.http;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by yuhaiyang on 2017/8/2.
 * Http请求结果
 */
public class AppHttpResult<T> {
    @JSONField(name = "status")
    public int code;
    public String message;
    public Object value;


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

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isSuccess() {
        return code == Key.CODE_SUCCESS;
    }

    public static final class Key {
        public static final int CODE_FAILED = 0;
        public static final int CODE_SUCCESS = 1;
    }


}
