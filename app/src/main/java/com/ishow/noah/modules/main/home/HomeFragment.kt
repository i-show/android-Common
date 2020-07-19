package com.ishow.noah.modules.main.home

import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.ishow.common.extensions.*
import com.ishow.common.modules.image.select.ImageModel
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.ui.widget.FixedScaleDrawable
import com.ishow.noah.utils.QRCodeUtils
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate


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
            Log.i("yhy", "1 = " + test(0L))
            Log.i("yhy", "1 = " + test(1L))
            Log.i("yhy", "1 = " + test(7L))
            Log.i("yhy", "1 = " + test(8L))
            Log.i("yhy", "1 = " + test(3L))


        }

        root.fitKeyBoard(neeShow)
    }

    fun test(test: Long): String {
        return when (test) {
            0L -> "今天"
            in 1L..7L -> "一周内"
            else -> "eselse"
        }
    }

    private fun getLastImage() {
        val context = requireContext()

        /**
         * 搜索的列
         */
        val IMAGE_PROJECTION = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION, null, null,
            MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        )

        cursor?.moveToFirst()
        cursor?.use {
            val id = it.getLong(0)
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            val name = it.getString(1)
            val bitmap = BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
            val result = QRCodeUtils.decode(bitmap)
            Log.i("yhy", "name = $name ,result = $result")
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