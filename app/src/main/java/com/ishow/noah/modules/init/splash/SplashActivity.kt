package com.ishow.noah.modules.init.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.utils.permission.PermissionDenied
import com.ishow.common.utils.permission.PermissionGranted
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivitySpalshBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindActivity

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash
 */
class SplashActivity : AppBindActivity<ActivitySpalshBinding, SplashViewModel>() {
    private lateinit var mSplashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_spalsh)
    }

    override fun initViewModel(vm: SplashViewModel) {
        super.initViewModel(vm)
        mSplashViewModel = vm
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
        mSplashViewModel.init(this@SplashActivity)
    }

    /**
     * 权限已经被拒绝
     */
    @PermissionDenied(SplashViewModel.REQUEST_PERMISSION_CODE)
    fun permissionDenied() {
        finish()
    }

    // ======= 系统配置 ===========//
    /**
     * 设置StatusBar的样式
     */
    override fun resetStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    /**
     * 是否显示版本更新的Dialog
     */
    override fun needShowUpdateVersionDialog(): Boolean {
        return false
    }
}
