package com.ishow.noah.ui.widget.load

import com.ishow.common.widget.load.status.AEmptyStatus
import com.ishow.noah.R

/**
 * Created by yuhaiyang on 2020/9/18.
 *
 */
class AppEmptyLoad : AEmptyStatus() {
    override fun getLayout(): Int {
        return R.layout.layout_empty_load
    }
}