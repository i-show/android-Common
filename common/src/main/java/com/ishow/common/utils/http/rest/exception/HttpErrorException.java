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

package com.ishow.common.utils.http.rest.exception;

import android.support.annotation.NonNull;

import com.ishow.common.utils.http.rest.HttpError;

/**
 * Created by yuhaiyang on 2017/5/2.
 * HttpError
 */

public class HttpErrorException extends Exception {
    private HttpError mHttpError;

    public void setHttpError(@NonNull HttpError error) {
        mHttpError = error;
    }

    public HttpError getHttpError() {
        return mHttpError;
    }
}
