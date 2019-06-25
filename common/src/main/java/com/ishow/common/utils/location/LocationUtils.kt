package com.ishow.common.utils.location

import android.content.Context
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.ishow.common.entries.Location
import com.ishow.common.utils.log.LogUtils

/**
 * 位置信息获取
 * Created by menghuihui on 17/6/16.
 * modify by yuhaiyang on 2018-06-22
 */
object LocationUtils {
    private const val TAG = "LocationUtils"

    /**
     * 获取当前位置信息
     */
    @JvmStatic
    @Suppress("unused")
    fun getLocation(context: Context, listener: OnLocationListener) {
        val option = LocationClientOption()
        /*
         * 设置为高精度模式
         * LocationMode.Hight_Accuracy：高精度
         * LocationMode. Battery_Saving：低功耗
         * LocationMode. Device_Sensors：仅使用设备
         */
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        /*
         * 设置返回经纬度坐标类型
         * gcj02：国测局坐标；
         * bd09ll：百度经纬度坐标；
         * bd09：百度墨卡托坐标；
         * 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
         */
        option.setCoorType("gcj02")
        option.setIsNeedAddress(true)
        option.isOpenGps = true

        val client = LocationClient(context.applicationContext)
        client.registerLocationListener(BaiduLocationListener(client, listener))
        client.locOption = option
        client.start()
        // 如果之前已经获取到 那么先把之前获取到的提供出来
        notificationOnReceiveLocation(listener, client.lastKnownLocation)
    }


    /**
     * 注销位置监听
     */
    private fun unregisterLocationListener(client: LocationClient?, listener: BDLocationListener) {
        if (client != null) {
            client.stop()
            client.unRegisterLocationListener(listener)
        }
    }

    /**
     * 通知已经接收到位置信息
     */
    private fun notificationOnReceiveLocation(listener: OnLocationListener?, bdLocation: BDLocation?) {
        if (listener == null || bdLocation == null) {
            LogUtils.e(TAG, "notificationOnReceiveLocation: listener is null or BDLocation is null")
            return
        }
        val location = Location()
        location.latitude = bdLocation.latitude
        location.longitude = bdLocation.longitude
        location.address = bdLocation.address
        listener.onReceiveLocation(location)
    }

    /**
     * 百度的位置监听
     */
    private class BaiduLocationListener constructor(
        /**
         * 百度的Location信息
         */
        private val client: LocationClient,
        /**
         * BaseLib中的监听回调
         */
        private val listener: OnLocationListener
    ) : BDLocationListener {

        override fun onReceiveLocation(bdLocation: BDLocation) {
            LogUtils.d(TAG, "位置信息: " + bdLocation.addrStr)
            notificationOnReceiveLocation(listener, bdLocation)
            unregisterLocationListener(client, this)
        }
    }


}
