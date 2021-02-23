package com.ishow.common.utils.saver.executor

import com.ishow.common.utils.saver.Saver
import com.ishow.common.utils.saver.worker.MMKVWorker
import com.ishow.common.utils.saver.worker.Worker

open class BaseExecutor {
    /**
     * 区块
     */
    protected var group: String = Saver.GROUP_MAIN

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

    protected fun <T> notNull(_list: MutableList<T>?, block: ((list: MutableList<T>) -> Unit)? = null): MutableList<T> {
        val list = _list ?: mutableListOf()
        block?.let { it(list) }
        return list
    }

    protected fun getWorker(expire: Boolean = false): Worker {
        val key = if (expire) group + Saver.EXPIRE_SUFFIX else group
        return Saver.workerList[key] ?: Worker.newWorker(key)
    }

    protected fun checkExpire(key: String?): Boolean {
        if (key == null) return true

        val expireWorker = getWorker(true)
        val expireTime = expireWorker.getLong(key)
        return expireTime > 0 && expireTime < System.currentTimeMillis()
    }
}