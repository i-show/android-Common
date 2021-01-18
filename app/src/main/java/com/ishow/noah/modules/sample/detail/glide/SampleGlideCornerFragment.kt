package com.ishow.noah.modules.sample.detail.glide

import android.os.Bundle
import android.view.View
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleGlideCornerBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by yuhaiyang on 2019-08-19.
 * GlideCornerSample
 */
class SampleGlideCornerFragment : AppBindFragment<FragmentSampleGlideCornerBinding, AppBaseViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_sample_glide_corner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgUrl = "https://img.i-show.club/d.jpg"
    }
}