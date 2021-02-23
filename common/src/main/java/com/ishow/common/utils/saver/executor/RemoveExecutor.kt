package com.ishow.common.utils.saver.executor

class RemoveExecutor : BaseExecutor() {
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
        key?.let {
            val saver = getWorker()
            saver.remove(it).apply()

            val expireSaver = getWorker(true)
            expireSaver.remove(it).apply()
        }
    }
}