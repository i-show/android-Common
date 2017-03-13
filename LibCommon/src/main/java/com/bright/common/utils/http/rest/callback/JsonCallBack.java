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

package com.bright.common.utils.http.rest.callback;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.bright.common.utils.http.rest.request.Request;
import com.bright.common.utils.http.rest.response.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Bright.Yu on 2017/3/13.
 * Json的解析
 */
@SuppressWarnings("unused")
public abstract class JsonCallBack<T> extends CallBack<T> {

    @Override
    public T parseResponse(@NonNull final Request request, @NonNull final Response response) throws Exception {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];
        Log.i("nian", "parseResponse: = " + new String(response.getBody()));
        return JSON.parseObject(response.getBody(), type);
    }
}
