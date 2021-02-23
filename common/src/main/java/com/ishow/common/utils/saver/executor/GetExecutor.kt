package com.ishow.common.utils.saver.executor

import com.ishow.common.utils.StringUtils
import com.ishow.common.utils.saver.Saver

class GetExecutor : BaseExecutor() {
    /**
     * key
     */
    private var key: String? = null

    override fun group(group: String): GetExecutor {
        super.group(group)
        return this
    }

    /**
     * 参数
     */
    fun key(key: String): GetExecutor {
        this.key = key
        return this
    }

    fun apply(): String {
        return apply(StringUtils.EMPTY)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> apply(defValue: T): T {
        val key = key ?: return defValue

        if (checkExpire(key)) {
            Saver.remove(key)
            return defValue
        }
        val saver = getWorker()

        return when (defValue) {
            is Boolean -> saver.getBoolean(key, defValue) as T
            is Int -> saver.getInt(key, defValue) as T
            is Long -> saver.getLong(key, defValue) as T
            is Float -> saver.getFloat(key, defValue) as T
            is String -> saver.getString(key, defValue) as T
            is Set<*> -> saver.getStringSet(key, mutableSetOf()) as T
            else -> throw IllegalStateException("type error")
        }
    }
}