package com.ishow.common.utils.download

import android.content.Context
import okhttp3.OkHttpClient
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class DownloadManager private constructor() {

    /**
     * 线城池
     */
    private val threadPoll = Executors.newFixedThreadPool(10)

    private val client by lazy {
        OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    /**
     * 进度监听
     */
    interface OnProgressListener {
        /**
         * @param current 已下载长度
         * @param total 总长度
         */
        fun progress(current: Long, total: Long)
    }

    /**
     * 状态改变
     */
    interface OnStatusChangedListener {
        fun onStatusChanged(info: DownloadStatusInfo)
    }

    internal fun addDownloadJob(job: DownloadJob) {
        threadPoll.execute(job)
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { DownloadManager() }
        
        private const val DEFAULT_TIMEOUT = 60L
        private var appContext: Context? = null

        fun init(context: Context) {
            appContext = context.applicationContext
        }

        @JvmStatic
        fun newTask(): DownloadTask {
            if (appContext == null) {
                throw IllegalStateException("DownloadManager must init")
            }
            return DownloadTask(appContext!!, instance.client)
        }
    }
}