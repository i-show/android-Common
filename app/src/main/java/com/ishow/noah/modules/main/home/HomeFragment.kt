package com.ishow.noah.modules.main.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.extensions.dialog
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.utils.textwatcher.PhoneNumberTextWatcher
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDateTime


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    private var job: Job? = null

    override fun getLayout(): Int = R.layout.f_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)
        PrintView.reset()

        test3.setOnClickListener {
            PrintView.reset()
            requestBaidu()
        }
    }


    private fun requestBaidu() = GlobalScope.launch {

        PrintView.print("yhy", "requestBaidu")
        val okHttp = OkHttpClient.Builder()
            .build()

        val request = Request.Builder()
            .url("https://www.baidu.com/")
            .build()

        val response = okHttp.newCall(request).execute()
        PrintView.print("yhy", "request code = " + response.code())
    }

    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, Observer { PrintView.print(it.toString()) })
    }


    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .action("com.yuhaiyang.androidcommon.Test")
            .start()
    }

    fun timing(times: Int, block: (time: Int) -> Unit) = GlobalScope.launch {
        var currentTime = times
        repeat(times) {
            block(currentTime)
            currentTime -= 1
            delay(1000)
        }
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