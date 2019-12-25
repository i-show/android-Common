package com.ishow.common.utils.image.compress

import java.io.File

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */
class CompressResult {
    var code: Int = STATUS_SUCCESS
    var image: File? = null
    lateinit var imageList: MutableList<File?>
    var errorList: MutableList<ErrorInfo>? = null

    fun isSuccess() = code == STATUS_SUCCESS


    fun addError(error: ErrorInfo) {
        if (errorList == null) {
            errorList = mutableListOf(error)
        } else {
            errorList?.add(error)
        }
    }

    companion object {
        /**
         * 成功了
         */
        const val STATUS_SUCCESS = 0
        /**
         * 失败了
         */
        const val STATUS_FAILED = 1
    }
}