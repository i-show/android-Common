package com.ishow.noah.modules.sample.detail.extend.download

import android.util.Log
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.ishow.noah.App
import java.io.File

class Test {
    init {
        Aria.download(this).register()
    }

    fun start() {
        val path = App.app.getExternalFilesDir("apk")!!
        val file = File(path, "wx_others_${System.currentTimeMillis()}.apk")

        Aria.download(this)
            .load(url) //读取下载地址
            .setFilePath(file.absolutePath)
            .create()
    }

    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        Log.i("yhy", "running1111 : = " + task.percent)
    }

    @Download.onTaskComplete
    fun complete(task: DownloadTask) {
        Log.i("yhy", "complete111 size = ${task.fileSize} ,  path = ${task.filePath}}" )
    }

    companion object {
        private const val url = "https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk"
    }
}