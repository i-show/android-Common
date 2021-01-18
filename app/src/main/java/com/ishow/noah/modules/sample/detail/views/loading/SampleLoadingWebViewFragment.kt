/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.sample.detail.views.loading

import android.os.Bundle
import android.view.View
import com.ishow.common.utils.WebViewUtils
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleLoadingWebviewBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * Created by yuhaiyang on 2017/6/26.
 * LoadingWebView测试
 */

class SampleLoadingWebViewFragment : AppBindFragment<FragmentSampleLoadingWebviewBinding, AppBaseViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_sample_loading_webview


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        WebViewUtils.init(binding.webView)
        binding.webView.loadUrl("https://juejin.cn/")
    }
}
