package com.ishow.common.utils.saver.executor

/**
 * ClearExecutor
 */
class ClearExecutor : BaseExecutor() {

    override fun group(group: String): ClearExecutor {
        super.group(group)
        return this
    }

    /**
     * 清空整个存储内容
     */
    fun clear() {
        val worker = getWorker()
        worker.clearAll().apply()

        val expireWorker = getWorker(true)
        expireWorker.clearAll().apply()
    }
}