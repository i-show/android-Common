package com.ishow.common.utils.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.util.Log
import androidx.fragment.app.Fragment
import com.ishow.common.entries.Location
import com.ishow.common.extensions.locationManager
import com.ishow.common.utils.permission.PermissionManager
import java.io.IOException
import java.util.*

/**
 * 位置信息获取
 * Created by menghuihui on 17/6/16.
 * modify by yuhaiyang on 2018-06-22
 */
object LocationUtils {
    private const val TAG = "LocationUtils"
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    fun getLocation(context: Context, listener: OnLocationListener? = null) {
        if (!PermissionManager.hasPermission(context, *permissions)) {
            Log.e(TAG, "no location permission")
            return
        }
        requestLocation(context, listener)
    }

    fun getLocation(context: Activity, listener: OnLocationListener? = null) {
        if (!PermissionManager.hasPermission(context, *permissions)) {
            PermissionManager.with(context)
                .permission(*permissions)
                .send()
            return
        }
        requestLocation(context, listener)
    }

    fun getLocation(fragment: Fragment, listener: OnLocationListener? = null) {
        if (fragment.context == null) {
            Log.e(TAG, "fragment  context is null")
            return
        }
        val context = fragment.context!!
        if (!PermissionManager.hasPermission(context, *permissions)) {
            PermissionManager.with(fragment)
                .permission(*permissions)
                .send()
            return
        }
        requestLocation(context, listener)
    }

    /**
     * 获取当前位置信息
     */

    @JvmStatic
    @Suppress("unused")
    @SuppressLint("MissingPermission")
    private fun requestLocation(context: Context, listener: OnLocationListener? = null) {

        // 构建位置查询条件
        val criteria = Criteria()
        /**
         * 设置定位精确度
         * Criteria.ACCURACY_COARSE比较粗略，
         * Criteria.ACCURACY_FINE则比较精细
         */
        criteria.bearingAccuracy = Criteria.ACCURACY_COARSE
        // 设置是否要求速度
        criteria.isSpeedRequired = false
        // 是否查询海拨：否
        criteria.isAltitudeRequired = false
        // 是否查询方位角 : 否
        criteria.isBearingRequired = false
        // 是否允许付费：是
        criteria.isCostAllowed = false
        criteria.accuracy = Criteria.ACCURACY_MEDIUM
        // 电量要求：低
        criteria.powerRequirement = Criteria.POWER_MEDIUM
        val manager = context.locationManager
        val bestProvider = manager.getBestProvider(criteria, true)

        val location = manager.getLastKnownLocation(bestProvider!!)
        if (location == null) {
            listener?.onStatusChanged(1, null)
            return
        }

        val geoCoder = Geocoder(context, Locale.CHINESE)
        var addresses: List<Address> = ArrayList()
        try {
            addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: IOException) {
            listener?.onStatusChanged(1, null)
            e.printStackTrace()
        }

        if (addresses.isEmpty()) {
            listener?.onStatusChanged(1, null)
            return
        }
        val address = addresses[0]

        listener?.let {
            val result = Location()
            result.latitude = location.latitude
            result.longitude = location.longitude
            result.address = address
            it.onStatusChanged(0, result)
        }
    }

}
