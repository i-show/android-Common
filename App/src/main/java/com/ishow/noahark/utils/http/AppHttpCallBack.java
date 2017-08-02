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

package com.ishow.noahark.utils.http;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.ishow.common.utils.http.rest.Headers;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.http.rest.callback.CallBack;
import com.ishow.common.utils.http.rest.config.HttpConfig;
import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;
import com.ishow.common.widget.dialog.BaseDialog;
import com.ishow.noahark.constant.Configure;
import com.ishow.noahark.entries.http.AppHttpResult;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yuhaiyang on 2017/5/2.
 * 对应项目的处理
 */

public abstract class AppHttpCallBack<T> extends CallBack<T> {

    protected AppHttpCallBack() {
        super();
    }

    protected AppHttpCallBack(Context context) {
        super(context);
    }


    @Override
    public T parseResponse(@NonNull Request request, @NonNull Response response) throws HttpErrorException {
        Headers headers = response.getHeaders();
        HttpConfig.setHeader(Configure.HTTP_TOKEN, headers.get(Configure.HTTP_TOKEN));

        Type genType = getClass().getGenericSuperclass();
        Type type;
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            type = params[0];
        } else {
            type = String.class;
        }

        String body = new String(response.getBody());
        AppHttpResult result = JSON.parseObject(body, AppHttpResult.class);
        if (result.isSuccess()) {
            response.setDebugString(result.getValue());
            return JSON.parseObject(result.getValue(), type);
        } else {
            HttpError error = HttpError.makeError(request);
            error.setCode(result.getCode());
            error.setMessage(result.getMessage());
            HttpErrorException e = new HttpErrorException();
            e.setHttpError(error);
            throw e;
        }
    }
}
