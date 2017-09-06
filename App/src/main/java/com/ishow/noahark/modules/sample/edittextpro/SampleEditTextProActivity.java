/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noahark.modules.sample.edittextpro;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.ishow.common.widget.edittext.EditTextPro;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;

/**
 * Created by Bright.Yu on 2017/2/10.
 * EditTextPro 的测试
 */

public class SampleEditTextProActivity extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_edittextpro);

        String text = "HelloWorld";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 5, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        EditTextPro pro = (EditTextPro) findViewById(R.id.text);
        pro.setInputText(spannableString);
    }
}
