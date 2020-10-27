package com.ishow.common.widget.load

import android.app.Activity
import android.view.View
import com.ishow.common.widget.load.target.ActivityTarget
import com.ishow.common.widget.load.target.ViewTarget

/**
 * Created by yuhaiyang on 2020/9/15.
 */
class LoadSir private constructor() {
    private lateinit var defaultLoader: Loader


    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LoadSir() }

        /**
         * 初始化一个Loader
         * 剩余其他地方都为copy数据
         */
        @JvmStatic
        fun init(loader: Loader) {
            instance.defaultLoader = loader
        }

        @JvmStatic
        fun width(activity: Activity): Loader {
            val viewTarget = ActivityTarget(activity)
            val loader = instance.defaultLoader.copy()
            loader.setTarget(viewTarget)
            return loader
        }

        @JvmStatic
        fun width(view: View): Loader {
            val viewTarget = ViewTarget(view)
            val loader = instance.defaultLoader.copy()
            loader.setTarget(viewTarget)
            return loader
        }
    }
}