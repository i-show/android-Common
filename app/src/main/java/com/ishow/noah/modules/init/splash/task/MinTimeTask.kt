package com.ishow.noah.modules.init.splash.task

import com.ishow.noah.constant.Configure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class MinTimeTask : ITask {

    override fun startAsync() = GlobalScope.async {
        delay(Configure.SPLASH_TIME)
    }
}