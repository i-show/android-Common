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

package com.ishow.noah.modules.sample.http

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.dialog

import com.ishow.common.utils.http.rest.Http
import com.ishow.common.utils.http.rest.HttpError
import com.ishow.common.utils.http.rest.callback.StringCallBack
import com.ishow.common.widget.loading.LoadingDialog
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity

/**
 * Created by bright.yu on 2017/2/21.
 * Http测试类
 */
class SampleHttpActivity : AppBaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_http)
        Http.init(this)
    }

    override fun initViews() {
        super.initViews()
        var view = findViewById<View>(R.id.sample_select_http_get)
        view.setOnClickListener(this)

        view = findViewById(R.id.sample_select_http_post)
        view.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sample_select_http_get -> testGet()
            R.id.sample_select_http_post -> testPost()
        }
    }

    private fun testGet() {
        mLoadingDialog = LoadingDialog.show(this, mLoadingDialog)
        Http.get()
            .url("http://www.qq.com/")
            .execute(object : StringCallBack() {
                override fun onFailed(error: HttpError) {
                    LoadingDialog.dismiss(mLoadingDialog)
                    dialog(error.message)
                }

                override fun onSuccess(result: String) {
                    LoadingDialog.dismiss(mLoadingDialog)
                    dialog(result)
                }
            })
    }

    private fun testPost() {
        Http.post()
            .url("https://test.cn.nuskin.com/ws/api/account/terminatedAccount")
            .params("{\"mailOrPhone\":\"15900667472\",\"sponsorId\":\"CN2173000\",\"taxId\":\"370306198112143925\"}")
            .execute(object : StringCallBack() {
                override fun onFailed(error: HttpError) {
                    dialog(error.message)
                }

                override fun onSuccess(result: String) {
                    dialog(result)
                }
            })

    }

}
