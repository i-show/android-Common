package com.ishow.noah.modules.init.splash

import android.os.Bundle
import androidx.lifecycle.Observer
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.fullWindow
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivitySpalshBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindActivity
import com.ishow.noah.modules.main.index.MainActivity

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash
 */
class SplashActivity : AppBindActivity<ActivitySpalshBinding, SplashViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullWindow()
        bindContentView(R.layout.activity_spalsh)
    }

    override fun initViewModel(vm: SplashViewModel) {
        super.initViewModel(vm)
        // 注册状态
        vm.permissionStatus.observe(activity, Observer { onPermission()})
        // 初始化
        vm.preInit(this@SplashActivity)
    }


    private fun onPermission() {
        dataBinding.vm?.start()
    }

    override fun showSuccess(success: Success) {
        super.showSuccess(success)
        AppRouter.with(context)
            .target(MainActivity::class.java)
            .finishSelf()
            .start()
    }

    /**
     * 是否显示版本更新的Dialog
     */
    override fun needShowUpdateVersionDialog(): Boolean {
        return false
    }
}
