package com.ishow.common.utils.image.compress

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */
class ErrorInfo(val position: Int, val message: String) {
    override fun toString(): String {
        return "ErrorInfo(position=$position, message='$message')"
    }
}