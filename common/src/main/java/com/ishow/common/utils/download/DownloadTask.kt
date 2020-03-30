package com.ishow.common.utils.download

import androidx.annotation.IntRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.RandomAccessFile
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 下载的任务
 */
class DownloadTask(private var httpClient: OkHttpClient) : DownloadJob.OnCallBack {
    private var url: String? = null

    /**
     * 保存的路径
     */
    private var savePath: String? = null

    /**
     * 保存的名称
     */
    private var saveName: String? = null

    /**
     * 下载的线程数
     */
    private var threadNumber: Int = 3

    private val progress = AtomicLong(0)

    /**
     * 进度监听
     */
    private var onProgressListener: DownloadManager.OnProgressListener? = null
    private var onProgressBlock: OnDownloadProgressBlock? = null

    /**
     * 状态改变
     */
    private var onStatusChangedListener: DownloadManager.OnStatusChangedListener? = null
    private var onStatusChangedBlock: OnDownloadStatusChangedBlock? = null

    /**
     * 是否终止
     */
    private var isIntercept: Boolean = false

    private var totalLength = 0L
    private val jobList = mutableListOf<DownloadJob>()

    private fun init() {
    }


    @Throws(Exception::class)
    fun start() {
        if (!checkParams()) {
            return
        }

        init()
        isIntercept = false
        download(url!!)
    }


    private fun download(url: String) = GlobalScope.launch(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = try {
            httpClient.newCall(request).execute()
        } catch (e: Exception) {
            notifyDownloadFailed(DownloadError.REQUEST_ERROR, e.toString())
            return@launch
        }

        val body = response.body()
        // 1. 检测是否请求成功
        if (!response.isSuccessful) {
            notifyDownloadFailed(DownloadError.REQUEST_ERROR, response.message())
            return@launch
        }

        if (body == null) {
            notifyDownloadFailed(DownloadError.REQUEST_BODY_EMPTY, response.message())
            return@launch
        }
        jobList.clear()
        val file = getSaveFile(response)
        totalLength = body.contentLength()
        if (totalLength > 0) {
            val randOut = RandomAccessFile(file, "rw")
            randOut.setLength(totalLength)
            randOut.close()
        }

        val distance: Long = (totalLength + threadNumber) / threadNumber
        val manager = DownloadManager.instance
        for (i in 0..threadNumber) {
            val info = DownloadInfo(i, url, file)
            info.start = i * distance
            info.end = info.start + distance

            val job = DownloadJob(httpClient, info, this@DownloadTask)
            jobList.add(job)
            manager.addDownloadJob(job)
        }
    }

    override fun onLengthChanged(length: Int) {
        val now = progress.addAndGet(length.toLong())
        notifyProgressChanged(now)
    }

    override fun onFinished(info: DownloadInfo) {
        for (job in jobList) {
            if (!job.isFinished) return
        }

        notifyDownloadComplete(info.saveFile)
    }

    /**
     * 终止下载
     */
    fun intercept() {
        isIntercept = true
    }

    /**
     * 下载url
     */
    fun url(url: String): DownloadTask {
        this.url = url
        return this
    }


    /**
     * 线程池中最多多少个线程
     */
    fun threadNumber(@IntRange(from = 1) number: Int): DownloadTask {
        this.threadNumber = number
        return this
    }

    /**
     * 超时时间设置
     */
    fun timeOut(long: Long, unit: TimeUnit) {
        httpClient = httpClient.newBuilder()
            .connectTimeout(long, unit)
            .writeTimeout(long, unit)
            .readTimeout(long, unit)
            .build()
    }

    fun savePath(savePath: String): DownloadTask {
        this.savePath = savePath
        return this
    }

    fun saveName(saveName: String): DownloadTask {
        this.saveName = saveName
        return this
    }

    fun setOnProgressListener(listener: DownloadManager.OnProgressListener): DownloadTask {
        onProgressListener = listener
        return this
    }

    fun setOnProgressListener(listener: OnDownloadProgressBlock): DownloadTask {
        onProgressBlock = listener
        return this
    }

    fun setOnStatusChangedListener(listener: DownloadManager.OnStatusChangedListener): DownloadTask {
        onStatusChangedListener = listener
        return this
    }

    fun setOnStatusChangedListener(listener: OnDownloadStatusChangedBlock): DownloadTask {
        onStatusChangedBlock = listener
        return this
    }

    /**
     * 检测必要参数是否有效
     */
    private fun checkParams(): Boolean {
        when {
            url.isNullOrEmpty() -> {
                throw IllegalStateException("url is empty")
            }
            savePath.isNullOrEmpty() -> {
                throw IllegalStateException("savePath is empty")
            }

            else -> return true
        }
    }


    /**
     * 获取保存的文件
     */
    private fun getSaveFile(response: Response): File {
        val path = getFinalFilePath()
        val name = getFinalFileName(response)
        val file = File(path, name)
        if (file.exists()) {
            file.renameTo(File(path, "$name.bak"))
        }
        return file
    }

    /**
     * 获取最终保存文件名称
     */
    private fun getFinalFilePath(): File {
        val file = File(savePath!!)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 获取最终保存文件名称
     */
    private fun getFinalFileName(response: Response): String {
        val name = saveName
        if (!name.isNullOrEmpty()) {
            return name
        }

        val fileName = getFileNameByResponse(response)
        if (!fileName.isNullOrEmpty()) {
            return fileName
        }

        val url = response.request().url().url().toString()
        return getFileNameByUrl(url)
    }


    private fun notifyDownloadFailed(code: Int, message: String) {
        val info = DownloadStatusInfo(DownloadStatus.Failed)
        info.errorCode = code
        info.errorMessage = message

        onStatusChangedBlock?.let { it(info) }
        onStatusChangedListener?.onStatusChanged(info)
    }

    private fun notifyDownloadComplete(file: File) {
        val info = DownloadStatusInfo(DownloadStatus.Complete)
        info.file = file

        onStatusChangedBlock?.let { it(info) }
        onStatusChangedListener?.onStatusChanged(info)
    }

    private fun notifyProgressChanged(progress: Long) {
        onProgressBlock?.invoke(progress, totalLength)
        onProgressListener?.progress(progress, totalLength)
    }

    companion object {
        /**
         * 通过返回值来获取文件名称
         */
        private fun getFileNameByResponse(response: Response): String? {

            var fileName = response.header("Content-Disposition")
            if (fileName.isNullOrBlank()) {
                return null
            }

            val index = fileName.indexOf("filename=")
            if (index == -1) {
                return null
            }

            fileName = fileName.substring(index + 9)
            fileName = fileName.replace("\"", "")
            return fileName
        }

        /**
         * 通过Url来获取FileName
         */
        private fun getFileNameByUrl(url: String): String {
            return url.substring(url.lastIndexOf("/") + 1)
        }
    }


}