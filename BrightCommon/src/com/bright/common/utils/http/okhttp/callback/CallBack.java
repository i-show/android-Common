package com.bright.common.utils.http.okhttp.callback;

import com.bright.common.utils.http.okhttp.OkHttpUtils;
import com.bright.common.utils.http.okhttp.exception.CanceledException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class CallBack<T> {
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
    public boolean validateReponse(Response response, int id) throws Exception {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;


    /**
     * 解析内容
     */
    public void parseResponse(final Call call, final Response response, final int id) {
        if (call.isCanceled()) {
            sendFailResult(call, new CanceledException(), null, 0, id);
            return;
        }

        try {
            if (!validateReponse(response, id)) {
                sendFailResult(call, new IOException("request failed , reponse's code is : " + response.code()), null, 0, id);
                return;
            }

            T t = parseNetworkResponse(response, id);
            sendSuccessResultCallback(t, id);
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
    public abstract boolean onError(Call call, Exception e, String errorMessage, int errorType, int id);

    public abstract void onSuccess(T result, int id);


    public void sendFailResult(final Call call, final Exception e, final String errorMessage, final int errorType, final int id) {
        OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
            @Override
            public void run() {
                onError(call, e, errorMessage, errorType, id);
                onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final T result, final int id) {

        OkHttpUtils.getInstance().getPlatform().execute(new Runnable() {
            @Override
            public void run() {
                onSuccess(result, id);
                onAfter(id);
            }
        });
    }
}