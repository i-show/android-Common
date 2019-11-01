package com.ishow.common.extensions

import android.os.Looper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun Any.toJson(): String? = Gson().toJson(this)

/**
 * 判断是否在主线程
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

/**
 * 通过协程  在主线程上运行
 */
fun mainThread(block: () -> Unit) = GlobalScope.launch(Dispatchers.Main) {
    block()
}
