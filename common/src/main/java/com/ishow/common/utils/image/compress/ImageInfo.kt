package com.ishow.common.utils.image.compress

/**
 * Created by yuhaiyang on 2019-12-24.
 *
 */
class ImageInfo(val position: Int) {
    /**
     * 图片多大
     */
    var byteCount: Long = 0
    /**
     * 图片宽度
     */
    var width: Int = 0
    /**
     * 图片高度
     */
    var height: Int = 0
    /**
     * 图片的mimeType  image/jpeg  image/png
     */
    var mimeType: String? = null

    override fun toString(): String {
        return "ImageInfo(byteCount=$byteCount, width=$width, height=$height)"
    }
}