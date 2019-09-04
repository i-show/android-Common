package com.ishow.common.modules.image.select

import android.os.Bundle
import android.view.View
import com.ishow.common.R
import com.ishow.common.app.mvvm.view.BindFragment
import com.ishow.common.databinding.FragmentImagePreviewCommonBinding

/**
 * Created by yuhaiyang on 2019-09-04.
 * 照片预览
 */
class ImagePreviewFragment : BindFragment<FragmentImagePreviewCommonBinding>() {
    override fun getLayout(): Int = R.layout.fragment_image_preview_common

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as ImageSelectorActivity
        dataBinding.vm = activity.viewModel
        dataBinding.fragment = this
    }
}