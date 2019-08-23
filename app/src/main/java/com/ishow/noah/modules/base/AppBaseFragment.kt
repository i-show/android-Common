package com.ishow.noah.modules.base

import com.baidu.mobstat.StatService
import com.ishow.common.app.fragment.BaseFragment

/**
 * Created by yuhaiyang on 2018/8/8.
 * 基础Fragment
 * 备用：
 */
abstract class AppBaseFragment : BaseFragment() {

    override fun onPause() {
        super.onPause()
        // 页面埋点
        StatService.onPageEnd(activity, null)
    }

    override fun onResume() {
        super.onResume()
        // 页面埋点
        StatService.onPageStart(activity, null)
    }


}
