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
        val oldContent = view
        val contentParent = oldContent.parent as ViewGroup
        var childIndex = 0
        val childCount = contentParent.childCount
        for (i in 0 until childCount) {
            if (contentParent.getChildAt(i) === oldContent) {
                childIndex = i
                break
            }
        }
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context)
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }

}