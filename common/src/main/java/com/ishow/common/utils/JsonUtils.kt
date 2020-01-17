package com.ishow.common.utils

import com.google.gson.Gson

object JsonUtils {
    /**
     * 实例化一个对象
     */
    val gson = Gson()

    fun <T> prase(json: String?, cls: Class<T>?): T? {
        return gson.fromJson<T>(json, cls)
    }

}