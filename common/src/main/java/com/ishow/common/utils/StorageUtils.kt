package com.ishow.common.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.system.Os.remove
import androidx.annotation.IntRange
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates.notNull

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

    private lateinit var app: Application

    fun init(application: Application) {
        app = application
    }

    @JvmStatic
    fun save() = SaveExecutor(app.applicationContext)

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
    fun get() = GetExecutor(app.applicationContext)

    @JvmStatic
    fun get(key: String): String = get(key, StringUtils.EMPTY)

    @JvmStatic
    fun <T> get(key: String, defaultValue: T): T {
        return get()
            .key(key)
            .apply(defaultValue)
    }

    @JvmStatic
    fun remove() = RemoveExecutor(app.applicationContext)

    @JvmStatic
    fun remove(key: String) {
        remove().key(key).apply()
    }

    @JvmStatic
    fun clear(group: String = GROUP_MAIN) {
        ClearExecutor(app.applicationContext).group(group).clear()
    }

    class ClearExecutor constructor(context: Context) : BaseExecutor(context) {

        override fun group(group: String): ClearExecutor {
            super.group(group)
            return this
        }

        /**
         * 清空整个存储内容
         */
        fun clear() {
            val editor = sharedPreferences().edit()
            editor.clear().apply()

            val expireEditor = sharedPreferences(true).edit()
            expireEditor.clear().apply()
        }
    }

    class RemoveExecutor constructor(context: Context) : BaseExecutor(context) {
        /**
         * key
         */
        private var key: String? = null

        /**
         * 参数
         */
        fun key(key: String): RemoveExecutor {
            this.key = key
            return this
        }

        override fun group(group: String): RemoveExecutor {
            super.group(group)
            return this
        }

        fun apply() {
            val editor = sharedPreferences().edit()
            editor.remove(key).apply()

            val expireEditor = sharedPreferences(true).edit()
            expireEditor.remove(key).apply()
        }
    }

    class GetExecutor constructor(context: Context) : BaseExecutor(context) {
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
        fun <T> apply(value: T): T {
            if (checkExpire(key)) {
                remove(key)
                return value
            }
            val sp = sharedPreferences()

            return when (value) {
                is Boolean -> sp.getBoolean(key, value) as T
                is Int -> sp.getInt(key, value) as T
                is Long -> sp.getLong(key, value) as T
                is Float -> sp.getFloat(key, value) as T
                is String -> sp.getString(key, value) as T
                is Set<*> -> sp.getStringSet(key, setOf()) as T
                else -> throw IllegalStateException("type error")
            }
        }
    }

    class SaveExecutor constructor(context: Context) : BaseExecutor(context) {
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
                remove(key)
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
        fun expire(@IntRange(from = 1) time: Int, unit: TimeUnit): SaveExecutor {
            expireTime = TimeUnit.MILLISECONDS.convert(time.toLong(), unit)
            return this
        }

        fun apply() {
            val editor = sharedPreferences().edit()
            val expireEditor = sharedPreferences(true).edit()
            keyList?.forEachIndexed { index, key ->
                save(editor, expireEditor, key, valueList!![index])
            }
            editor.apply()
            expireEditor.apply()
        }

        @Suppress("UNCHECKED_CAST")
        private fun save(editor: Editor, expireEditor: Editor, key: String, value: Any) {
            when (value) {
                is Boolean -> editor.putBoolean(key, value)
                is String -> editor.putString(key, value)
                is Int -> editor.putInt(key, value)
                is Long -> editor.putLong(key, value)
                is Float -> editor.putFloat(key, value)
                else -> editor.putString(key, value as String)
            }

            if (expireTime > 0) {
                expireEditor.putLong(key, System.currentTimeMillis() + expireTime)
            }
        }
    }


    open class BaseExecutor constructor(protected val context: Context) {
        /**
         * 区块
         */
        protected var group: String = GROUP_MAIN

        /**
         * 过期时间
         */
        protected var expireTime: Long = 0
        /**
         * Key值
         */
        protected var keyList: MutableList<String>? = null

        /**
         * 分组信息
         */
        open fun group(group: String): BaseExecutor {
            this.group = group
            return this
        }


        protected fun <T> notNull(
            _list: MutableList<T>?,
            block: ((list: MutableList<T>) -> Unit)? = null
        ): MutableList<T> {
            val list = _list ?: mutableListOf()
            block?.let { it(list) }
            return list
        }

        protected fun sharedPreferences(expire: Boolean = false): SharedPreferences {
            val key = if (expire) group + EXPIRE_SUFFIX else group
            return context.getSharedPreferences(key, Context.MODE_PRIVATE)
        }


        protected fun checkExpire(key: String?): Boolean {
            val expireSp = sharedPreferences(true)
            val expireTime = expireSp.getLong(key, 0)
            return expireTime > 0 && expireTime < System.currentTimeMillis()
        }
    }
}
