package com.ishow.noah.modules.init.splash

import android.Manifest
import android.content.Context
import android.text.TextUtils

import com.alibaba.fastjson.JSONObject
import com.baidu.mobstat.StatService
import com.ishow.common.utils.DeviceUtils
import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.common.utils.permission.PermissionManager
import com.ishow.noah.constant.Configure
import com.ishow.noah.constant.Url
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.ConfigureManager
import com.ishow.noah.manager.UserManager
import com.ishow.noah.manager.VersionManager
import com.ishow.noah.modules.base.AppBaseActivity
import com.ishow.noah.utils.http.AppHttpCallBack

/**
 * Created by yuhaiyang on 2018/3/27.
 * Presenter
 */

internal class SplashPresenter(private val mView: SplashContract.View) : SplashContract.Presenter {

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

    /**
     * 初始化的时间
     */
    override var initStartTime: Long = 0
    /**
     * 登录完成
     */
    override var isInitFinished: Boolean = false

    private var mLoginSuccess: Boolean = false


    override val target: Target
        get() {
            if (VersionManager.isFirstEnterThisVersion()) {
                return Target.GUIDE
            }
            return if (mLoginSuccess) Target.MAIN else Target.LOGIN
        }

    override fun preInit(context: Context) {
        // 百度统计 - 进入就开始初始化
        StatService.start(context)
        initStartTime = System.currentTimeMillis()
        isInitFinished = false
        mLoginSuccess = false
    }

    override fun checkPermission(activity: AppBaseActivity) {
        if (PermissionManager.hasPermission(activity, *PERMISSIONS)) {
            mView.permissionGranted()
        } else {
            PermissionManager.with(activity)
                    .permission(*PERMISSIONS)
                    .annotationClass(activity)
                    .requestCode(REQUEST_PERMISSION_CODE)
                    .send()
        }
    }

    override fun init(activity: SplashActivity) {
        initStartTime = System.currentTimeMillis()
        isInitFinished = false
        mLoginSuccess = false
        // 配置管理
        ConfigureManager.getInstance().init(activity)
        // 更新版本信息
        VersionManager.getInstance().init(activity)
        // 登录
        login(activity)
    }


    /**
     * 验证登录
     */
    private fun login(context: Context) {
        val accessToken = UserManager.getInstance().getAccessToken(context)
        if (TextUtils.isEmpty(accessToken)) {
            isInitFinished = true
            mView.next()
            return
        }

        val params = JSONObject()
        params["device"] = Configure.DEVICE
        params["deviceModel"] = DeviceUtils.model()
        params["deviceVersion"] = DeviceUtils.version()

        Http.post()
                .url(Url.loginByToken())
                .params(params.toJSONString())
                .addHeader(Configure.HTTP_TOKEN, accessToken)
                .execute(object : AppHttpCallBack<UserContainer>(context) {
                    override fun onFailed(error: HttpError) {
                        isInitFinished = true
                        mLoginSuccess = false
                        mView.next()
                    }

                    override fun onSuccess(result: UserContainer) {
                        val userManager = UserManager.getInstance()
                        userManager.setUserContainer(context, result)
                        isInitFinished = true
                        mLoginSuccess = true
                        mView.next()
                    }
                })
    }


    enum class Target {
        /**
         * 主页
         */
        MAIN,
        /**
         * 登录页
         */
        LOGIN,
        /**
         * 引导页
         */
        GUIDE
    }


}
