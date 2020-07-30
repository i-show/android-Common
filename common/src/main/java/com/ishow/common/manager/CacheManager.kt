package com.ishow.common.manager

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
        /**
         * 缓存对应Url 的请求信息
         */
        fun cache(url: String, value: Any?, expire: Int = 0) {
            if (value == null) return

            StorageUtils.save()
                .group("cache")
                .addParam(url, value.toJSON())
                .expire(expire, TimeUnit.SECONDS)
                .apply()
        }

        /**
         * 获取缓存对应Url的值
         */
        inline fun <reified T> get(url: String): T? {
            val cacheStr = getCacheString(url)
            if (cacheStr.isNullOrEmpty()) return null

            return cacheStr.parseJSON()
        }

        /**
         * 移除对应Url的缓存
         */
        fun remove(url: String) {
            StorageUtils.remove()
                .group("cache")
                .key(url)
                .apply()
        }

        /**
         * 获取缓存Str
         */
        fun getCacheString(url: String): String? {
            return StorageUtils.get()
                .group("cache")
                .key(url)
                .apply()
        }
    }
}