package com.bright.common.utils.http.okhttp.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 一个什么也不做的CallBack
 */
public class EmptyCallBack extends CallBack {

    @Override
    public Object parseNetworkResponse(Response response, int id) throws Exception {
        return null;
    }

    @Override
    public void onError(Call call, Exception e, String errorMessage, int errorType, int id) {

    }

    @Override
    public void onSuccess(Object result, int id) {
        
    }


}
