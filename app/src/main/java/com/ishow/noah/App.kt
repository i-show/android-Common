package com.ishow.noah

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ishow.common.extensions.timing
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.utils.router.AppRouterConfigure

/**
 * Application
 */
class App : Application() {

    private val _test = MutableLiveData<String>()
    val test: LiveData<String>
        get() = _test

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        AppRouter.setConfigure(AppRouterConfigure())

        timing(Int.MAX_VALUE) {
            _test.value = DateUtils.now()
        }
    }

    companion object {
        private lateinit var sInstance: App
        val app
            get() = sInstance
    }
}
