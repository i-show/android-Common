package com.ishow.common.utils.saver.worker

import com.ishow.common.app.provider.InitProvider
import com.tencent.mmkv.MMKV

class MMKVWorker(private val mmkv: MMKV) : Worker() {

    companion object {
        fun init(key: String?): Worker {
            val mmkv = if (key == null) {
                MMKV.defaultMMKV()!!
            } else {
                MMKV.mmkvWithID(key)!!
            }
            return MMKVWorker(mmkv)
        }
    }

    override fun putString(key: String, value: String?): Worker {
        mmkv.putString(key, value)
        return this
    }

    override fun putStringSet(key: String, values: MutableSet<String>?): Worker {
        mmkv.putStringSet(key, values)
        return this
    }

    override fun putInt(key: String, value: Int): Worker {
        mmkv.putInt(key, value)
        return this
    }

    override fun putLong(key: String, value: Long): Worker {
        mmkv.putLong(key, value)
        return this
    }

    override fun putFloat(key: String, value: Float): Worker {
        mmkv.putFloat(key, value)
        return this
    }

    override fun putBoolean(key: String, value: Boolean): Worker {
        mmkv.putBoolean(key, value)
        return this
    }

    override fun remove(key: String): Worker {
        mmkv.remove(key)
        return this
    }

    override fun clearAll() : Worker {
        mmkv.clearAll()
        return this
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        return mmkv.getBoolean(key, defValue)
    }

    override fun getInt(key: String, defValue: Int): Int {
        return mmkv.getInt(key, defValue)
    }

    override fun getLong(key: String, defValue: Long): Long {
        return mmkv.getLong(key, defValue)
    }

    override fun getFloat(key: String, defValue: Float): Float {
        return mmkv.getFloat(key, defValue)
    }

    override fun getString(key: String, defValue: String): String? {
        return mmkv.getString(key, defValue)
    }

    override fun getStringSet(key: String, defValue: MutableSet<String>): MutableSet<String>? {
        return mmkv.getStringSet(key, defValue)
    }
}