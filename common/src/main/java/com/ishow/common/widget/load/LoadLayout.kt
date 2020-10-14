package com.ishow.common.widget.load

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.ishow.common.widget.load.status.ALoadStatus

/**
 * Created by yuhaiyang on 2020/9/15.
 * Load的layout
 */
@SuppressLint("ViewConstructor")
class LoadLayout(context: Context) : FrameLayout(context) {

    private var loadStatus: ALoadStatus? = null

    private val defaultLayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    @Synchronized
    internal fun showStatus(status: ALoadStatus?, block: ((view: View) -> Unit)) {
        if (status == null) return

        // 如果状态相同则不进行展示
        if (loadStatus?.type == status.type) {
            return
        }

        Log.i("yhy", "=========================")
        Log.i("yhy", "childCount1 = $childCount")
        // remove last info
        if (childCount > 1) removeViewAt(2)
        loadStatus?.onDetach()
        Log.i("yhy", "childCount2 = $childCount")
        loadStatus = status
        val view = status.buildView(context)
        block.invoke(view)
        addView(view, defaultLayoutParams)
        status.onAttach()
        Log.i("yhy", "childCount3 = $childCount")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loadStatus?.onDetach()
    }
}