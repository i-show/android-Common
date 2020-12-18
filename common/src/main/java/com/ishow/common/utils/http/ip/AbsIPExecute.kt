package com.ishow.common.utils.http.ip

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject

abstract class AbsIPExecutor {
    abstract suspend fun execute(): IpUtils.IpInfo?

    suspend fun requestHttp(client: OkHttpClient, request: Request): ResponseBody? = withContext(Dispatchers.IO) {
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful && response.body() != null) {
                return@withContext response.body()
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            return@withContext null
        }
    }

    fun getIp(ipJson: String, ipKey: String): String {
        return try {
            JSONObject(ipJson)
                .getString(ipKey)
        } catch (e: JSONException) {
            ""
        }
    }


    fun getIp(ipJson: String, ipKey1: String, ipKey2: String): String {
        return try {
            JSONObject(ipJson)
                .getJSONObject(ipKey1)
                .getString(ipKey2)
        } catch (e: JSONException) {
            ""
        }
    }
}

