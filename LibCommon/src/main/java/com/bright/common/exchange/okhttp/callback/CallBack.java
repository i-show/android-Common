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

package com.bright.common.exchange.okhttp.callback;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.bright.common.entries.HttpError;
import com.bright.common.exchange.okhttp.response.Response;

/**
 * Created by Bright.Yu on 2017/2/20.
 * 返回
 */

public abstract class CallBack<T> {
    /**
     * CallBack onFailed
     */
    protected abstract void onFailed(final long id, @NonNull HttpError error);

    /**
     * CallBack onFailed runOnUiThread
     */
    public final void runOnUiThreadFailed(final long id, @NonNull final HttpError error) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailed(id, error);
            }
        });
    }

    /**
     * CallBack onSuccess
     */
    protected abstract void onSuccess(final long id, T result);

    /**
     * CallBack onFailed runOnUiThread
     */
    public final void runOnUiThreadSuccessful(final long id, @NonNull final T response) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(id, response);
            }
        });
    }

    /**
     * CallBack parseResponse
     */
    public abstract T parseResponse(Response response);


}
