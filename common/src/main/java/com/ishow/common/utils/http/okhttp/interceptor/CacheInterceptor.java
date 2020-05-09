package com.ishow.common.utils.http.okhttp.interceptor;

import android.content.Context;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    Context context;
    final static int maxStale = 7 * 24 * 60;//分钟
    int maxAge = 6;//分钟

    public CacheInterceptor(Context context) {
        this.context = context;
    }

    public CacheInterceptor(Context context, int maxAgeOnline, int maxScaleOffline) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

//        if (checkNet(context)) {
//            Response response = chain.proceed(request);
//            return response.newBuilder()
//                    .removeHeader("Pragma")
//                    .removeHeader("Cache-Control")
//                    .header("Cache-Control", "public, max-age=" + maxAge)
//                    .build();
//        } else {
            request = request.newBuilder()
                    .cacheControl(FORCE_CACHE)
                    .build();
            Response response = chain.proceed(request);

            return response.newBuilder()
                    .build();
        // }
    }

    //这是设置在多长时间范围内获取缓存里面,缓存一周
    public static final CacheControl FORCE_CACHE = new CacheControl.Builder()
            .onlyIfCached()
            .maxStale(maxStale, TimeUnit.MINUTES)
            .build();
}