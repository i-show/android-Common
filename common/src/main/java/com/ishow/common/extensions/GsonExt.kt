package com.ishow.common.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


inline fun <reified T : Any> Gson.fromJSON(json: String?): T {
    return this.fromJson(json, T::class.javaObjectType)
}

inline fun <reified T> Gson.parseJSON(json: String?): T {
    return this.fromJson<T>(json, object : TypeToken<T>() {}.type)
}