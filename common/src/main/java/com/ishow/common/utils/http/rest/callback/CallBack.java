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

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.ishow.common.R;
import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.http.rest.exception.CanceledException;
import com.ishow.common.utils.http.rest.exception.HttpErrorException;
import com.ishow.common.utils.http.rest.request.Request;
import com.ishow.common.utils.http.rest.response.Response;
import com.ishow.common.utils.log.LogManager;

import java.io.IOException;
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
     * OnStatusViewListener onFailed
     *
     * @param error 错误信息的的组合对象
     */
    protected abstract void onFailed(@NonNull HttpError error);

    /**
     * OnStatusViewListener onFailed runOnUiThread
     */
    public final void runOnUiThreadFailed(@NonNull final HttpError error) {
        // 输出Debug信息
        debugForFailed(error);

        // Step 1. 检测是否需要终端操作
        boolean interruption = checkInterruptionFailed(error);
        if (interruption) {
            LogManager.d(error.getLogtag(), "sendFailResult: interruption");
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
     * OnStatusViewListener onSuccess
     *
     * @param result 返回成功的组合对象
     */
    protected abstract void onSuccess(T result);

    /**
     * OnStatusViewListener onFailed runOnUiThread
     */
    public final void runOnUiThreadSuccessful(@NonNull final Response response, @NonNull final T result) {
        debugForSuccess(response, result);

        // Step 1. 检测是否需要终端操作
        boolean interruption = checkInterruptionSuccessed(response);
        if (interruption) {
            LogManager.d(response.getLogtag(), "sendFailResult: interruption");
            return;
        }

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(result);
            }
        });
    }

    /**
     * OnStatusViewListener parseResponse
     */
    public abstract T parseResponse(@NonNull final Request request, @NonNull final Response response) throws HttpErrorException;

    /**
     * 检测是否需要中断处理
     * <p>
     * 例如： 如果一个请求如果canceled了 或者context 已经finish了 那么再进行返回就报错了
     */
    protected boolean checkInterruptionFailed(@NonNull HttpError error) {
        if (mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (activity.isFinishing()) {
                LogManager.d(error.getLogtag(), StringUtils.plusString(error.getId(), " on Error activity is finishing to do nothing "));
                return true;
            }
        }

        if (error.getException() instanceof CanceledException) {
            LogManager.d(error.getLogtag(), StringUtils.plusString(error.getId(), "  is canceled "));
            return true;
        }
        return false;
    }

    /**
     * 检测是否需要中断处理
     * <p>
     * 例如： 如果context 已经finish了 那么再进行继续操作了
     */
    protected boolean checkInterruptionSuccessed(@NonNull final Response response) {
        if (mContext != null && mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (activity.isFinishing()) {
                LogManager.d(response.getLogtag(), StringUtils.plusString(response.getId(), " on Error activity is finishing to do nothing "));
                return true;
            }
        }
        return false;
    }


    /**
     * 解析统一的返回错误信息
     */
    protected String parseUniteErrorMessage(@NonNull HttpError error) {
        Exception e = error.getException();
        Resources resources = Http.getResources();

        if (e instanceof ConnectException) {
            return resources.getString(R.string.net_poor_connections);
        } else if (e instanceof SocketTimeoutException) {
            return resources.getString(R.string.net_server_error);
        } else if (e instanceof UnknownHostException) {
            return resources.getString(R.string.net_server_error);
        } else if (e instanceof IOException) {
            return resources.getString(R.string.net_poor_connections);
        }
        return null;
    }

    /**
     * 如果错误信息要单独处理，就重写这个方法进行处理
     */
    protected String parseErrorMessage(HttpError error) {
        return null;
    }

    /**
     * 获取Context
     */
    protected Context getContext() {
        return mContext;
    }

    /**
     * 当Failed的时候打印debug信息
     */
    private void debugForFailed(@NonNull HttpError error) {
        if (!TextUtils.isEmpty(error.getMessage())) {
            LogManager.d(error.getLogtag(), StringUtils.plusString(error.getId(), " ERROR_MSG  = " + error.getMessage()));
        }

        if (error.getCode() != 0) {
            LogManager.d(error.getLogtag(), StringUtils.plusString(error.getId(), " ERROR_TYPE  = " + error.getCode()));
        }

        if (!TextUtils.isEmpty(error.getBody())) {
            LogManager.d(error.getLogtag(), StringUtils.plusString(error.getId(), " ERROR_BODY  = " + error.getBody()));
        }

        if (error.getException() == null) {
            error.setException(new Exception(error.getMessage()));
        }

        LogManager.d(error.getLogtag(), StringUtils.plusString(error.getId(), " EXCEPTION  = " + error.getException().toString()));
    }

    /**
     * 当Successed的时候打印debug信息
     */
    private void debugForSuccess(@NonNull final Response response, @NonNull final T result) {
        if (!TextUtils.isEmpty(response.getDebugString())) {
            LogManager.d(response.getLogtag(), StringUtils.plusString(response.getId(), " RESULT  = " + response.getDebugString()));
        } else {
            LogManager.d(response.getLogtag(), StringUtils.plusString(response.getId(), " RESULT  Success but no debugString or empty"));
        }
    }

}
