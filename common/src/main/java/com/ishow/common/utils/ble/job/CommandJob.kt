package com.ishow.common.utils.ble.job

import android.bluetooth.BluetoothGattCallback
import com.ishow.common.utils.ble.BLEManager


abstract class CommandJob : BluetoothGattCallback(), Runnable {

    protected val bleManager = BLEManager.instance

    @Volatile
    protected var jobStatus = JobStatus.Run


    override fun run() {
        bleManager.addGattCallBack(this)
        startJob()

        val time = System.currentTimeMillis()
        while (true) {
            if (checkJobStatus()) {
                break
            }

            if (isTimeout(time)) {
                break
            }

            Thread.sleep(80)
        }

        bleManager.removeGattCallBack(this)
        Thread.sleep(100)
    }

    /**
     * 开启当前的Job
     */
    protected abstract fun startJob()

    /**
     * 检测当前Job的状态
     */
    protected open fun checkJobStatus(): Boolean {
        return jobStatus != JobStatus.Run
    }

    /**
     * 当前Job是否超时
     */
    protected open fun isTimeout(startTime: Long): Boolean {
        return System.currentTimeMillis() - startTime > 800
    }

}