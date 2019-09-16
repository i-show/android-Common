package com.ishow.noah.modules.sample.detail.glide

import android.os.Bundle
import android.view.View
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleGlideCornerBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2019-08-19.
 * GlideCornerSample
 */
class SampleGlideCornerFragment : AppBindFragment<FragmentSampleGlideCornerBinding>() {

    override fun getLayout(): Int = R.layout.fragment_sample_glide_corner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.imgUrl =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566195833357&di=a1a6538f1ac0b083c530d2825a1bb13c&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsports%2F20161220%2FH4Uw-fxytqax6757480.jpg"
    }
}