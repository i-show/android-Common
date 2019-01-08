package com.ishow.common.entries;

import com.baidu.location.Address;

/**
 * Created by yuhaiyang on 2018/6/22.
 * 定位信息
 */
public class Location {
    /**
     * 地址
     */
    private Address address;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 经度
     */
    private double longitude;


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    @SuppressWarnings({"NullableProblems", "ConstantConditions"})
    public String toString() {
        if(address != null){
            return address.address;
        }else{
            return null;
        }
    }
}
