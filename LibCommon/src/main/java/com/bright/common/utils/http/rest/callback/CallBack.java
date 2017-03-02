/**
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

package com.bright.common.utils.http.rest.callback;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bright.common.R;
import com.bright.common.utils.http.rest.HttpError;
import com.bright.common.utils.StringUtils;
import com.bright.common.utils.debug.DEBUG;
import com.bright.common.utils.http.rest.Http;
import com.bright.common.utils.http.rest.exception.CanceledException;
import com.bright.common.utils.http.rest.response.Response;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by Bright.Yu on 2017/2/20.
 * 返回
 */

public abstract class CallBack<T> {
    private Context mContext;

    public CallBack() {
        this(null);
    }

    public CallBack(Context context) {
        mContext = context;
    }

    /**
     * CallBack onFailed
     */
    @SuppressWarnings("WeakerAccess")
    protected abstract void onFailed(@NonNull HttpError error);

    /**
     * CallBack onFailed runOnUiThread
     */
    public final void runOnUiThreadFailed(@NonNull final HttpError error) {
        // Step 1. 检测是否需要终端操作
        boolean interruption = checkInterruptionFailed(error);
        if (interruption) {
            DEBUG.d(error.getLogtag(), "sendFailResult: interruption");
            return;
        }

        // Step 2. 重置错误信息的数据
        String parsedMessage = parseUniteErrorMessage(error);
        if (TextUtils.isEmpty(parsedMessage)) {
            parsedMessage = parseErrorMessage(error);
        }

        if (!TextUtils.isEmpty(parsedMessage)) {
            error.setMessage(parsedMessage);
        }


        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onFailed(error);
            }
        });
    }

    /**
     * CallBack onSuccess
     */
    @SuppressWarnings("WeakerAccess")
    protected abstract void onSuccess(T result);

    /**
     * CallBack onFailed runOnUiThread
     */
    public final void runOnUiThreadSuccessful(@NonNull final T response) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response);
            }
        });
    }

    /**
     * CallBack parseResponse
     */
    public abstract T parseResponse(Response response);

    /**
     * 检测是否需要中断处理
     * <p>
     * 例如： 如果一个请求如果canceled了 或者context 已经finish了 那么再进行返回就报错了
     */
    @SuppressWarnings("WeakerAccess")
    protected boolean checkInterruptionFailed(@NonNull HttpError error) {
        if (mContext != null && mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (activity.isFinishing()) {
                DEBUG.d(error.getLogtag(), StringUtils.plusString(error.getId(), " on Error activity is finishing to do nothing "));
                return true;
            }
        }

        if (error.getException() instanceof CanceledException) {
            DEBUG.d(error.getLogtag(), StringUtils.plusString(error.getId(), "  is canceled "));
            return true;
        }
        return false;
    }


    /**
     * 解析统一的返回错误信息
     */
    @SuppressWarnings("WeakerAccess")
    protected String parseUniteErrorMessage(@NonNull HttpError error) {
        Exception e = error.getException();
        Resources resources = Http.getResources();

        if (e instanceof ConnectException) {
            return resources.getString(R.string.net_poor_connections);
        } else if (e instanceof SocketTimeoutException) {
            return resources.getString(R.string.net_server_error);
        } else if (e instanceof UnknownHostException) {
            return resources.getString(R.string.net_server_error);
        }
        return null;
    }

    /**
     * 如果错误信息要单独处理，就重写这个方法进行处理
     */
    @SuppressWarnings("WeakerAccess,unused")
    protected String parseErrorMessage(HttpError error) {
        return null;
    }


}
