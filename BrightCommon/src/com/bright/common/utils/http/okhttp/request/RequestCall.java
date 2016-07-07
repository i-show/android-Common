package com.bright.common.utils.http.okhttp.request;

import com.bright.common.utils.http.okhttp.OkHttpUtils;
import com.bright.common.utils.http.okhttp.callback.CallBack;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/12/15.
 * 对OkHttpRequest的封装，对外提供更多的接口：cancel(),mReadTimeOut()...
 */
public class RequestCall {
    private OkHttpRequest mHttpRequest;
    private Request mRequest;
    private Call mCall;

    private long mReadTimeOut;
    private long mWriteTimeOut;
    private long mConnTimeOut;

    private OkHttpClient mCloneClient;

    public RequestCall(OkHttpRequest request) {
        mHttpRequest = request;
    }

    /**
     * @param readTimeOut 单位为毫秒
     */
    public RequestCall readTimeOut(long readTimeOut) {
        mReadTimeOut = readTimeOut;
        return this;
    }

    /**
     * @param writeTimeOut 单位为毫秒
     */
    public RequestCall writeTimeOut(long writeTimeOut) {
        mWriteTimeOut = writeTimeOut;
        return this;
    }

    /**
     * @param connTimeOut 单位为毫秒
     */
    public RequestCall connTimeOut(long connTimeOut) {
        mConnTimeOut = connTimeOut;
        return this;
    }

    public Call buildCall(CallBack callback) {
        mRequest = generateRequest(callback);

        if (mReadTimeOut > 0 || mWriteTimeOut > 0 || mConnTimeOut > 0) {
            mReadTimeOut = mReadTimeOut > 0 ? mReadTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            mWriteTimeOut = mWriteTimeOut > 0 ? mWriteTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;
            mConnTimeOut = mConnTimeOut > 0 ? mConnTimeOut : OkHttpUtils.DEFAULT_MILLISECONDS;

            mCloneClient = OkHttpUtils.getInstance().getOkHttpClient().newBuilder()
                    .readTimeout(mReadTimeOut, TimeUnit.MILLISECONDS)
                    .writeTimeout(mWriteTimeOut, TimeUnit.MILLISECONDS)
                    .connectTimeout(mConnTimeOut, TimeUnit.MILLISECONDS)
                    .build();

            mCall = mCloneClient.newCall(mRequest);
        } else {
            mCall = OkHttpUtils.getInstance().getOkHttpClient().newCall(mRequest);
        }
        return mCall;
    }

    private Request generateRequest(CallBack callback) {
        return mHttpRequest.generateRequest(callback);
    }


    public void execute(CallBack callback) {
        buildCall(callback);

        if (callback != null) {
            callback.onBefore(mRequest, getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this, callback);
    }

    public Call getCall() {
        return mCall;
    }

    public Request getRequest() {
        return mRequest;
    }

    public OkHttpRequest getOkHttpRequest() {
        return mHttpRequest;
    }

    public Response execute() throws IOException {
        buildCall(null);
        return mCall.execute();
    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
