package com.ishow.noah.manager

import com.ishow.noah.data.retrofit.AppRestService
import com.ishow.noah.utils.http.okhttp.interceptor.AppHttpInterceptor
import com.ishow.noah.utils.http.okhttp.interceptor.OkHttpLogInterceptor
import com.ishow.noah.utils.http.retrofit.convert.AppConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitManager private constructor() {

    val appService: AppRestService by lazy {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        val okBuilder = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(AppHttpInterceptor())
                .addInterceptor(OkHttpLogInterceptor())

        val retrofit = Retrofit.Builder()
                .baseUrl(AppRestService.BASE_URL)
                .client(okBuilder.build())
                .addConverterFactory(AppConverterFactory.create())
                .build()

        retrofit.create(AppRestService::class.java)
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