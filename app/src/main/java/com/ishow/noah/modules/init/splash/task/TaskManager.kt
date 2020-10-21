package com.ishow.noah.modules.init.splash.task

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class TaskManager private constructor() {

    private val taskList = ArrayList<ITask>()

    internal fun addTask(task: ITask): TaskManager {
        taskList.add(task)
        return this
    }

    internal fun startAsync(scope: CoroutineScope) = scope.async {
        if (taskList.isNullOrEmpty()) {
            return@async true
        }

        taskList.map { it.startAsync(scope) }.forEach { it.await() }
        return@async true
    }

    internal fun clear(): TaskManager {
        taskList.clear()
        return this
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { TaskManager() }
    }
}