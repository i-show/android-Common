package com.ishow.noah.modules.init.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.entries.status.Success
import com.ishow.common.extensions.fullWindow
import com.ishow.common.utils.permission.PermissionDenied
import com.ishow.common.utils.permission.PermissionGranted
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivitySpalshBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindActivity
import com.ishow.noah.modules.main.MainActivity

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
        vm.permissionStatus.observe(activity, Observer { permissionGranted() })
        // 初始化
        vm.preInit(this@SplashActivity)
    }

    /**
     * 权限已经获取到
     */
    @PermissionGranted(SplashViewModel.REQUEST_PERMISSION_CODE)
    fun permissionGranted() {
        dataBinding.vm?.start()
    }

    /**
     * 权限已经被拒绝
     */
    @PermissionDenied(SplashViewModel.REQUEST_PERMISSION_CODE)
    fun permissionDenied() {
        finish()
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
