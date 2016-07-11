package com.bright.common.utils.http.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * 普通字符回调
 */
public abstract class StringCallBack extends CallBack<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }

}
