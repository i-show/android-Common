package com.ishow.common.utils.ble.job

import android.bluetooth.BluetoothGatt
import android.content.Context

class ConnectJob(private val context: Context, private val address: String) : CommandJob() {

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (gatt.device.address == address && newState == BluetoothGatt.STATE_DISCONNECTED) {
            jobStatus = JobStatus.Failed
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (gatt.device.address == address) {
            jobStatus = JobStatus.Success
        }
    }

    override fun startJob() {
        bleManager.connectGatt(context, address)
    }

}