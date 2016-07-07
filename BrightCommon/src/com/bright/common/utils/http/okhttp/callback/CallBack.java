package com.bright.common.utils.http.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class CallBack<T> {
    /**
     * UI Thread
     */
    public void onBefore(Request request, int id) {
    }

    /**
     * UI Thread
     */
    public void onAfter(int id) {
    }

    /**
     * UI Thread
     */
    public void inProgress(float progress, long total, int id) {

    }

    /**
     * if you parse reponse code in parseNetworkResponse, you should make this method return true.
     */
    public boolean validateReponse(Response response, int id) {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     */
    public abstract T parseNetworkResponse(Response response, int id) throws Exception;

    /**
     * @param call 请求的Call
     * @param e    错误信息
     * @param id   请求的ID
     */
    public abstract void onError(Call call, Exception e, int id);

    public abstract void onResponse(T response, int id);

}