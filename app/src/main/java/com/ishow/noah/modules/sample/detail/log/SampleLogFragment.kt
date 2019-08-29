/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.sample.detail.log

import android.os.Bundle
import android.view.View
import com.ishow.common.manager.CCacheManager
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleLogBinding
import com.ishow.noah.modules.base.mvvm.AppBindFragment

/**
 * Created by Bright.Yu on 2017/2/8.
 * Log测试
 */

class SampleLogFragment : AppBindFragment<FragmentSampleLogBinding>() {

    override fun getLayout(): Int = R.layout.fragment_sample_log

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.fragment = this
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.request -> requestLog()
        }
    }

    private fun requestLog() {
        CCacheManager.cache(context)
    }

}
