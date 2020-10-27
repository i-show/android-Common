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
    private var statusView: View? = null

    private val defaultLayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    @Synchronized
    internal fun showStatus(status: ALoadStatus?, margin: IntArray, block: ((view: View) -> Unit)) {
        if (status == null) return
        // 如果状态相同则不进行展示
        if (loadStatus?.type == status.type) {
            statusView?.visibility = View.VISIBLE
            return
        }
        // remove last info
        if (childCount > 1) {
            val child = getChildAt(1)
            removeView(child)
        }
        loadStatus?.onDetach()
        loadStatus = status
        val view = status.buildView(context)
        block.invoke(view)

        addView(view, getSirLayoutParams(margin))
        status.onAttach()
        statusView = view
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loadStatus?.onDetach()
    }

    fun dismiss() {
        loadStatus = null
        statusView?.let { removeView(it) }
    }

    private fun getSirLayoutParams(margin: IntArray): LayoutParams {
        val lp = defaultLayoutParams
        lp.marginStart = margin[0]
        lp.topMargin = margin[1]
        lp.marginEnd = margin[2]
        lp.bottomMargin = margin[3]
        return lp
    }
}