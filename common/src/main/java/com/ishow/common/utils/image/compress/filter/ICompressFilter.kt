package com.ishow.common.utils.image.compress.filter

import com.ishow.common.utils.image.compress.ImageInfo

/**
 * Created by yuhaiyang on 2019-12-24.
 * 压缩的过滤条件
 */
interface ICompressFilter {
    /**
     * 过滤条件
     * @return 是否需要压缩
     */
    fun filter(info: ImageInfo): Boolean
}