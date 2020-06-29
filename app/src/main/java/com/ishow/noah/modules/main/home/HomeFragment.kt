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
import kotlinx.coroutines.*
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
            var time = formatTime(60)
            PrintView.print(time)

            time = formatTime(360)
            PrintView.print(time)

            time = formatTime(3960)
            PrintView.print(time)
        }
    }


    private fun formatTime(time: Long): String {
        if (time < 60) {
            return "00:${formatNumber(time)}"
        }
        val hour = time / 3600
        val min = time % 3600 / 60
        val sec = time % 60

        if (hour > 0) {
            return "${hour}:${formatNumber(min)}:${formatNumber(sec)}"
        } else {
            return "${formatNumber(min)}:${formatNumber(sec)}"
        }

    }

    private fun formatNumber(number: Long): String {
        return if (number < 10) {
            "0$number"
        } else {
            number.toString()
        }
    }


    private suspend fun test(): Int = withContext(Dispatchers.IO) {
        return@withContext 20
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