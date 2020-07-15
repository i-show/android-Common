package com.ishow.noah.modules.main.home

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.ishow.common.extensions.*
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.ui.widget.FixedScaleDrawable
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    private var job: Job? = null

    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)
        PrintView.reset()

        val drawable = requireContext().findDrawable(R.drawable.test)!!
        image1.setImageResource(R.drawable.test_banner)
        image1.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_banner)
            image1.setImageBitmap(bitmap.brightness(0F))
        }

        image2.setImageResource(R.drawable.test_banner)
        image2.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_banner)
            image2.setImageBitmap(bitmap.brightness(50F))
        }

        image3.setImageResource(R.drawable.test_banner)
        image3.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test_banner)
            image3.setImageBitmap(bitmap.brightness(-100F))
        }

        root.fitKeyBoard(neeShow)
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