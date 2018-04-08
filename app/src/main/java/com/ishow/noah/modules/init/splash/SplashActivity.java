package com.ishow.noah.modules.init.splash;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ishow.common.utils.permission.PermissionDenied;
import com.ishow.common.utils.permission.PermissionGranted;
import com.ishow.common.utils.router.AppRouter;
import com.ishow.noah.constant.Configure;
import com.ishow.noah.modules.account.login.LoginActivity;
import com.ishow.noah.modules.base.AppBaseActivity;
import com.ishow.noah.modules.init.GuideActivity;
import com.ishow.noah.modules.main.MainActivity;

/**
 * Created by yuhaiyang on 2018/3/27.
 * Splash
 */

public class SplashActivity extends AppBaseActivity implements SplashContract.View {


    private SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        mPresenter = new SplashPresenter(this);
        mPresenter.preInit(this);
        mPresenter.checkPermission(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter.isInitFinished()) {
            // 稍微延迟一下,体验更好
            nextDelay(800);
        }
    }

    @Override
    @SuppressWarnings("unused")
    @PermissionGranted(SplashPresenter.REQUEST_PERMISSION_CODE)
    public void permissionGranted() {
        mPresenter.init(SplashActivity.this);
    }

    @SuppressWarnings("unused")
    @PermissionDenied(SplashPresenter.REQUEST_PERMISSION_CODE)
    public void permissionDenied() {
        finish();
    }


    /**
     * 延迟进入
     */
    public void nextDelay(long time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                next();
            }
        }, time);
    }


    @Override
    public void next() {
        if (isActivityPaused()) {
            return;
        }

        if (!mPresenter.isInitFinished()){
            return;
        }

        final long walkedTime = System.currentTimeMillis() - mPresenter.getInitStartTime();
        if (walkedTime < Configure.SPLASH_TIME) {
            nextDelay( Configure.SPLASH_TIME - walkedTime);
            return;
        }

        switch (mPresenter.getTarget()) {
            case SplashPresenter.Target.MAIN:
                AppRouter.with(this)
                        .target(MainActivity.class)
                        .finishSelf()
                        .start();
                break;
            case SplashPresenter.Target.LOGIN:
                AppRouter.with(this)
                        .target(LoginActivity.class)
                        .finishSelf()
                        .start();
                break;
            case SplashPresenter.Target.GUIDE:
                AppRouter.with(this)
                        .target(GuideActivity.class)
                        .finishSelf()
                        .start();
                break;
        }
    }


    // ======= 系统配置 ===========//
    @Override
    protected void resetStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View rootView = getWindow().getDecorView();
            rootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    /**
     * 是否显示版本更新的Dialog
     */
    @Override
    protected boolean needShowUpdateVersionDialog() {
        return false;
    }
}
