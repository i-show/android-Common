package com.ishow.noah.modules.init.splash.task

import com.ishow.noah.constant.Configure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MinTimeTask : ITask {
    override fun run() {
        Thread.sleep(Configure.SPLASH_TIME)
    }

    fun start(listener: (() -> Unit)) {

    }
}