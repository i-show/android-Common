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

        Thread.sleep(3000)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("yhy", "onDestroy")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("yhy", "isMain = " + isMainThread() + " ,startId = $startId")
        return super.onStartCommand(intent, flags, startId)
    }
}