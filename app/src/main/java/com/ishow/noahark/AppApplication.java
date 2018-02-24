package com.ishow.noahark;

import android.app.Application;

import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.common.utils.router.AppRouter;
import com.ishow.noahark.utils.http.AppHttpCallBack;
import com.ishow.noahark.utils.router.AppRouterConfigure;

/**
 * Application
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Http.init(this);
        ImageLoader.init(this);

        AppRouter.setConfigure(new AppRouterConfigure());
    }
}
