package com.ishow.common.modules.log

import android.content.Context
import android.os.Build
import com.ishow.common.BuildConfig
import com.ishow.common.extensions.appName
import com.ishow.common.extensions.toJSON
import com.ishow.common.extensions.versionName
import com.ishow.common.manager.LogManager
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.http.okhttp.interceptor.OkHttpLogInterceptor
import com.ishow.common.utils.http.retrofit.adapter.CallAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by yuhaiyang on 2019-11-01.
 * 日志工作
 */
class InitLogWorker {
    private val ss: LogManager.S by lazy {
        val logInterceptor = OkHttpLogInterceptor()
        logInterceptor.logTag = "LogWorker"

        val ok = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(logInterceptor)

        val re = Retrofit.Builder()
            .baseUrl(LogManager.S.url)
            .client(ok.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CallAdapterFactory())
            .build()
        re.create(LogManager.S::class.java)
    }

    fun work(context: Context) = GlobalScope.launch {
        val screenSize = DeviceUtils.screenSize
        val app = HashMap<String, Any?>()
        app["appId"] = context.packageName
        app["appName"] = context.appName
        app["appVersion"] = context.versionName
        app["commonVersion"] = BuildConfig.VERSION
        app["commonArtifact"] = BuildConfig.ARTIFACT_ID

        val device = HashMap<String, Any?>()
        device["deviceId"] = DeviceUtils.deviceId(context)
        device["type"] = "android"
        device["manufacturer"] = Build.MANUFACTURER
        device["model"] = Build.MODEL
        device["sdk"] = Build.VERSION.SDK_INT
        device["version"] = Build.DISPLAY
        device["sw"] = context.resources.configuration.smallestScreenWidthDp
        device["resolution"] = screenSize[0].toString() + "x" + screenSize[1]

        val params = HashMap<String, Any>()
        params["app"] = app
        params["device"] = device
        params["dateTime"] = System.currentTimeMillis()
        val response = ss.init(params)
        if (response.isSuccess()) {
            StorageUtils.save(LogManager.initKey(context), response.data?.toJSON())
        }
    }
}