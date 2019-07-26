package com.ishow.noah.modules.init.splash

import android.Manifest
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.mobstat.StatService
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.manager.ConfigureManager
import com.ishow.noah.manager.VersionManager
import com.ishow.noah.modules.account.common.AccountModel
import com.ishow.noah.modules.account.login.LoginActivity
import com.ishow.noah.modules.base.AppBaseViewModel
import com.ishow.noah.modules.init.splash.task.MinTimeTask
import com.ishow.noah.modules.init.splash.task.TaskManager
import com.ishow.noah.modules.init.splash.task.UserTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashViewModel(app: Application) : AppBaseViewModel(app) {

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
        val taskManager = TaskManager.instance
                .addTask(MinTimeTask())
                .addTask(UserTask(activity))

        GlobalScope.launch {
            taskManager.startAsync().await()
            gotoTarget(activity)
        }

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


    private fun gotoTarget(context: Context) {
        AppRouter.with(context)
                .target(LoginActivity::class.java)
                .start()
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