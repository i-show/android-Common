package com.ishow.common.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.IntRange
import java.util.concurrent.TimeUnit

/**
 * Created by yuhaiyang on 2017/12/12.
 * 存储服务
 */

object StorageUtils {
    /**
     * 保存在手机里面的文件名
     */
    private const val GROUP_MAIN = "share_main_date"
    /**
     * 时间超时保存的db
     */
    private const val EXPIRE_SUFFIX = "_enter_port_expire_ee"

    fun with(context: Context): Executor {
        return Executor(context)
    }

    class Executor constructor(private val context: Context) {
        /**
         * 区块
         */
        private var group: String? = null
        /**
         * key
         */
        private var key: String? = null
        /**
         * Value
         */
        private var value: Any? = null

        /**
         * 过期时间
         */
        private var expireTime: Long = 0

        init {
            this.group = GROUP_MAIN
        }

        /**
         * 分组信息
         */
        fun group(group: String): Executor {
            this.group = group
            return this
        }

        /**
         * 参数
         */
        fun key(key: String): Executor {
            this.key = key
            return this
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        fun param(key: String, value: Int): Executor {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        fun param(key: String, value: Long): Executor {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        fun param(key: String, value: Float): Executor {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        fun param(key: String, value: Boolean): Executor {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        fun param(key: String, value: String): Executor {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 参数
         *
         * @param value 如果是保存 那么是保存数据, 获取则是默认数据
         */
        fun param(key: String, value: Set<String>): Executor {
            this.key = key
            this.value = value
            return this
        }

        /**
         * 过期时间
         *
         * @param time 多久过期
         * @param unit 时间单位
         */
        fun expire(@IntRange(from = 1) time: Int, unit: TimeUnit): Executor {
            expireTime = TimeUnit.MILLISECONDS.convert(time.toLong(), unit)
            return this
        }

        @Suppress("UNCHECKED_CAST")
        fun save() {
            val sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            if (value == null || value is String) {
                editor.putString(key, value as String?)
                editor.apply()
            } else if (value is Boolean) {
                editor.putBoolean(key, (value as Boolean))
                editor.apply()
            } else if (value is Int) {
                editor.putInt(key, (value as Int))
                editor.apply()
            } else if (value is Float) {
                editor.putFloat(key, (value as Float))
                editor.apply()
            } else if (value is Long) {
                editor.putLong(key, (value as Long))
                editor.apply()
            } else if (value is Set<*>) {
                editor.putStringSet(key, value as Set<String>?)
                editor.apply()
            }

            if (expireTime > 0) {
                val expireSp = context.getSharedPreferences(group!! + EXPIRE_SUFFIX, Context.MODE_PRIVATE)
                val expireEditor = expireSp.edit()
                expireEditor.putLong(key, System.currentTimeMillis() + expireTime)
                expireEditor.apply()
            }
        }

        fun get(): String {
            return get(StringUtils.EMPTY)
        }

        fun get(defaultValue: String): String {
            if (checkExpire()) {
                return defaultValue
            }
            return getSharedPreferences().getString(key, defaultValue) ?: StringUtils.EMPTY
        }

        fun get(defaultValue: Boolean): Boolean {
            if (checkExpire()) {
                return defaultValue
            }
            return getSharedPreferences().getBoolean(key, defaultValue)
        }

        fun getInt(defaultValue: Int): Int {
            if (checkExpire()) {
                return defaultValue
            }
            return getSharedPreferences().getInt(key, defaultValue)
        }

        fun get(defaultValue: Long): Long {
            if (checkExpire()) {
                return defaultValue
            }
            return getSharedPreferences().getLong(key, defaultValue)
        }

        fun get(defaultValue: Float): Float {
            if (checkExpire()) {
                return defaultValue
            }
            return getSharedPreferences().getFloat(key, defaultValue)
        }

        fun get(defaultValue: Set<String>): Set<String>? {
            if (checkExpire()) {
                return defaultValue
            }
            return getSharedPreferences().getStringSet(key, defaultValue)
        }

        private fun checkExpire(): Boolean {
            val expireSp = context.getSharedPreferences(group!! + EXPIRE_SUFFIX, Context.MODE_PRIVATE)
            val expireTime = expireSp.getLong(key, 0)

            return if (expireTime > 0 && expireTime < System.currentTimeMillis()) {
                remove()
                true
            } else {
                false
            }
        }

        private fun getSharedPreferences(): SharedPreferences {
            return context.getSharedPreferences(group, Context.MODE_PRIVATE)
        }

        /**
         * 移除
         */
        fun remove() {
            val sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove(key).apply()

            val expireSp = context.getSharedPreferences(group!! + EXPIRE_SUFFIX, Context.MODE_PRIVATE)
            val expireEditor = expireSp.edit()
            expireEditor.remove(key).apply()
        }

        /**
         * 清空整个存储内容
         */
        fun clear() {
            val sharedPreferences = context.getSharedPreferences(group, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear().apply()

            val expireSp = context.getSharedPreferences(group!! + EXPIRE_SUFFIX, Context.MODE_PRIVATE)
            val expireEditor = expireSp.edit()
            expireEditor.clear().apply()
        }
    }

}
