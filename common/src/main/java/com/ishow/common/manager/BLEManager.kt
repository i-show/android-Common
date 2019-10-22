@file:Suppress("unused")

package com.ishow.common.manager

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.ishow.common.utils.log.LogUtils
import com.ishow.common.utils.permission.PermissionManager
import java.util.*


/**
 * Created by yuhaiyang on 2019-10-16.
 * BLE 方便处理的
 */

class BLEManager private constructor() {
    /**
     * 是否已经注册了蓝牙状态变更的广播
     */
    private var isRegisterStatusReceiver = false
    /**
     * 蓝牙监听的广播
     */
    private var statusReceiver: StatusReceiver? = null

    private var btStatusListeners: MutableList<OnStatusChangedListener>? = null
    private var btStatusBlocks: MutableList<(status: Int) -> Unit>? = null

    private var bluetoothGatt: BluetoothGatt? = null
    private var innerGattCallback = GattCallBack()
    private var gattCallbackList: MutableList<BluetoothGattCallback> = mutableListOf()
    /**
     * 注册监听状态改变广播
     */
    fun registerStatusReceiver(context: Context, listener: OnStatusChangedListener? = null) {
        registerStatusReceiver(context)
        addOnBTStatusListener(listener)
    }

    /**
     * 注册监听状态改变广播
     */
    fun registerStatusReceiver(context: Context, listener: ((status: Int) -> Unit)? = null) {
        registerStatusReceiver(context)
        addOnBTStatusListener(listener)
    }

    private fun registerStatusReceiver(context: Context) {
        if (isRegisterStatusReceiver) {
            return
        }
        val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        val receiver = StatusReceiver()
        context.applicationContext.registerReceiver(receiver, filter)
        isRegisterStatusReceiver = true
        statusReceiver = receiver
    }

    /**
     * 设置状态监听
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun addOnBTStatusListener(listener: OnStatusChangedListener?) {
        listener?.let {
            if (btStatusListeners == null) {
                btStatusListeners = mutableListOf()
            }

            btStatusListeners?.add(listener)
        }
    }

    fun removeOnBTStatusListener(listener: OnStatusChangedListener) {
        btStatusListeners?.remove(listener)
    }

    /**
     * 设置状态监听
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun addOnBTStatusListener(listener: ((status: Int) -> Unit)?) {
        listener?.let {
            if (btStatusBlocks == null) {
                btStatusBlocks = mutableListOf()
            }
            btStatusBlocks?.add(listener)
        }
    }

    fun unregisterStatusReceiver(context: Context) {
        statusReceiver?.let {
            context.applicationContext.unregisterReceiver(it)
        }

        btStatusListeners?.clear()
        btStatusBlocks?.clear()
    }


    fun startScan(callback: ScanCallback, filter: ScanFilter? = null, settings: ScanSettings? = null) {
        if (filter == null) {
            startScan(callback, emptyList(), settings)
        } else {
            val list = mutableListOf(filter)
            startScan(callback, list, settings)
        }
    }

    @SuppressLint("MissingPermission")
    @Suppress("MemberVisibilityCanBePrivate")
    fun startScan(callback: ScanCallback, filter: List<ScanFilter>, settings: ScanSettings? = null) {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        if (settings == null) {
            adapter.bluetoothLeScanner.startScan(filter, ScanSettings.Builder().build(), callback)
        } else {
            adapter.bluetoothLeScanner.startScan(filter, settings, callback)
        }
    }

    @SuppressLint("MissingPermission")
    fun stopScan(callback: ScanCallback) {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        adapter.bluetoothLeScanner.stopScan(callback)
    }

    fun connectGatt(context: Context, address: String?, callback: BluetoothGattCallback): BluetoothGatt? {
        if (address.isNullOrEmpty()) return null
        // 暂时不考虑多设备连接情况
        val adapter = BluetoothAdapter.getDefaultAdapter()
        val device = adapter.getRemoteDevice(address)
        if (device == null) {
            LogUtils.i(TAG, "connect: device not found")
            return null
        }
        addGattCallBack(callback)
        bluetoothGatt = device.connectGatt(context.applicationContext, false, innerGattCallback)
        return bluetoothGatt
    }

    fun addGattCallBack(callback: BluetoothGattCallback) {
        if (gattCallbackList.contains(callback)) {
            LogUtils.i(TAG, "addGattCallBack: already add")
            return
        }
        gattCallbackList.add(callback)
    }

    fun removeGattCallBack(callback: BluetoothGattCallback) {
        gattCallbackList.remove(callback)
    }

    /**
     * 断开当前连接
     */
    fun disconnectGatt() = bluetoothGatt?.disconnect()

    /**
     * 关闭当前连接， 如果连接不关闭 那么后面会导致一段时间连接不上设备
     */
    fun closeGatt() {
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ == 0) {
            LogUtils.i(TAG, "readCharacteristic: false")
        }

        val result = bluetoothGatt?.readCharacteristic(characteristic)
        LogUtils.i(TAG, "readCharacteristic: result = $result")
    }

    fun writeCharacteristic(characteristic: BluetoothGattCharacteristic) {
        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE == 0) {
            Log.i(TAG, "writeCharacteristic: writeCharacteristic false")
        } else {
            Log.i(TAG, "writeCharacteristic: writeCharacteristic ok")
        }

        val result = bluetoothGatt?.writeCharacteristic(characteristic)
        Log.i(TAG, "writeCharacteristic: result = $result")
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification.  False otherwise.
     */
    fun setCharacteristicNotification(characteristic: BluetoothGattCharacteristic, enabled: Boolean) {
        if (bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized")
            return
        }

        val gatt = bluetoothGatt!!
        val result = gatt.setCharacteristicNotification(characteristic, enabled)
        val parentWriteType = characteristic.writeType
        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        Log.i(TAG, "setCharacteristicNotification: success = $result")

        val descriptor = characteristic.getDescriptor(CLIENT_CHARACTERISTIC_CONFIG)
        descriptor.value = if (enabled) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        val descriptorResult = gatt.writeDescriptor(descriptor)
        Log.i(TAG, "setCharacteristicNotification: descriptorResult = $descriptorResult")
        characteristic.writeType = parentWriteType
    }

    fun getCharacteristic(sUUID: String, cUUID: String): BluetoothGattCharacteristic? {
        val service = bluetoothGatt?.getService(UUID.fromString(sUUID))
        if (service == null) {
            Log.i(TAG, "getCharacteristic:  service is null")
            return null
        }

        return service.getCharacteristic(UUID.fromString(cUUID))
    }

    /**
     * 状态改变
     */
    interface OnStatusChangedListener {
        /**
         * @param status 蓝牙的当前状态 例如：BluetoothAdapter.STATE_OFF
         */
        fun onBluetoothStatusChanged(status: Int)
    }

    inner class StatusReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> syncBluetoothStatus(intent)
            }
        }

        private fun syncBluetoothStatus(intent: Intent) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
            btStatusListeners?.forEach { it.onBluetoothStatusChanged(state) }
            btStatusBlocks?.forEach { it(state) }
        }
    }

    inner class GattCallBack : BluetoothGattCallback() {
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            gattCallbackList.forEach { it.onServicesDiscovered(gatt, status) }
        }

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> gatt.discoverServices()
                BluetoothProfile.STATE_DISCONNECTED -> gatt.close()
            }

            gattCallbackList.forEach { it.onConnectionStateChange(gatt, status, newState) }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            super.onCharacteristicChanged(gatt, characteristic)
            gattCallbackList.forEach { it.onCharacteristicChanged(gatt, characteristic) }
        }

        override fun onCharacteristicRead(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicRead(gatt, characteristic, status)
            gattCallbackList.forEach { it.onCharacteristicRead(gatt, characteristic, status) }
        }

        override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
            super.onCharacteristicWrite(gatt, characteristic, status)
            gattCallbackList.forEach { it.onCharacteristicWrite(gatt, characteristic, status) }
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorRead(gatt, descriptor, status)
            gattCallbackList.forEach { it.onDescriptorRead(gatt, descriptor, status) }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            super.onDescriptorWrite(gatt, descriptor, status)
            gattCallbackList.forEach { it.onDescriptorWrite(gatt, descriptor, status) }
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            super.onMtuChanged(gatt, mtu, status)
            gattCallbackList.forEach { it.onMtuChanged(gatt, mtu, status) }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPhyRead(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyRead(gatt, txPhy, rxPhy, status)
            gattCallbackList.forEach { it.onPhyRead(gatt, txPhy, rxPhy, status) }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onPhyUpdate(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
            gattCallbackList.forEach { it.onPhyUpdate(gatt, txPhy, rxPhy, status) }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            super.onReadRemoteRssi(gatt, rssi, status)
            gattCallbackList.forEach { it.onReadRemoteRssi(gatt, rssi, status) }
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            super.onReliableWriteCompleted(gatt, status)
            gattCallbackList.forEach { it.onReliableWriteCompleted(gatt, status) }
        }


    }

    companion object {
        private const val TAG = "BLEManager"
        @Volatile
        private var sInstance: BLEManager? = null

        private val CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        /**
         * Android 6.0 以后所必需的的请求权限
         */
        private val PERMISSION = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        /**
         * 请求蓝牙打开时的权限
         */
        const val REQUEST_BT_PERMISSION = 1000
        /**
         * 打开蓝牙
         */
        const val REQUEST_OPEN_BT = 2000

        /**
         * 单例模式
         */
        val instance: BLEManager
            get() =
                sInstance ?: synchronized(BLEManager::class.java) {
                    sInstance ?: BLEManager().also { sInstance = it }
                }

        /**
         * 是否支持Ble
         */
        fun isSupportBLE(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        }

        /**
         * 蓝牙是否可用
         */
        fun isBTEnabled(): Boolean = BluetoothAdapter.getDefaultAdapter().isEnabled

        /**
         * 打开蓝牙
         */
        fun openBT(activity: Activity, requestCode: Int = REQUEST_OPEN_BT) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(intent, requestCode)
        }

        fun isGpsProviderEnabled(context: Context): Boolean {
            val service = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return service.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        /**
         * 是否已经有权限了
         */
        fun hasPermission(context: Context) = PermissionManager.hasPermission(context, *PERMISSION)

        /**
         * 请求权限
         */
        fun requestPermission(activity: Activity, requestCode: Int = REQUEST_BT_PERMISSION) {
            PermissionManager.with(activity)
                .requestCode(requestCode)
                .permission(*PERMISSION)
                .send()
        }

        /**
         * @return Returns **true** if property is writable
         */
        fun isCharacteristicWritable(pChar: BluetoothGattCharacteristic): Boolean {
            return pChar.properties and (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) != 0
        }

        /**
         * @return Returns **true** if property is Readable
         */
        fun isCharacteristicReadable(pChar: BluetoothGattCharacteristic): Boolean {
            return pChar.properties and BluetoothGattCharacteristic.PROPERTY_READ != 0
        }

        /**
         * @return Returns **true** if property is supports notification
         */
        fun isCharacteristicNotifiable(pChar: BluetoothGattCharacteristic): Boolean {
            return pChar.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0
        }

        /**
         * 工具二进制转String
         */
        @JvmStatic
        fun byte2String(data: ByteArray): String {
            val sb = StringBuilder(data.size * 2)
            for (b in data) {
                val v: Int = b.toInt() and 0xff
                if (v < 16) {
                    sb.append('0')
                }
                sb.append(Integer.toHexString(v))
            }
            return sb.toString().toUpperCase(Locale.getDefault())
        }
    }


}