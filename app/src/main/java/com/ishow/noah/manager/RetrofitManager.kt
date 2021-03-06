package com.ishow.noah.manager

import com.ishow.common.utils.http.okhttp.interceptor.OkHttpLogInterceptor
import com.ishow.noah.data.retrofit.ApiService
import com.ishow.noah.data.retrofit.LogService
import com.ishow.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import com.ishow.noah.utils.http.retrofit.adapter.AppCallAdapterFactory
import com.ishow.noah.utils.http.retrofit.convert.AppConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class RetrofitManager private constructor() {

    val appService: ApiService by lazy {

        val okBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(AppHttpInterceptor())
            .addInterceptor(OkHttpLogInterceptor())

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiService.BASE_URL)
            .client(okBuilder.build())
            .addConverterFactory(AppConverterFactory.create())
            .addCallAdapterFactory(AppCallAdapterFactory())
            .build()

        retrofit.create(ApiService::class.java)
    }

    @Suppress("ConstantConditionIf")
    val logService: LogService by lazy {
        val okBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(AppHttpInterceptor())
            .addInterceptor(OkHttpLogInterceptor())

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.baidu.com/")
            .client(okBuilder.build())
            .addConverterFactory(AppConverterFactory.create())
            .addCallAdapterFactory(AppCallAdapterFactory())
            .build()

        retrofit.create(LogService::class.java)
    }

    companion object {

        @Volatile
        private var sInstance: RetrofitManager? = null

        val instance: RetrofitManager
            get() =
                sInstance ?: synchronized(RetrofitManager::class.java) {
                    sInstance ?: RetrofitManager().also { sInstance = it }
                }
    }
}