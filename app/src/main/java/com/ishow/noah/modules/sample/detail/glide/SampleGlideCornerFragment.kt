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
        dataBinding.imgUrl =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1600064178217&di=64d2249c6da59ecbe663100c16575a46&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F86%2F10%2F01300000184180121920108394217.jpg"
    }
}