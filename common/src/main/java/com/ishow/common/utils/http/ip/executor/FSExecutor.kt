package com.ishow.common.utils.http.ip.executor

import com.ishow.common.utils.http.ip.IIPExecutor
import com.ishow.common.utils.http.ip.IpUtils
import com.ishow.common.utils.http.ip.entries.IpSource
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class FSExecutor(private val okHttpClient: OkHttpClient) : IIPExecutor {

    override fun execute(): IpUtils.IpInfo? {
        val request = Request.Builder()
            .url(URL)
            .get()
            .build()

        val response = okHttpClient.newCall(request).execute()
        val body = response.body()
        return if (response.isSuccessful && body != null) {
            val responseStr = body.string()

            val ipStr = JSONObject(responseStr)
                .getJSONObject("ip")
                .getString("addr")

            val info = IpUtils.IpInfo(IpSource.FeiShu, 0)
            info.ip = ipStr
            info
        } else {
            null
        }
    }

    companion object {
        private const val URL = "https://internal-api-lark-api.feishu.cn/dns"
    }

}