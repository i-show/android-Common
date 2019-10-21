package com.ishow.noah

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.utils.router.AppRouterConfigure

/**
 * Application
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Fresco.initialize(this)
        AppRouter.setConfigure(AppRouterConfigure())
    }
}
