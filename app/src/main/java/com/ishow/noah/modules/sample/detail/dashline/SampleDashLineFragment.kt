package com.ishow.noah.modules.sample.detail.dashline

import android.graphics.Color
import android.view.View
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleDashLineBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel


/**
 * Created by yuhaiyang on 2018-09-06.
 * 破折线的测试
 */
class SampleDashLineFragment : AppBindFragment<FragmentSampleDashLineBinding, AppBaseViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_sample_dash_line

    fun onViewClick(v: View?) {
        when (v?.id) {
            R.id.colorSet -> {
                var color = binding.colorInput.text.toString()
                if (!color.startsWith("#")) {
                    color = "#$color"
                }
                binding.dashLine.dashColor = Color.parseColor(color)
            }
            R.id.gapSet -> binding.dashLine.dashGap = binding.gapInput.text.toString().toInt()
            R.id.widthSet -> binding.dashLine.dashWidth = binding.widthInput.text.toString().toInt()
        }
    }
}