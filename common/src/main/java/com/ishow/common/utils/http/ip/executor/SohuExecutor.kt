package com.ishow.common.utils.http.ip.executor

import com.ishow.common.utils.http.ip.AbsIPExecutor
import com.ishow.common.utils.http.ip.IpUtils
import com.ishow.common.utils.http.ip.entries.IpSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SohuExecutor(private val okHttpClient: OkHttpClient) : AbsIPExecutor() {

    override suspend fun execute(): IpUtils.IpInfo? {
        val request = Request.Builder()
            .url(URL)
            .get()
            .build()

        val body = requestHttp(okHttpClient, request)
        return if (body != null) {
            val responseStr = body.string()
                .split("=")[1]
                .trim()
                .apply { substring(0, length - 2) }

            val info = IpUtils.IpInfo(IpSource.Sohu, 0)
            info.ip = getIp(responseStr, "cip")
            info
        } else {
            null
        }
    }

    companion object {
        private const val URL = "http://pv.sohu.com/cityjson"
    }

}