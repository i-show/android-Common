package com.ishow.noah.modules.init.splash

import android.Manifest
import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.mobstat.StatService
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.ConfigureManager
import com.ishow.noah.manager.VersionManager
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.base.AppBaseViewModel
import com.ishow.noah.modules.init.splash.task.MinTimeTask
import kotlinx.coroutines.delay

class SplashViewModel : AppBaseViewModel() {

    private val _permissionGranted = MutableLiveData<Boolean>()
    val permissionGranted: LiveData<Boolean>
        get() = _permissionGranted

    private var initTime: Long = 0
    private var initFinished: Boolean = false
    private val mAccountModel = AccountModel()

    fun preInit(activity: SplashActivity) {
        StatService.start(activity)
        initTime = System.currentTimeMillis()
        initFinished = false
        checkPermission(activity)
    }

    fun init(activity: SplashActivity) {
        ConfigureManager.init()
        VersionManager.init(activity)
        mAccountModel.loginByToken(activity, loginCallBack)
        Settings.Secure.ANDROID_ID
    }


    /**
     * 检测权限
     */
    private fun checkPermission(activity: SplashActivity) {
        if (PermissionManager.hasPermission(activity, *PERMISSIONS)) {
            _permissionGranted.value = true
        } else {
            PermissionManager.with(activity)
                    .permission(*PERMISSIONS)
                    .annotationClass(activity)
                    .requestCode(REQUEST_PERMISSION_CODE)
                    .send()
        }
    }

    /**
     * 登录的回调
     */
    private val loginCallBack = object : AccountModel.OnLoginCallBack {
        override fun onSuccess(container: UserContainer) {
        }

        override fun onFailed(error: HttpError) {
        }

    }


    companion object {
        /**
         * 请求权限的Code
         */
        const val REQUEST_PERMISSION_CODE = 1001
        /**
         * 请求的权限
         */
        private val PERMISSIONS = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}