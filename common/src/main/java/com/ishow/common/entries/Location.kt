package com.ishow.common.entries

import com.baidu.location.Address

/**
 * Created by yuhaiyang on 2018/6/22.
 * 定位信息
 */
class Location {
    /**
     * 地址
     */
    var address: Address? = null
    /**
     * 纬度
     */
    var latitude: Double = 0.toDouble()
    /**
     * 经度
     */
    var longitude: Double = 0.toDouble()

    override fun toString(): String {
        return "Location(address=$address, latitude=$latitude, longitude=$longitude)"
    }
}
