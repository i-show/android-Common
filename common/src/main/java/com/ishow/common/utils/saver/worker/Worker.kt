package com.ishow.common.utils.saver.worker

import com.ishow.common.app.provider.InitProvider
import com.ishow.common.utils.saver.Saver
import com.tencent.mmkv.MMKV

abstract class Worker {
    abstract fun putString(key: String, value: String?): Worker

    abstract fun putStringSet(key: String, values: MutableSet<String>?): Worker

    abstract fun putInt(key: String, value: Int): Worker

    abstract fun putLong(key: String, value: Long): Worker

    abstract fun putFloat(key: String, value: Float): Worker

    abstract fun putBoolean(key: String, value: Boolean): Worker

    abstract fun remove(key: String): Worker

    fun apply() {
    }

    abstract fun clearAll(): Worker

    abstract fun getBoolean(key: String, defValue: Boolean = false): Boolean

    abstract fun getInt(key: String, defValue: Int = 0): Int

    abstract fun getLong(key: String, defValue: Long = 0L): Long

    abstract fun getFloat(key: String, defValue: Float = 0.0F): Float

    abstract fun getString(key: String, defValue: String = ""): String?

    abstract fun getStringSet(key: String, defValue: MutableSet<String> = mutableSetOf()): MutableSet<String>?

    companion object {
        fun init() {
            MMKV.initialize(InitProvider.app)
        }

        fun newWorker(key: String?): Worker {
            val newWorker = MMKVWorker.init(key)
            Saver.workerList.put(key, newWorker)
            return newWorker
        }
    }
}