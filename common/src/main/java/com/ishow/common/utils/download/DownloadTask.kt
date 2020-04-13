package com.ishow.common.utils.download

import android.content.Context
import android.util.Log
import androidx.annotation.IntRange
import com.ishow.common.extensions.delay
import com.ishow.common.extensions.mainThread
import com.ishow.common.utils.download.db.DownloadDB
import com.ishow.common.utils.download.db.DownloadData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.RandomAccessFile
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong

/**
 * 下载的任务
 */
class DownloadTask(context: Context, private var httpClient: OkHttpClient) : DownloadJob.OnCallBack {
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

    private var totalLength = 0L
    private val jobList = mutableListOf<DownloadJob>()

    private val savePull by lazy { Executors.newSingleThreadExecutor() }
    private val downloadDao by lazy { DownloadDB.get(context).getDownloadDao() }
    private var _isDownloading = false
    val isDownloading
        get() = _isDownloading

    @Throws(Exception::class)
    fun start(): DownloadTask {
        if (isDownloading) return this
        if (!checkParams()) {
            return this
        }
        download(url!!)
        return this
    }

    fun resume(): DownloadTask {
        if (isDownloading) return this

        if (!checkParams()) {
            return this
        }
        resumeDownload(url!!)
        return this
    }

    /**
     * 终止下载
     */
    fun intercept() {
        jobList.forEach { it.intercept = true }
    }

    fun pause() {
        jobList.forEach { it.intercept = true }
    }


    private fun resumeDownload(url: String) = GlobalScope.launch(Dispatchers.IO) {
        val file = prepareDownload(url, true) ?: return@launch
        if (file.length() <= 0) {
            download(url)
            return@launch
        }

        val dataList = downloadDao.getData()
        val manager = DownloadManager.instance

        _isDownloading = true

        progress.set(0)
        dataList?.forEach {
            progress.addAndGet(it.downloadLength)

            val info = DownloadInfo(it.id, url, file)
            info.start = it.start
            info.end = it.end
            info.downloadLength = it.downloadLength

            val job = DownloadJob(httpClient, info, this@DownloadTask)
            jobList.add(job)
            manager.addDownloadJob(job)
        }
    }

    private fun download(url: String) = GlobalScope.launch(Dispatchers.IO) {
        val file = prepareDownload(url) ?: return@launch

        if (totalLength > 0) {
            val randOut = RandomAccessFile(file, "rw")
            randOut.setLength(totalLength)
            randOut.close()
        }

        val distance: Long = (totalLength + threadNumber) / threadNumber
        val manager = DownloadManager.instance

        jobList.clear()
        for (i in 0 until threadNumber) {
            val info = DownloadInfo(i, url, file)
            info.start = i * distance
            info.end = info.start + distance
            insertData(i, info, file)

            val job = DownloadJob(httpClient, info, this@DownloadTask)
            jobList.add(job)
            manager.addDownloadJob(job)
        }
    }

    private fun prepareDownload(url: String, resume: Boolean = false): File? {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = try {
            httpClient.newCall(request).execute()
        } catch (e: Exception) {
            notifyDownloadFailed(DownloadError.REQUEST_ERROR, e.toString())
            return null
        }

        val body = response.body()
        // 1. 检测是否请求成功
        if (!response.isSuccessful) {
            notifyDownloadFailed(DownloadError.REQUEST_ERROR, response.message())
            return null
        }

        if (body == null) {
            notifyDownloadFailed(DownloadError.REQUEST_BODY_EMPTY, response.message())
            return null
        }

        totalLength = body.contentLength()
        return getSaveFile(response, resume)
    }

    private fun updateData(info: DownloadInfo, length: Long) {
        savePull.execute {
            val data = DownloadData()
            data.id = info.id
            data.url = info.url
            data.start = info.start
            data.end = info.end
            data.downloadLength = info.downloadLength
            downloadDao.update(data)
        }
    }

    private fun insertData(id: Int, info: DownloadInfo, file: File) {
        savePull.execute {
            val data = DownloadData()
            data.id = id
            data.url = info.url
            data.start = info.start
            data.end = info.end
            data.downloadLength = 0L
            data.file = file.absolutePath
            downloadDao.insert(data)
        }
    }

    override fun onLengthChanged(info: DownloadInfo, length: Long) {
        updateData(info, length)
        val now = progress.addAndGet(length)
        notifyProgressChanged(now)
    }


    @Suppress("NON_EXHAUSTIVE_WHEN")
    override fun onStatusChanged(job: DownloadJob, info: DownloadInfo, msg: String?) {
        when (job.status) {
            DownloadJob.Status.Finished -> onDownloadFinished(info)
            DownloadJob.Status.Error -> onDownloadError(job)
        }
    }

    private fun onDownloadError(job: DownloadJob) {
        delay(2000) {
            val manager = DownloadManager.instance
            if (job.status == DownloadJob.Status.Error) {
                manager.addDownloadJob(job)
            }
        }
    }

    private fun onDownloadFinished(info: DownloadInfo) {
        for (job in jobList) {
            if (job.status != DownloadJob.Status.Finished) return
        }
        downloadDao.delete(info.url)
        notifyDownloadComplete(info.saveFile)
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
    private fun getSaveFile(response: Response, resume: Boolean): File {
        val path = getFinalFilePath()
        val name = getFinalFileName(response)
        val file = File(path, name)
        if (file.exists() && !resume) {
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


    private fun notifyDownloadFailed(code: Int, message: String) = mainThread {
        _isDownloading = false

        val info = DownloadStatusInfo(DownloadStatus.Failed)
        info.errorCode = code
        info.errorMessage = message

        onStatusChangedBlock?.let { it(info) }
        onStatusChangedListener?.onStatusChanged(info)
    }

    private fun notifyDownloadComplete(file: File) = mainThread {
        _isDownloading = false

        val info = DownloadStatusInfo(DownloadStatus.Complete)
        info.file = file

        onStatusChangedBlock?.let { it(info) }
        onStatusChangedListener?.onStatusChanged(info)
    }

    private fun notifyProgressChanged(progress: Long) = mainThread {
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