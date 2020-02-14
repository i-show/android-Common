package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.inflate
import com.ishow.common.utils.image.compress.ImageCompress
import com.ishow.common.utils.image.compress.adapter.RenameDateTimeAdapter
import com.ishow.common.utils.image.compress.filter.MinSizeFilter
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class HomeFragment : AppBaseFragment() {

    private var mRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView != null) {
            return mRootView
        }

        mRootView = container?.inflate(R.layout.fragment_home)
        return mRootView
    }

    var count = 1
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrintView.init(printView)

        send.setOnClickListener {
            PrintView.print("第 $count 次启动")
            PrintView.print("sdfgsdfg")
            count++
        }

        reset.setOnClickListener {
            Log.i("yhy", "reset")
            ImageCompress.with(context!!)
                .compress(Uri.parse("content://media/external/images/media/90125"))
                .renameAdapter(RenameDateTimeAdapter())
                .addFilter(MinSizeFilter(500))
                .savePath("/test/te")
                .compressListener {
                    if (it.isSuccess()) {
                        PrintView.print("压缩成功")
                        PrintView.print("压缩后的路径为${it.image?.absolutePath}")
                    } else {
                        PrintView.print("压缩失败")
                        PrintView.print("压缩错误：${it.errorList}")
                    }
                }
                .start()
        }

        show.setOnClickListener {
            test()
        }

    }


    fun test() {
        Log.i("yhy", "test: register")
        val filter = IntentFilter()
        filter.addAction("com.huawei.cloudserive.loginSuccess")
        filter.addAction("com.huawei.cloudserive.loginFailed")
        filter.addAction("com.huawei.cloudserive.loginCancel")

        activity?.registerReceiver(broadcastReceiver, filter)
    }


    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.i("yhy", "onReceive: action = ${intent.action}")
        }

    }

    fun <T> test(t: T) {
        PrintView.print("T = $t")
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .target(SampleMainActivity::class.java)
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
