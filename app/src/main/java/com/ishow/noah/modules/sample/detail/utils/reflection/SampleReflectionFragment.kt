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

package com.ishow.noah.modules.sample.detail.utils.reflection

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleReflectionBinding
import com.ishow.noah.modules.base.mvvm.AppBindFragment

/**
 * Created by Bright.Yu on 2017/2/8.
 * Log测试
 */

class SampleReflectionFragment : AppBindFragment<FragmentSampleReflectionBinding>() {

    override fun getLayout(): Int = R.layout.fragment_sample_reflection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.fragment = this
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.request -> request()
        }
    }

    private fun request() {
        try {
            val config = Class.forName("com.ishow.noah.BuildConfig")
            val field = config.getField("DEBUG")
            Log.d(TAG, "request: it.name " + field.get(null))
        }catch (e:Exception){

        }

    }

    companion object {
        private const val TAG = "yhy"
    }
}
