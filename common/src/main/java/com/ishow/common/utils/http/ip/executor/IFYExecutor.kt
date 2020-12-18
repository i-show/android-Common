package com.ishow.common.utils.http.ip.executor

import com.ishow.common.utils.http.ip.AbsIPExecutor
import com.ishow.common.utils.http.ip.IpUtils
import com.ishow.common.utils.http.ip.entries.IpSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class IFYExecutor(private val okHttpClient: OkHttpClient) : AbsIPExecutor() {

    override suspend fun execute(): IpUtils.IpInfo? {
        val request = Request.Builder()
            .url(URL)
            .get()
            .build()

        val body = requestHttp(okHttpClient, request)
        return if (body != null) {
            val responseStr = body.string()

            val info = IpUtils.IpInfo(IpSource.IFY, 0)
            info.ip = getIp(responseStr, "ip")
            info
        } else {
            null
        }
    }

    companion object {
        private const val URL = "https://api.ipify.org/?format=json"
    }

}