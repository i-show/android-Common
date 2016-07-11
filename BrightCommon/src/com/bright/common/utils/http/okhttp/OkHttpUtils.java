package com.bright.common.utils.http.okhttp;

import com.bright.common.utils.http.okhttp.builder.GetBuilder;
import com.bright.common.utils.http.okhttp.builder.HeadBuilder;
import com.bright.common.utils.http.okhttp.builder.OtherRequestBuilder;
import com.bright.common.utils.http.okhttp.builder.PostFileBuilder;
import com.bright.common.utils.http.okhttp.builder.PostFormBuilder;
import com.bright.common.utils.http.okhttp.builder.PostStringBuilder;
import com.bright.common.utils.http.okhttp.callback.CallBack;
import com.bright.common.utils.http.okhttp.callback.EmptyCallBack;
import com.bright.common.utils.http.okhttp.request.RequestCall;
import com.bright.common.utils.http.okhttp.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * OKHttp
 */
public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";
    /**
     * 默认的回调时间
     */
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }


    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public Platform getPlatform() {
        return mPlatform;
    }


    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get() {
        return new GetBuilder();
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static HeadBuilder head() {
        return new HeadBuilder();
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    public void execute(final RequestCall requestCall, CallBack callback) {
        if (callback == null) {
            callback = new EmptyCallBack();
        }
        final CallBack finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                finalCallback.sendFailResult(call, e, e.toString(), 0, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                finalCallback.parseResponse(call, response, id);
            }
        });
    }


    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
}

