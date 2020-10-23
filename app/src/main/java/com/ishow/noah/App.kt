package com.ishow.noah

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.utils.AppLifeCycleChecker
import com.ishow.noah.utils.router.AppRouterConfigure

/**
 * Application
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        AppRouter.setConfigure(AppRouterConfigure())
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifeCycleChecker())
    }

    companion object {
        private lateinit var sInstance: App
        val app
            get() = sInstance
    }
}
