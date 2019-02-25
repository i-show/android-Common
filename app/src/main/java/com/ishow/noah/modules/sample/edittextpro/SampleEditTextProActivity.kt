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

package com.ishow.noah.modules.sample.edittextpro

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.ishow.common.extensions.format2Money

import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_sample_edittextpro.*

/**
 * Created by Bright.Yu on 2017/2/10.
 * EditTextPro 的测试
 */

class SampleEditTextProActivity : AppBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_edittextpro)

        val textStr = "HelloWorld"
        val spannableString = SpannableString(textStr)
        spannableString.setSpan(ForegroundColorSpan(Color.RED), 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text.setInputText(spannableString)
    }
}
