package com.ishow.common.extensions

import android.os.Looper
import com.ishow.common.app.provider.InitProvider
import com.ishow.common.utils.JsonUtils
import kotlinx.coroutines.*


inline val Any.appScope: CoroutineScope
    get() = InitProvider.scope

/**
 * 任意对象转成Json字符串
 */
fun Any.toJSON(): String? = JsonUtils.gson.toJson(this)

/**
 * 判断是否在主线程
 */
fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

/**
 * 通过协程  在主线程上运行
 */
fun mainThread(scope: CoroutineScope = InitProvider.scope, block: () -> Unit) {
    if (isMainThread()) {
        block()
    } else {
        scope.launch(Dispatchers.Main) { block() }
    }
}

/**
 * Delay多少毫秒后执行
 */
fun delay(long: Long, scope: CoroutineScope = InitProvider.scope, block: () -> Unit) = scope.launch(Dispatchers.Main) {
    delay(long)
    block()
}

/**
 * Delay
 */
fun delayInThread(long: Long, scope: CoroutineScope = InitProvider.scope, block: () -> Unit) = scope.launch {
    delay(long)
    block()
}

/**
 * 计时工具
 */
fun timing(times: Int, delayTimes: Long = 1000, scope: CoroutineScope = InitProvider.scope, block: (time: Int) -> Unit) = scope.launch(Dispatchers.Main) {
    var currentTime = times
    repeat(times) {
        delay(delayTimes)
        block(currentTime)
        currentTime -= 1
    }
}