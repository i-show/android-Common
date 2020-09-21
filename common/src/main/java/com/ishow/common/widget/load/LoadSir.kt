package com.ishow.common.widget.load

import android.app.Activity
import android.view.View
import com.ishow.common.widget.load.target.ViewTarget

/**
 * Created by yuhaiyang on 2020/9/15.
 */
class LoadSir private constructor() {
    lateinit var defaultLoader: Loader


    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LoadSir() }

        @JvmStatic
        fun init(): Loader {
            return instance.defaultLoader
        }

        @JvmStatic
        fun width(activity: Activity): Loader {
            return instance.defaultLoader
        }

        @JvmStatic
        fun width(view: View): Loader {
            return instance.defaultLoader
        }

        @JvmStatic
        fun new(view: View): Loader.NewBuilder {
            return Loader.newBuilder(ViewTarget(view))
        }
    }
}