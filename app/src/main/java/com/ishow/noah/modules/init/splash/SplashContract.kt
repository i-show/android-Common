package com.ishow.noah.modules.init.splash

import android.content.Context

import com.ishow.common.mvp.base.IPresenter
import com.ishow.common.mvp.base.IView
import com.ishow.common.mvp.base.IViewStatus
import com.ishow.noah.modules.base.AppBaseActivity

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash操作
 */

internal interface SplashContract {

    interface View : IView, IViewStatus {

        /**
         * 权限已经获取成功
         */
        fun permissionGranted()

        /**
         * 下一步操作
         */
        operator fun next()
    }

    interface Presenter : IPresenter {

        /**
         * 获取初始化时间点
         */
        var initStartTime: Long

        /**
         * 初始化完成
         */
        var isInitFinished: Boolean

        /**
         * 目标路径
         */
        val target: SplashPresenter.Target

        /**
         * 预先加载
         */
        fun preInit(context: Context)

        /**
         * init
         */
        fun checkPermission(activity: AppBaseActivity)

        /**
         * 权限初始化后进行初始化
         */
        fun init(activity: SplashActivity)
    }
}