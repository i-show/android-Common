package com.ishow.noah.modules.init.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.utils.permission.PermissionDenied
import com.ishow.common.utils.permission.PermissionGranted
import com.ishow.noah.databinding.ActivitySpalshBinding
import com.ishow.noah.modules.base.mvvm.AppBindActivity

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash
 */
class SplashActivity : AppBindActivity<ActivitySpalshBinding>() {
    private lateinit var mSplashViewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSplashViewModel = getViewModel(SplashViewModel::class.java)
        initViewModel()
    }

    private fun initViewModel() {
        val activity = this@SplashActivity
        mSplashViewModel.run {
            // 注册状态
            permissionGranted.observe(activity, Observer { permissionGranted() })
            // 初始化
            preInit(activity)
        }
    }

    override fun onResume() {
        super.onResume()
        /*
        if (mPresenter.isInitFinished) {
            // 稍微延迟一下,体验更好
            nextDelay(800)
        }
        */
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
