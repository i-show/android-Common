package com.ishow.noah.ui.widget.load

import android.content.Context
import android.view.View
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

    override fun onViewCreate(context: Context, view: View) {
        super.onViewCreate(context, view)
        val emptyView: View = view.findViewById(R.id.empty)
        emptyView.setOnClickListener { notifyClick(it) }
    }
}