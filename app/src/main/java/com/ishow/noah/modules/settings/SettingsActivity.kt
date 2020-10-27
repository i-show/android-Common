/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.noah.modules.settings

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.loadUrl
import com.ishow.common.utils.AppUtils
import com.ishow.common.widget.load.ext.loadSir
import com.ishow.common.widget.load.ext.sirEmpty
import com.ishow.common.widget.load.ext.withLoadSir
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_settings.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit


/**
 * Created by yuhaiyang on 2017/4/24.
 * 设置
 */

class SettingsActivity : AppBaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        withLoadSir()
    }

    override fun initViews() {
        super.initViews()
        logout.setOnClickListener(this)
        version.setText(AppUtils.getVersionName(this))

        image.loadUrl("https://img.yuhaiyang.net/common/avatar/default_avatar.jpg")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout -> logout()
        }
    }

    private fun logout() {
        sirEmpty()
    }
}
