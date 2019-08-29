package com.ishow.common.manager

import android.content.Context
import android.os.Build
import android.util.Log
import com.ishow.common.BuildConfig
import com.ishow.common.entries.http.HttpResponse
import com.ishow.common.extensions.appName
import com.ishow.common.extensions.versionName
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.http.retrofit.adapter.CallAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object CCacheManager {

    private val logService: LogService by lazy {
        val okBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)

        val retrofit = Retrofit.Builder()
            .baseUrl(LogService.url)
            .client(okBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CallAdapterFactory())
            .build()
        retrofit.create(LogService::class.java)
    }

    fun cache(context: Context?) {
        context?.let {
            Log.i("yhy", "context = $context")
            if (!initPrecondition(it)) {
                return
            }
            cacheH(it)
        }
    }

    private fun initPrecondition(context: Context): Boolean {
        try {
            if (System.currentTimeMillis() - l < 300000) {
                return false
            }
            l = System.currentTimeMillis()
            // 是否ok
            val status = StorageUtils.with(context)
                .key(getStatusKey(context))
                .get(false)

            if (status) {
                return false
            }

            val firstTime = StorageUtils.with(context)
                .key(getStatusFirstTimeKey(context))
                .get(0L)

            if (firstTime == 0L) {
                StorageUtils.with(context)
                    .param(getStatusFirstTimeKey(context), System.currentTimeMillis())
                    .save()
            }

            if (System.currentTimeMillis() - firstTime < DateUtils.DAY_7) {
                return false
            }
            val lastTime = StorageUtils.with(context)
                .key(getStatusLastTimeKey(context))
                .get(0L)
            val deltaTime = System.currentTimeMillis() - lastTime

            if (deltaTime < DateUtils.HOUR_1) {
                return false
            }
        } catch (e: Exception) {
            return false
        }

        return true
    }

    private fun cacheH(context: Context) {
        val screenSize = DeviceUtils.screenSize
        val app = HashMap<String, Any?>()
        app["appId"] = context.packageName
        app["appName"] = context.appName()
        app["appVersion"] = context.versionName()
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

        GlobalScope.launch {
            val response = logService.init(params)
            if (response.isSuccess()) {
                formatResult(context, response.data)
            } else {
                prolongTime(context)
            }
        }

    }

    private fun prolongTime(context: Context) {
        StorageUtils.with(context)
            .param(getStatusLastTimeKey(context), System.currentTimeMillis())
            .save()
    }

    private fun formatResult(context: Context, result: Value?) {
        if (result == null) {
            prolongTime(context)
            return
        }
        //  0 不需要进行校验  1 需要进行校验，-1 以后可以不再进行校验
        val nowStatus = result.status

        if (nowStatus == null || nowStatus == 0) {
            prolongTime(context)
            return
        }

        if (nowStatus == -1) {
            StorageUtils.with(context)
                .param(getStatusKey(context), true)
                .save()
            return
        }

        when (result.status) {
            11 -> error11(context)
            12 -> throw IllegalAccessError("")
            21 -> error21(context, result)
        }
    }

    private fun error11(context: Context) {
        prolongTime(context)
        throw IllegalAccessError("")
    }

    private fun error21(context: Context, result: Value) {
        prolongTime(context)
        ToastUtils.show(context, result.message)
    }

    class Value(var status: Int? = 0, var message: String? = null)

    private var l: Long = 0L

    private fun getStatusKey(context: Context): String {
        return "key_status_" + context.versionName().replace(".", "_")
    }

    private fun getStatusLastTimeKey(context: Context): String {
        return "key_status_time_" + context.versionName().replace(".", "_")
    }

    private fun getStatusFirstTimeKey(context: Context): String {
        return "key_status_first_time_" + context.versionName().replace(".", "_")
    }


    @JvmSuppressWildcards
    interface LogService {
        companion object {
            const val url: String = "https://api.i-show.club/ess/log/"
        }

        @POST("init")
        fun init(@Body params: Map<String, Any>): HttpResponse<Value>
    }
}
