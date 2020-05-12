package com.ishow.noah

import android.app.Application
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.utils.router.AppRouterConfigure

/**
 * Application
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        sInstance = this
        AppRouter.setConfigure(AppRouterConfigure())
    }

    companion object {
        private lateinit var sInstance: App
        val instance
            get() = sInstance
    }
}
