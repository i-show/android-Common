package com.ishow.noahark.modules.init.splash;

import android.Manifest;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mobstat.StatService;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.permission.PermissionManager;
import com.ishow.noahark.constant.Configure;
import com.ishow.noahark.constant.Url;
import com.ishow.noahark.entries.UserContainer;
import com.ishow.noahark.manager.ConfigureManager;
import com.ishow.noahark.manager.UserManager;
import com.ishow.noahark.manager.VersionManager;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.utils.http.AppHttpCallBack;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Created by yuhaiyang on 2018/3/27.
 * Presenter
 */

class SplashPresenter implements SplashContract.Presenter {

    private Handler mHandler;
    /**
     * 请求权限的Code
     */
    static final int REQUEST_PERMISSION_CODE = 1001;

    /**
     * 初始化的时间
     */
    private long mInitTime;
    /**
     * 登录完成
     */
    private boolean mLoginFinished;
    private boolean mLoginSuccess;
    /**
     * 请求的权限
     */
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    private SplashContract.View mView;

    SplashPresenter(SplashContract.View view) {
        mView = view;
        mHandler = new Handler();
    }

    @Override
    public void preInit(Context context) {
        // 百度统计 - 进入就开始初始化
        StatService.start(context);
        mInitTime = System.currentTimeMillis();
        mLoginFinished = false;
        mLoginSuccess = false;
    }

    @Override
    public void checkPermission(AppBaseActivity activity) {
        if (PermissionManager.hasPermission(activity, PERMISSIONS)) {
            mView.permissionGranted();
        } else {
            PermissionManager.with(activity)
                    .permission(PERMISSIONS)
                    .annotationClass(activity)
                    .requestCode(REQUEST_PERMISSION_CODE)
                    .send();
        }
    }

    @Override
    public void init(SplashActivity activity) {
        mInitTime = System.currentTimeMillis();
        mLoginFinished = false;
        mLoginSuccess = false;
        // 配置管理
        ConfigureManager.getInstance().init(activity);
        // 更新版本信息
        VersionManager.getInstance().init(activity);
        // 登录
        login(activity);
    }

    @Override
    public long getInitStartTime() {
        return mInitTime;
    }

    @Override
    public boolean isInitFinished() {
        return mLoginFinished;
    }

    @Override
    public int getTarget() {
        if (VersionManager.isFirstEnterThisVerison()) {
            return Target.GUIDE;
        }
        return mLoginSuccess ? Target.MAIN : Target.LOGIN;
    }


    /**
     * 验证登录
     */
    private void login(final Context context) {
        String accessToken = UserManager.getInstance().getAccessToken(context);
        if (TextUtils.isEmpty(accessToken)) {
            mLoginFinished = true;
            mView.next();
            return;
        }

        JSONObject params = new JSONObject();
        params.put("device", Configure.DEVICE);
        params.put("deviceModel", DeviceUtils.model());
        params.put("deviceVersion", DeviceUtils.version());

        Http.post()
                .url(Url.loginByToken())
                .params(params.toJSONString())
                .addHeader(Configure.HTTP_TOKEN, accessToken)
                .execute(new AppHttpCallBack<UserContainer>(context) {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        mLoginFinished = true;
                        mLoginSuccess = false;
                        mView.next();
                    }

                    @Override
                    protected void onSuccess(UserContainer result) {
                        UserManager userManager = UserManager.getInstance();
                        userManager.setUserContainer(context, result);
                        mLoginFinished = true;
                        mLoginSuccess = true;
                        mView.next();
                    }
                });

    }


    @IntDef({Target.MAIN, Target.LOGIN, Target.GUIDE})
    @Retention(RetentionPolicy.SOURCE)
    @interface Target {
        /**
         * 主页
         */
        int MAIN = 0;
        /**
         * 登录页
         */
        int LOGIN = 1;
        /**
         * 引导页
         */
        int GUIDE = 2;
    }

}
