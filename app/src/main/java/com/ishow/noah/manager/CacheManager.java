package com.ishow.noah.manager;

import android.content.Context;

import java.lang.ref.WeakReference;

public class CacheManager {

    /**
     * 这个东西使用后可以被回收
     */
    private volatile static WeakReference<CacheManager> sInstance;



    private CacheManager() {
    }

    public static CacheManager getInstance() {

        if (sInstance == null || sInstance.get() == null) {
            synchronized (ConfigureManager.class) {
                if (sInstance == null || sInstance.get() == null) {
                    CacheManager manager = new CacheManager();
                    sInstance = new WeakReference<>(manager);
                }
            }
        }

        return sInstance.get();
    }

    public void clearCache(Context context){
        // TODO
    }
}
