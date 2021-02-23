package com.ishow.common.utils.saver.executor

import androidx.annotation.IntRange
import com.ishow.common.utils.saver.Saver
import com.ishow.common.utils.saver.worker.Worker
import java.util.concurrent.TimeUnit

class SaveExecutor : BaseExecutor() {
    private var valueList: MutableList<Any>? = null

    override fun group(group: String): SaveExecutor {
        super.group(group)
        return this
    }

    /**
     * 参数
     */
    fun addParam(key: String, value: Int): SaveExecutor {
        keyList = notNull(keyList) { it.add(key) }
        valueList = notNull(valueList) { it.add(value) }
        return this
    }

    /**
     * 参数
     */
    fun addParam(key: String, value: Long): SaveExecutor {
        keyList = notNull(keyList) { it.add(key) }
        valueList = notNull(valueList) { it.add(value) }
        return this
    }

    /**
     * 参数
     */
    fun addParam(key: String, value: Float): SaveExecutor {
        keyList = notNull(keyList) { it.add(key) }
        valueList = notNull(valueList) { it.add(value) }
        return this
    }

    /**
     * 参数
     */
    fun addParam(key: String, value: Boolean): SaveExecutor {
        keyList = notNull(keyList) { it.add(key) }
        valueList = notNull(valueList) { it.add(value) }
        return this
    }

    /**
     * 参数
     */
    fun addParam(key: String, value: String?): SaveExecutor {
        if (value == null) {
            Saver.remove(key)
            return this
        }
        keyList = notNull(keyList) { it.add(key) }
        valueList = notNull(valueList) { it.add(value) }
        return this
    }

    /**
     * 过期时间
     *
     * @param time 多久过期
     * @param unit 时间单位
     */
    fun expire(@IntRange(from = 0) time: Int, unit: TimeUnit): SaveExecutor {
        if (time > 0) {
            expireTime = TimeUnit.MILLISECONDS.convert(time.toLong(), unit)
        }
        return this
    }

    fun apply() {
        val worker = getWorker()
        val expireWorker = getWorker(true)
        keyList?.forEachIndexed { index, key ->
            save(worker, expireWorker, key, valueList!![index])
        }

        worker.apply()
        expireWorker.apply()
    }

    @Suppress("UNCHECKED_CAST")
    private fun save(worker: Worker, expireWorker: Worker, key: String, value: Any) {
        when (value) {
            is Boolean -> worker.putBoolean(key, value)
            is String -> worker.putString(key, value)
            is Int -> worker.putInt(key, value)
            is Long -> worker.putLong(key, value)
            is Float -> worker.putFloat(key, value)
            else -> worker.putString(key, value as String)
        }

        if (expireTime > 0) {
            expireWorker.putLong(key, System.currentTimeMillis() + expireTime)
        }
    }
}