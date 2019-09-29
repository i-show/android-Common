package com.ishow.common.utils.location


import com.ishow.common.entries.Location

/**
 * modify by y.haiyang @2018-6-22
 * 位置信息监听
 */
interface OnLocationListener {
    /**
     * 获取到了位置信息
     *  @param status 0成功 1 失败
     */
    fun onStatusChanged(status: Int, location: Location?)
}
