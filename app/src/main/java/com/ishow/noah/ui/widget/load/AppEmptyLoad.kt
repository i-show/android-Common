package com.ishow.noah.ui.widget.load

import com.ishow.common.widget.load.status.EmptyLoadStatus
import com.ishow.noah.R

/**
 * Created by yuhaiyang on 2020/9/18.
 *
 */
class AppEmptyLoad : EmptyLoadStatus() {
    override fun getLayout(): Int {
        return R.layout.layout_empty_load
    }
}