package com.ishow.noah

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.image.loader.ImageLoader
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.utils.router.AppRouterConfigure

/**
 * Application
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Http.init(this)
        ImageLoader.init(this)
        Fresco.initialize(this)
        AppRouter.setConfigure(AppRouterConfigure())
    }
}
