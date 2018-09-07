package com.ishow.noah.modules.init.splash;

import android.content.Context;

import com.ishow.common.mvp.base.IPresenter;
import com.ishow.common.mvp.base.IView;
import com.ishow.common.mvp.base.IViewStatus;
import com.ishow.noah.modules.base.AppBaseActivity;

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash操作
 */

interface SplashContract {

    interface View extends IView, IViewStatus {

        /**
         * 权限已经获取成功
         */
        void permissionGranted();

        /**
         * 下一步操作
         */
        void next();
    }

    interface Presenter extends IPresenter {

        /**
         * 预先加载
         */
        void preInit(Context context);
        /**
         * start
         */
        void checkPermission(AppBaseActivity activity);

        /**
         * 权限初始化后进行初始化
         */
        void init(SplashActivity activity);

        /**
         * 获取初始化时间点
         */
        long getInitStartTime();

        /**
         * 初始化完成
         */
        boolean isInitFinished();

        /**
         * 目标路径
         */
        @SplashPresenter.Target
        int getTarget();
    }
}