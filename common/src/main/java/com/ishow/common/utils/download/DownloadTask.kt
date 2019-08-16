package com.ishow.common.utils.download

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 下载的任务
 */
class DownloadTask {
    private var context: Context? = null
    private var url: String? = null
    private var savePath: String? = null
    private var saveName: String? = null
    private var onProgressListener: DownloadManager.OnProgressListener? = null
    private var onStatusChangedListener: DownloadManager.OnStatusChangedListener? = null
    /**
     * 是否终止
     */
    private var isIntercept: Boolean = false

    private lateinit var mDownloadService: DownloadService

    private fun init() {
        val okBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)

        onProgressListener?.let {
            okBuilder.addInterceptor(DownloadInterceptor(onProgressListener))
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.ishow.club/")
            .client(okBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mDownloadService = retrofit.create(DownloadService::class.java)
    }


    @Throws(IOException::class)
    fun start() {
        if (!checkParams()) {
            return
        }

        init()
        isIntercept = false
        val finalUrl = url!!

        GlobalScope.launch(Dispatchers.IO) {
            val response = mDownloadService.download(finalUrl).execute()
            val body = response.body()
            // 1. 检测是否请求成功
            if (!response.isSuccessful) {
                onStatusChangedListener?.onFailed(DownloadError.REQUEST_ERROR, response.message())
                return@launch
            }

            if (body == null) {
                onStatusChangedListener?.onFailed(DownloadError.REQUEST_BODY_EMPTY, response.message())
                return@launch
            }

            // 2. 创建文件，准备保存
            val file = getSaveFile(response.raw())
            val buffer = ByteArray(8192)
            val inputStream = body.byteStream()
            val outputStream = file.outputStream()

            // 3. 开始保存文件
            do {
                val length = inputStream.read(buffer)
                if (length < 0) {
                    withContext(Dispatchers.Main) {
                        onStatusChangedListener?.onComplete(file)
                    }
                    break
                }
                outputStream.write(buffer, 0, length)
            } while (!isIntercept)

            inputStream.close()
            outputStream.flush()
            outputStream.close()
        }
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
     * context
     */
    fun context(context: Context): DownloadTask {
        this.context = context
        return this
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

    fun setOnStatusChangedListener(listener: DownloadManager.OnStatusChangedListener): DownloadTask {
        onStatusChangedListener = listener
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


    companion object {
        /**
         * 默认 超时时间
         */
        const val DEFAULT_TIMEOUT = 60L

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