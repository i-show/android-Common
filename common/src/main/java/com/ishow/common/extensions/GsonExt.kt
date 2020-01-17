package com.ishow.common.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T : Any> Gson.fromJson(json: String): T {
    return this.fromJson(json, T::class.java)
}

inline fun <reified T> Gson.parseJson(json: String?): T {
    return this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}