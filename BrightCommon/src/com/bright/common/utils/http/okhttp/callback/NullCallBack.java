package com.bright.common.utils.http.okhttp.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Bright.Yu on 2016/5/6.
 */
public class NullCallBack extends CallBack {

    @Override
    public Object parseNetworkResponse(Response response, int id) throws Exception {
        return null;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(Object response, int id) {

    }
}
