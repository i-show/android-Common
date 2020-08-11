package com.ishow.common.manager

import android.os.SystemClock
import androidx.collection.LruCache
import com.ishow.common.extensions.parseJSON
import com.ishow.common.extensions.toJSON
import com.ishow.common.utils.StorageUtils
import java.util.concurrent.TimeUnit

/**
 * Created by yuhaiyang on 2020/7/30.
 * 缓存管理
 */

class CacheManager private constructor() {


    companion object {
        private val ramCache: LruCache<String, String> = LruCache(20)
        private val ramCacheExpire: LruCache<String, Long> = LruCache(20)

        /**
         * 缓存对应key 的请求信息
         */
        fun cache(key: String, value: Any?, expire: Int = 0) {
            if (value == null) return

            StorageUtils.save()
                .group("cache")
                .addParam(key, value.toJSON())
                .expire(expire, TimeUnit.SECONDS)
                .apply()
        }

        /**
         * 获取缓存对应Url的值
         */
        inline fun <reified T> get(key: String): T? {
            val cacheStr = getCacheString(key)
            if (cacheStr.isNullOrEmpty()) return null

            return cacheStr.parseJSON()
        }

        /**
         * 移除对应Url的缓存
         */
        fun remove(key: String) {
            StorageUtils.remove()
                .group("cache")
                .key(key)
                .apply()
        }

        /**
         * 获取缓存Str
         */
        fun getCacheString(key: String): String? {
            val cacheStr = ramCache.get(key)
            if (!cacheStr.isNullOrEmpty()) {
                return getCacheStringFromRam(key, cacheStr)
            }

            return StorageUtils.get()
                .group("cache")
                .key(key)
                .apply()
        }

        /**
         * 从内存中获取缓存数据
         */
        @Suppress("LiftReturnOrAssignment", "CascadeIf")
        private fun getCacheStringFromRam(key: String, value: String): String? {
            val expire = ramCacheExpire.get(key)
            if (expire == null) {
                return value
            } else if (expire < SystemClock.elapsedRealtime()) {
                ramCache.remove(key)
                ramCacheExpire.remove(key)
                StorageUtils.remove()
                    .group("cache")
                    .key(key)
                    .apply()
                return null
            } else {
                return value
            }
        }
    }
}