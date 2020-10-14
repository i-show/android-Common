package com.ishow.noah.ui.widget.load

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.ishow.common.utils.AnimatorUtils
import com.ishow.common.widget.load.status.ALoadingStatus
import com.ishow.noah.R

/**
 * Created by yuhaiyang on 2020/9/18.
 *
 */
class AppLoadingStatus : ALoadingStatus() {
    private lateinit var image: ImageView
    override fun getLayout(): Int {
        return R.layout.layout_loader_loading_status
    }

    override fun onViewCreate(context: Context, view: View) {
        super.onViewCreate(context, view)
        image = view.findViewById(R.id.image)
    }

    override fun onAttach() {
        super.onAttach()
        AnimatorUtils.breath(image)
    }

    override fun onDetach() {
        super.onDetach()
        image.clearAnimation()
    }
}