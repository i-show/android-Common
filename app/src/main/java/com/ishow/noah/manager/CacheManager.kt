package com.ishow.noah.manager

import android.content.Context

import java.lang.ref.WeakReference

/**
 * Created by yuhaiyang on 2018/8/8.
 * 缓存管理
 */
class CacheManager private constructor() {

    @Suppress("UNUSED_PARAMETER")
    fun clearCache(context: Context) {
        // TODO
    }

    companion object {

        /**
         * 这个东西使用后可以被回收
         */
        @Volatile
        private var sInstance: CacheManager? = null

        val instance: CacheManager
            get() = sInstance ?: synchronized(CacheManager::class.java) {
                sInstance ?: CacheManager().also { sInstance = it }
            }
    }
}
