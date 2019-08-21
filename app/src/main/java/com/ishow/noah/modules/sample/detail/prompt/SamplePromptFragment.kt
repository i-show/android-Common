package com.ishow.noah.modules.sample.detail.prompt

import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSamplePermissionBinding
import com.ishow.noah.modules.base.mvvm.AppBindFragment

/**
 * 关于提示信息类别的测试类
 */
class SamplePromptFragment : AppBindFragment<FragmentSamplePermissionBinding>() {

    override fun getLayout(): Int {
        return R.layout.fragement_sample_prompt
    }
}
