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

package com.ishow.common.utils.http.rest.callback;

import android.content.Context;
import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Bright.Yu on 2017/3/13.
 * Json的解析
 */
@SuppressWarnings("unused")
public abstract class JsonCallBack<T> extends CallBack<T> {

    @SuppressWarnings("WeakerAccess")
    public JsonCallBack() {
        super();
    }

    public JsonCallBack(Context context) {
        super(context);
    }

    @Override
    public T parseResponse(@NonNull final Request request, @NonNull final Response response) throws HttpErrorException {
        Type genType = getClass().getGenericSuperclass();

        Type type;
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            type = params[0];
        } else {
            type = String.class;
        }

        String body = new String(response.getBody());
        response.setDebugString(body);

        return JSON.parseObject(response.getBody(), type);
    }
}
