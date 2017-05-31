package com.ishow.noahark;

import android.app.Application;

import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.squareup.leakcanary.LeakCanary;

/**
 * Application
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        Http.init(this);
        ImageLoader.init(this);
    }
}
