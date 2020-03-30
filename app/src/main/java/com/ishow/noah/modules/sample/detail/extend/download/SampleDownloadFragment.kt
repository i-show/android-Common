package com.ishow.noah.modules.sample.detail.extend.download

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ishow.common.utils.download.DownloadManager
import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleDownloadBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_sample_download.*

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
                .setOnStatusChangedListener { Log.i("yhy", "info1 = $it") }
                .start()
        }catch (e:Exception){
            Log.e("yhy", "download1: e = $e")
        }

    }

    private fun update1(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：$current / $total, 耗时：$time"
        mainThread { status1.text = text }
    }


    private fun download2() {
        val start = System.currentTimeMillis()
        DownloadManager.newTask()
            .url("https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk")
            .threadNumber(2)
            .saveName("weixin2.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update2(current, total, start) }
            .setOnStatusChangedListener { Log.i("yhy", "info2 = $it") }
            .start()
    }

    private fun update2(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：$current / $total, 耗时：$time"
        mainThread { status2.text = text }
    }


    private fun download3() {
        val start = System.currentTimeMillis()
        DownloadManager.newTask()
            .url("https://imtt.dd.qq.com/16891/apk/A9CF9330B8F98FDA0702745A0EA2BDFC.apk")
            .threadNumber(3)
            .saveName("weixin3.apk")
            .savePath(requireContext().getExternalFilesDir("apk")!!.absolutePath)
            .setOnProgressListener { current, total -> update3(current, total, start) }
            .setOnStatusChangedListener { Log.i("yhy", "info3 = $it") }
            .start()
    }

    private fun update3(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：$current / $total, 耗时：$time"
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
            .setOnStatusChangedListener { Log.i("yhy", "info4 = $it") }
            .start()
    }

    private fun update4(current: Long, total: Long, startTime: Long) {
        val time = (System.currentTimeMillis() - startTime) / 1000
        val text = "下载进度：$current / $total, 耗时：$time"
        mainThread { status4.text = text }
    }
}