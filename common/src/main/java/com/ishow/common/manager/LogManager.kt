package com.ishow.common.manager

import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.annotation.Keep
import com.ishow.common.entries.http.HttpResponse
import com.ishow.common.extensions.versionName
import com.ishow.common.modules.log.InitLogWorker
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by yuhaiyang on 2019-11-01.
 * 日志管理
 */
@Keep
class LogManager private constructor() {

    companion object {

        @Volatile
        private var sInstance: LogManager? = null

        val instance: LogManager
            get() = sInstance ?: synchronized(LogManager::class.java) {
                sInstance ?: LogManager().also { sInstance = it }
            }

        fun init(context: Context) {

            if (isDebug(context)) {
                return
            }
            InitLogWorker().work(context)
        }

        fun initKey(context: Context) = "log_" + context.versionName().replace(".", "_")


        private fun isDebug(context: Context): Boolean {
            if(context.applicationInfo == null){
                return true
            }

            return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        }
    }


    @Keep
    class V(var status: Int? = 0, var message: String? = null)

    @JvmSuppressWildcards
    interface S {
        companion object {
            const val url: String = "https://api.i-show.club/ess/log/"
        }

        @POST("init")
        fun init(@Body params: Map<String, Any>): HttpResponse<V>
    }
}