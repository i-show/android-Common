package com.ishow.noah.modules.init.splash

import android.Manifest
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.baidu.mobstat.StatService
import com.ishow.common.utils.permission.PermissionInfo
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.noah.manager.ConfigureManager
import com.ishow.noah.manager.VersionManager
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import com.ishow.noah.modules.init.splash.task.MinTimeTask
import com.ishow.noah.modules.init.splash.task.TaskManager
import com.ishow.noah.modules.init.splash.task.UserTask
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashViewModel(app: Application) : AppBaseViewModel(app) {

    private val _permissionStatus = MutableLiveData<Boolean>()
    val permissionStatus: LiveData<Boolean>
        get() = _permissionStatus

    private var initTime: Long = 0
    private var initFinished: Boolean = false

    fun preInit(activity: SplashActivity) {
        StatService.start(activity)
        initTime = System.currentTimeMillis()
        initFinished = false
        checkPermission(activity)
    }


    fun start() {
        ConfigureManager.init()
        VersionManager.init()

        val taskManager = TaskManager.instance
            .clear()
            .addTask(MinTimeTask())
            .addTask(UserTask())

        GlobalScope.launch {
            taskManager.startAsync().await()
            showSuccess()
        }
    }

    /**
     * 检测权限
     */
    private fun checkPermission(activity: SplashActivity) {
        if (PermissionManager.hasPermission(activity, *PERMISSIONS)) {
            _permissionStatus.value = true
        } else {
            PermissionManager.newTask(activity)
                .permissions(*PERMISSIONS)
                .callback { _permissionStatus.value = it.status == PermissionInfo.Status.Success }
                .request()
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