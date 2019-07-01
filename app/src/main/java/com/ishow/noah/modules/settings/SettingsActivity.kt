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

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.ishow.common.utils.AppUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.TopBar
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.noah.R
import com.ishow.noah.modules.account.login.LoginActivity
import com.ishow.noah.modules.base.AppBaseActivity
import com.ishow.noah.modules.egg.EggActivity
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by yuhaiyang on 2017/4/24.
 * 设置
 */

class SettingsActivity : AppBaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun initViews() {
        super.initViews()
        logout.setOnClickListener(this)
        version.setText(AppUtils.getVersionName(this))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.logout -> logout()
        }
    }

    private fun logout() {
        BaseDialog.Builder(this)
                .setMessage(R.string.ensure_logout)
                .setMessageGravity(Gravity.CENTER)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.yes){ _, _ ->
                    AppRouter.with(this)
                        .target(LoginActivity::class.java)
                        .flag(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        .finishSelf()
                        .start()}
                .show()
    }
}
