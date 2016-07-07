package com.bright.common.utils.http.okhttp.callback;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by zhy on 15/12/14.
 */
public abstract class StringCallBack extends CallBack<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws IOException {
        return response.body().string();
    }

}
