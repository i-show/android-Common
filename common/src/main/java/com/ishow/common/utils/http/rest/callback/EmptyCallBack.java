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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;

/**
 * Created by bright.yu on 2017/2/28.
 * String OnStatusViewListener
 */
public  class EmptyCallBack extends CallBack<String> {
    @SuppressWarnings("WeakerAccess")
    public EmptyCallBack() {
        super();
    }

    public EmptyCallBack(Context context) {
        super(context);
    }

    @Override
    protected void onFailed(@NonNull HttpError error) {

    }

    @Override
    protected void onSuccess(@Nullable String result) {

    }

    @Override
    public String parseResponse(@NonNull final Request request, @NonNull final Response response) throws HttpErrorException {
        String body = new String(response.getBody());
        response.setDebugString(body);
        return body;
    }
}
