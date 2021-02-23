package com.ishow.common.utils.saver

import android.util.LruCache
import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.saver.executor.ClearExecutor
import com.ishow.common.utils.saver.executor.GetExecutor
import com.ishow.common.utils.saver.executor.RemoveExecutor
import com.ishow.common.utils.saver.executor.SaveExecutor
import com.ishow.common.utils.saver.worker.Worker

/**
 * Created by yuhaiyang on 2021/02/23.
 * 存储服务
 */
object Saver {
    /**
     * 保存在手机里面的文件名
     */
    internal const val GROUP_MAIN = "main_data"

    /**
     * 时间超时保存的db
     */
    internal const val EXPIRE_SUFFIX = "_expire_ee"

    /**
     * 工作组织
     */
    internal val workerList: LruCache<String, Worker?> by lazy { LruCache(8) }

    init {
        Worker.init()
    }

    /**
     * Save area
     */
    @JvmStatic
    fun save() = SaveExecutor()

    @JvmStatic
    fun save(key: String, value: Any?) {
        if (value == null) {
            remove(key)
        }

        when (value) {
            is Boolean -> save().addParam(key, value).apply()
            is String -> save().addParam(key, value).apply()
            is Int -> save().addParam(key, value).apply()
            is Long -> save().addParam(key, value).apply()
            is Float -> save().addParam(key, value).apply()
        }
    }

    @JvmStatic
    fun get() = GetExecutor()

    @JvmStatic
    fun get(key: String): String = get(key, StringUtils.EMPTY)

    @JvmStatic
    fun <T> get(key: String, defaultValue: T): T {
        return get()
            .key(key)
            .apply(defaultValue)
    }

    @JvmStatic
    fun remove() = RemoveExecutor()

    @JvmStatic
    fun remove(key: String?) {
        key?.let { remove().key(it).apply() }
    }

    @JvmStatic
    fun clear(group: String = GROUP_MAIN) {
        ClearExecutor().group(group).clear()
    }
}
