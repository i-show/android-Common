package com.ishow.common.utils.location;

import android.app.Dialog;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ishow.common.entries.Location;
import com.ishow.common.utils.log.LogManager;

/**
 * 位置信息获取
 * Created by menghuihui on 17/6/16.
 * modify by yuhaiyang on 2018-06-22
 */
public class LocationUtils {
    private static final String TAG = "LocationUtils";

    /**
     * 获取当前位置信息
     */
    @SuppressWarnings("WeakerAccess")
    public static void getLocation(final Context context, final OnLocationListener listener) {
        LocationClientOption option = new LocationClientOption();
        /*
         * 设置为高精度模式
         * LocationMode.Hight_Accuracy：高精度
         * LocationMode. Battery_Saving：低功耗
         * LocationMode. Device_Sensors：仅使用设备
         */
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /*
         * 设置返回经纬度坐标类型
         * gcj02：国测局坐标；
         * bd09ll：百度经纬度坐标；
         * bd09：百度墨卡托坐标；
         * 海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
         */
        option.setCoorType("gcj02");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);

        final LocationClient client = new LocationClient(context.getApplicationContext());
        client.registerLocationListener(new BaiduLocationListener(client, listener));
        client.setLocOption(option);
        client.start();
        // 如果之前已经获取到 那么先把之前获取到的提供出来
        notificationOnReceiveLocation(listener, client.getLastKnownLocation());
    }


    /**
     * 获取当前位置信息
     *
     * @param dialog Loading显示的逻辑，在APP层面进行实现
     */
    @Deprecated
    public static void getCurrentLocation(final Context context, final Dialog dialog, final OnLocationListener listener) {
        getLocation(context, listener);
    }

    /**
     * 注销位置监听
     */
    private static void unregisterLocationListener(LocationClient client, BDLocationListener listener) {
        if (client != null) {
            client.stop();
            client.unRegisterLocationListener(listener);
        }
    }

    /**
     * 通知已经接收到位置信息
     */
    private static void notificationOnReceiveLocation(OnLocationListener listener, BDLocation bdLocation) {
        if (listener == null || bdLocation == null) {
            LogManager.e(TAG, "notificationOnReceiveLocation: listener is null or BDLocation is null");
            return;
        }
        Location location = new Location();
        location.setLatitude(bdLocation.getLatitude());
        location.setLongitude(bdLocation.getLongitude());
        location.setAddress(bdLocation.getAddress());
        listener.onReceiveLocation(location);
    }

    /**
     * 百度的位置监听
     */
    private static class BaiduLocationListener implements BDLocationListener {
        /**
         * 百度的Location信息
         */
        private LocationClient mLocationClient;
        /**
         * BaseLib中的监听回调
         */
        private OnLocationListener mListener;

        private BaiduLocationListener(LocationClient client, OnLocationListener listener) {
            mListener = listener;
            mLocationClient = client;
        }

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LogManager.d(TAG, "位置信息: " + bdLocation.getAddrStr());
            notificationOnReceiveLocation(mListener, bdLocation);
            unregisterLocationListener(mLocationClient, this);
        }
    }


}
