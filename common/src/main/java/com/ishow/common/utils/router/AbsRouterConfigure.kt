package com.ishow.common.utils.router

/**
 * Created by yuhaiyang on 2017/12/12.
 * 配置文件
 */

abstract class AbsRouterConfigure {

    /**
     * 自定义处理区域
     */
    abstract fun config(router: AppRouter)
}
