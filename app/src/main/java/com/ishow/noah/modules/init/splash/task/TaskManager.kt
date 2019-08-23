package com.ishow.noah.modules.init.splash.task

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class TaskManager private constructor() {

    private val taskList = ArrayList<ITask>()

    internal fun addTask(task: ITask): TaskManager {
        taskList.add(task)
        return this
    }

    internal fun startAsync() = GlobalScope.async {
        if (taskList.isNullOrEmpty()) {
            return@async true
        }

        taskList.map { it.startAsync() }.forEach { it.await() }
        return@async true
    }

    internal fun clear(): TaskManager {
        taskList.clear()
        return this
    }

    companion object {
        @Volatile
        private var manager: TaskManager? = null
        val instance: TaskManager
            get() {
                if (manager == null) {
                    synchronized(TaskManager::class.java) {
                        if (manager == null) {
                            manager = TaskManager()
                        }
                    }
                }
                return manager!!
            }
    }
}