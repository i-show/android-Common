package com.ishow.common.widget.load.target

import android.view.View
import android.view.ViewGroup
import com.ishow.common.widget.load.LoadLayout
import com.ishow.common.widget.load.Loader

/**
 * Created by yuhaiyang on 2020/9/18.
 *
 */

class ViewTarget(private val view: View) : ITarget {
    override fun replace(loader: Loader): LoadLayout {
        val contentParent = view.parent as ViewGroup
        var childIndex = 0
        val childCount = contentParent.childCount
        for (i in 0 until childCount) {
            if (contentParent.getChildAt(i) === view) {
                childIndex = i
                break
            }
        }
        contentParent.removeView(view)
        val oldLayoutParams = view.layoutParams
        val loadLayout = LoadLayout(view.context)
        loadLayout.addView(view)

        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}