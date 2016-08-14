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

package com.bright.common.utils.http.okhttp.callback;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.utils.StringUtils;
import com.bright.common.utils.debug.DEBUG;
import com.bright.common.utils.http.okhttp.OkHttpUtils;
import com.bright.common.utils.http.okhttp.exception.CanceledException;
import com.bright.common.widget.YToast;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class CallBack<T> {
    private static final String TAG = "CallBack";
    protected Context mContext;
    protected boolean isShowTip;
    protected String mLogTag;

    public CallBack() {
        this(null, false);
    }

    public CallBack(Context context) {
        this(context, true);
    }

    public CallBack(Context context, boolean showTip) {
        mContext = context;
        isShowTip = showTip;
    }

    /**
     * UI Thread
     */
    public void onBefore(Request request, int id) {
        // TODO
    }

    /**
     * UI Thread
     */
    public void onAfter(int id) {
        // TODO
    }

    /**
     * UI Thread
     */
    public void inProgress(final float progress, final long total, final int id) {

    }

    /**
     * 检测是否有效
     */
    public boolean validateReponse(final Call call, final Response response, final int id) throws Exception {
        boolean isValid = response.isSuccessful();
        if (!isValid) {
            String message = "request failed , reponse's code is : " + response.code();
            sendFailResult(call, new IOException(message), null, 0, id);
        }
        return isValid;
    }

    /**
     * Thread Pool Thread
     * 最终结果
     */
    public abstract void generateFinalResult(final Call call, final Response response, final int id) throws Exception;


    /**
     * 解析内容
     */
    public void parseResponse(final Call call, final Response response, final int id) {
        if (call.isCanceled()) {
            sendFailResult(call, new CanceledException(), null, 0, id);
            return;
        }

        try {
            if (!validateReponse(call, response, id)) {
                return;
            }
            generateFinalResult(call, response, id);
        } catch (Exception e) {
            sendFailResult(call, e, null, 0, id);
        } finally {
            if (response.body() != null) {
                response.body().close();
            }
        }
    }


    /**
     * @param call         请求的Call
     * @param e            错误信息
     * @param errorMessage 错误信息
     * @param errorType    错误的类型
     * @param id           请求的ID
     * @return true 错误类型已经被处理 ， false 错误类型未被处理
     */
    public boolean onError(Call call, Exception e, String errorMessage, int errorType, int id) {
        // 如果不进行提示消息 那么直接返回false
        if (!isShowTip) {
            return false;
        }
        try {
            if (e instanceof ConnectException) {
                toast(R.string.net_poor_connections);
                return true;
            } else if (e instanceof SocketTimeoutException) {
                toast(R.string.net_server_error);
                return true;
            } else if (e instanceof UnknownHostException) {
                toast(R.string.net_server_error);
                return true;
            } else if (e instanceof CanceledException) {
                Log.d(TAG, "onError: call is canceled");
                return true;
            } else if (!TextUtils.isEmpty(errorMessage)) {
                toast(errorMessage);
                return true;
            }
        } catch (WindowManager.BadTokenException tokenerror) {
            Log.i(TAG, "onError: tokenerror =" + tokenerror);
        }

        return false;
    }

    public abstract void onSuccess(T result, int id);


    public void setLogTag(String tag) {
        mLogTag = tag;
    }

    public void sendFailResult(final Call call, final Exception e, final String errorMessage, final int errorType, final int id) {
        if (!TextUtils.isEmpty(errorMessage)) {
            DEBUG.d(mLogTag, StringUtils.plusString(id, " ERROR_MSG  = " + errorMessage));
        }

        if (errorType != 0) {
            DEBUG.d(mLogTag, StringUtils.plusString(id, " ERROR_TYPE  = " + errorType));
        }

        if (e != null) {
            DEBUG.d(mLogTag, StringUtils.plusString(id, " EXCEPTION  = " + e.toString()));
        }

        OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
            @Override
            public void run() {
                onError(call, e, errorMessage, errorType, id);
                onAfter(id);
            }
        });
    }

    protected void sendSuccessResultCallback(final T result, final int id) {
        DEBUG.d(mLogTag, StringUtils.plusString(id, " RESULT  = " + result));

        if (mContext != null && mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            if (activity.isFinishing()) {
                DEBUG.d(mLogTag, StringUtils.plusString(id, " activity is finishing to do nothing "));
                return;
            }
        }

        OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
            @Override
            public void run() {
                onSuccess(result, id);
                onAfter(id);
            }
        });
    }

    public void toast(int toast) {
        if (mContext != null) {
            toast(mContext.getString(toast));
        }
    }

    public void toast(String toast) {
        if (mContext != null) {
            YToast.makeText(mContext.getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
        }
    }
}