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

package com.ishow.noahark.modules.sample.http;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.http.rest.HttpError;
import com.ishow.common.utils.http.rest.callback.JsonCallBack;
import com.ishow.common.utils.http.rest.callback.StringCallBack;
import com.ishow.common.widget.loading.LoadingDialog;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.sample.entries.SampleJson;

/**
 * Created by bright.yu on 2017/2/21.
 * Http测试类
 */
public class SampleHttpActivity extends AppBaseActivity implements View.OnClickListener {
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_http);
        Http.init(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        View view = findViewById(R.id.sample_select_http_get);
        view.setOnClickListener(this);

        view = findViewById(R.id.sample_select_http_post);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sample_select_http_get:
                testGet();
                break;
            case R.id.sample_select_http_post:
                testPost();
                break;
        }
    }

    private void testGet() {
        mLoadingDialog = LoadingDialog.show(this, mLoadingDialog);
        Http.get()
                .url("https://soatest.cn.nuskin.com/ws/api/auth/loginWithID?password=a11111&loginid=1234567")
                .execute(new JsonCallBack<SampleJson>() {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        LoadingDialog.dismiss(mLoadingDialog);
                        dialog(error.getMessage());
                    }

                    @Override
                    protected void onSuccess(SampleJson result) {
                        LoadingDialog.dismiss(mLoadingDialog);
                        dialog(result.toString());
                    }
                });
    }

    private void testPost() {
        Http.post()
                .url("https://test.cn.nuskin.com/ws/api/account/terminatedAccount")
                .params("{\"mailOrPhone\":\"15900667472\",\"sponsorId\":\"CN2173000\",\"taxId\":\"370306198112143925\"}")
                .execute(new StringCallBack() {
                    @Override
                    protected void onFailed(@NonNull HttpError error) {
                        dialog(error.getMessage());
                    }

                    @Override
                    protected void onSuccess(String result) {
                        dialog(result);
                    }
                });

    }

}
