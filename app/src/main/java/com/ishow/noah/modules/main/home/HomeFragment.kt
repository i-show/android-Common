package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
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
import com.ishow.common.extensions.*
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.http.ip.IpUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.announcement.IAnnouncementData
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


class Test(override val title: String) : IAnnouncementData {

}


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


        bu1.withAnimator()
            .shakeY()
            .repeatForever()
            .setDuration(2000L)
            .start()

        ann.data = makeData("AAAA")
    }

    private fun makeData(string: String): MutableList<IAnnouncementData> {
        val list = mutableListOf<IAnnouncementData>()
        list.add(Test(string))
        list.add(Test(string))
        return list
    }

    override fun initViews(view: View) {
        super.initViews(view)
        PrintView.init(printView)
        PrintView.reset()
    }


    @SuppressLint("SetTextI18n")
    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        // vm.user.observe(this, { Log.i("yhy", "user = " + it.toJSON()) })
        vm.userName.observe(this, { Log.i("yhy", "userName = $it") })
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