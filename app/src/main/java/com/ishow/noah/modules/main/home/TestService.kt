package com.ishow.noah.modules.main.home

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ishow.common.extensions.isMainThread

class TestService : IntentService("Test") {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.i("yhy", "isMain2 = " + isMainThread())
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("yhy", "isMain = " + isMainThread())
        return super.onStartCommand(intent, flags, startId)
    }
}