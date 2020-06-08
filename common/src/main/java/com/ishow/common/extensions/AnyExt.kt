package com.ishow.common.extensions

import android.os.Looper
import com.ishow.common.utils.JsonUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun Any.toJSON(): String? = JsonUtils.gson.toJson(this)

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

/**
 * Delay
 */
fun delay(long: Long, block: () -> Unit) = GlobalScope.launch(Dispatchers.Main) {
    delay(long)
    block()
}

/**
 * Delay
 */
fun delayInThread(long: Long, block: () -> Unit) = GlobalScope.launch {
    delay(long)
    block()
}

fun timing(times: Int, delayTimes: Long = 1000, block: (time: Int) -> Unit) = GlobalScope.launch(Dispatchers.Main) {
    var currentTime = times
    repeat(times) {
        delay(delayTimes)
        block(currentTime)
        currentTime -= 1
    }
}