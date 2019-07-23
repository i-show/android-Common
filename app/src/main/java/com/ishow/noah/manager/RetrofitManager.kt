package com.ishow.noah.manager

import com.ishow.noah.data.retrofit.AppRestService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager private constructor() {
    
    val appService: AppRestService by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(AppRestService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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