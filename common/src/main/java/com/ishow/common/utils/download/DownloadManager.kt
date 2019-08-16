package com.ishow.common.utils.download

import com.ishow.common.utils.download.DownloadTask
import java.io.File


class DownloadManager {


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


    interface OnStatusChangedListener{
        /**
         * @param status 1：下载开始； 2：下载完成  3：下载失败
         * @param message 下载提示语
         */
        fun onFailed(status: Int, message: String)

        /**
         * 下载完成
         */
        fun onComplete(file: File)
    }

    companion object {

        @JvmStatic
        fun newTask(): DownloadTask {
            return DownloadTask()
        }
    }
}