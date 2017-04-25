package com.brightyu.androidcommon;

import android.app.Application;

import com.ishow.common.utils.image.loader.ImageLoader;
import com.squareup.leakcanary.LeakCanary;

/**
 * Application
 */
public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ImageLoader.init(this);

        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }


    }
}
