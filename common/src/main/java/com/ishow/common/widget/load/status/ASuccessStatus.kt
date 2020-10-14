package com.ishow.common.widget.load.status

import com.ishow.common.widget.load.Loader

/**
 * Created by yuhaiyang on 2020/9/15.
 * 成功的加载状态
 */
abstract class ASuccessStatus : ALoadStatus(Loader.Type.Success)