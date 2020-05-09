package com.ishow.noah.modules.sample.detail.utils.okhttp.cache

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ishow.common.utils.http.okhttp.interceptor.NetCacheInterceptor
import com.ishow.common.utils.http.okhttp.interceptor.OfflineCacheInterceptor
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FOkHttpCacheBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_ok_http_cache.*
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by yuhaiyang on 2020-05-09.
 */
class OkHttpCacheFragment : AppBindFragment<FOkHttpCacheBinding, OkHttpCacheViewModel>() {
    private lateinit var client: OkHttpClient
    override fun getLayout(): Int = R.layout.f_ok_http_cache


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOkHttp()

        PrintView.init(printView)


        get.setOnClickListener {
            PrintView.reset()
            PrintView.print("=======start request =======")
            val request = Request.Builder()
                .url("http://192.168.2.187:8080/test/testTime")
                .header(NetCacheInterceptor.CACHE_TIME, "10")
                .header(OfflineCacheInterceptor.KEY_ENABLE, "0")
                .build()


            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i(TAG, "onFailure: error = $e")
                    PrintView.print(e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    PrintView.print("onResponse: code:" + response.code())
                    PrintView.print("onResponse: body:" + response.body()?.string())
                }

            })

        }
    }


    private fun initOkHttp() {
        val context = context ?: return
        val path = context.getExternalFilesDir("okcache")!!
        if (!path.exists()) path.mkdirs()
        val cache = Cache(path, 1024 * 1024 * 1024)
        client = OkHttpClient.Builder()
            .addNetworkInterceptor(NetCacheInterceptor())
            .addInterceptor(OfflineCacheInterceptor())
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }


    companion object {
        private const val TAG = "OkHttpCacheFragment"
    }
}