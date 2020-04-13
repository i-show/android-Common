package com.ishow.noah.modules.sample.detail.extend.download

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import com.ishow.common.extensions.toJSON
import com.ishow.common.utils.download.DownloadManager
import com.ishow.common.utils.download.DownloadStatusInfo
import com.ishow.common.utils.download.DownloadTask
import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleDownloadBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_sample_download.*
import java.io.File


/**
 * Created by yuhaiyang on 2020-03-05.
 */
class SampleDownloadFragment : AppBindFragment<FSampleDownloadBinding, SampleDownloadViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_download

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        download1.setOnClickListener { download1() }
        download2.setOnClickListener { download2() }
        download3.setOnClickListener { download3() }
        download4.setOnClickListener { download4() }
    }

    private fun download1() {

        val start = System.currentTimeMillis()
        try {
            DownloadManager.newTask()
                .url("https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk")
                .threadNumber(1)
                .saveName("weixin1.apk")
                .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
                .setOnProgressListener { current, total -> update1(current, total, start) }
                .setOnStatusChangedListener {
                    installApp(it) }
                .start()
        } catch (e: Exception) {
            Log.e("yhy", "download1: e = $e")
        }

    }

    private fun update1(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：$current / $total, 耗时：$time"
        mainThread { status1.text = text }
    }


    private var task2: DownloadTask? = null
    private fun download2() {
        val start = System.currentTimeMillis()
        if (task2 != null) {
            task2?.pause()
            return
        }

        task2 = DownloadManager.newTask()
            .url("https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk")
            .threadNumber(3)
            .saveName("weixin3.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update2(current, total, start) }
            .setOnStatusChangedListener { installApp(it) }
            .resume()
    }

    private fun update2(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：${current / total.toFloat()}, 耗时：$time"
        mainThread { status2.text = text }
    }

    private var task3: DownloadTask? = null
    private fun download3() {
        val start = System.currentTimeMillis()
        if (task3 != null) {
            task3?.pause()
            return
        }
        task3 = DownloadManager.newTask()
            .url("https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk")
            .threadNumber(3)
            .saveName("weixin3.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update3(current, total, start) }
            .setOnStatusChangedListener { installApp(it) }
            .start()
    }

    private fun update3(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：${current / total.toFloat()}, 耗时：$time"
        mainThread { status3.text = text }
    }


    private fun download4() {
        val start = System.currentTimeMillis()
        DownloadManager.newTask()
            .url("https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk")
            .threadNumber(4)
            .saveName("weixin4.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update4(current, total, start) }
            .setOnStatusChangedListener { installApp(it) }
            .start()
    }

    private fun update4(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：${current / total.toFloat()}, 耗时：$time"
        mainThread { status4.text = text }
    }

    private fun installApp(info: DownloadStatusInfo) {
        val file = info.file
        Log.i(TAG, "installApp: info = " + info.toJSON())
        Log.i(TAG, "installApp: file = " + file?.absoluteFile)
        if (file == null) return
        val context = context ?: return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        Log.i(TAG, "installApp: start ===" + isMainThread())
        context.startActivity(intent)
    }


    companion object {
        private const val TAG = "SampleDownloadFragment"
    }
}