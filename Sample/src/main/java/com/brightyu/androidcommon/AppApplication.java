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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);


        ImageLoader.init(this);
    }
}
