package com.ishow.noah.modules.main.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.common.HttpOption
import com.arialyy.aria.core.download.m3u8.M3U8VodOption
import com.arialyy.aria.core.task.DownloadTask
import com.ishow.common.extensions.dp2px
import com.ishow.common.extensions.toJSON
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.load.LoadSir
import com.ishow.common.widget.load.Loader
import com.ishow.common.widget.load.ext.sirEmpty
import com.ishow.common.widget.load.ext.sirLoading
import com.ishow.common.widget.load.ext.withLoadSir
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.ui.widget.load.AppEmptyLoad
import com.ishow.noah.ui.widget.load.AppLoadingStatus
import kotlinx.android.synthetic.main.f_home.*


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loader = Loader.new()
            .empty(AppEmptyLoad())
            .loading(AppLoadingStatus())
            .build()
        LoadSir.init(loader = loader)

        test1.withLoadSir()
            .emptyText(R.id.empty, "Hello Empty2222")
            .marginTop(40.dp2px())

        val aa = AAA(1)
        loading.setOnClickListener { test1.sirLoading() }
        loading2.setOnClickListener { test1.sirEmpty() }
        dis.setOnClickListener {
            Log.i("yhy", "aa = "+ aa.toJSON())
            test(aa)
            Log.i("yhy", "bb = "+ aa.toJSON())
        }



    }

    data class AAA(var status: Int)

    private fun test(a:AAA) {
        a.status = 11
    }

    private fun startDownload() {
        val option = M3U8VodOption()
        option.merge(true)

        val httpOption = HttpOption()
        httpOption.useServerFileName(true)

        val id = Aria.download(this)
            .load("https://douban.donghongzuida.com/20201013/10932_816f6065/index.m3u8")     //读取下载地址
            .setFilePath(requireActivity().getExternalFilesDir("m3u8")!!.absolutePath + "/a.m3u8") //设置文件保存的完整路径
            .ignoreCheckPermissions()
            .ignoreFilePathOccupy()
            //.m3u8VodOption(option)
            .option(httpOption)
            .create()

        Log.i("yhy", "id = $id")
    }

    @Keep
    @Download.onTaskRunning
    fun running(task: DownloadTask) {
        Log.i("yhy", "=================================")
        Log.i("yhy", "currentProgress = " + task.currentProgress)
        Log.i("yhy", "convertCurrentProgress = " + task.convertCurrentProgress)
        Log.i("yhy", "speed = " + task.speed)
        Log.i("yhy", "extendField = " + task.percent)
        Log.i("yhy", "key = " + task.key)

    }

    @Keep
    @Download.onTaskComplete
    fun com(task: DownloadTask) {
        Log.i("yhy", "onTaskComplete = " + task.currentProgress)
    }

    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, { PrintView.print(it.toString()) })
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .action("com.yuhaiyang.androidcommon.Test")
            .start()
    }

    companion object {

        fun newInstance(): HomeFragment {

            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}