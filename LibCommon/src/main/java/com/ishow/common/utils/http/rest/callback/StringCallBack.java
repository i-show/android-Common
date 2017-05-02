/*
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

package com.ishow.common.utils.http.rest.callback;

import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;

/**
 * Created by bright.yu on 2017/2/28.
 * String CallBack
 */
public abstract class StringCallBack extends CallBack<String> {
    @Override
    public String parseResponse(@NonNull final Request request, @NonNull final Response response) throws HttpErrorException {
        String body = new String(response.getBody());
        response.setDebugString(body);
        return body;
    }
}
