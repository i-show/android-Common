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

package com.brightyu.androidcommon.modules.account.password.forgot;

import android.os.Bundle;
import android.view.View;

import com.bright.common.widget.TopBar;
import com.bright.common.widget.VerifyCodeButton;
import com.bright.common.widget.edittext.EditTextPro;
import com.bright.common.widget.loading.LoadingDialog;
import com.brightyu.androidcommon.R;
import com.brightyu.androidcommon.modules.base.AppBaseActivity;

/**
 * 修改密码和重置密码一系类的东西
 * 和注册分开预防后期业务更改
 */
public class ForgotPasswordActivity extends AppBaseActivity implements View.OnClickListener, ForgotPasswordContract.View {

    private EditTextPro mInputPhone;
    private EditTextPro mInputVerify;
    private EditTextPro mInputPassword;
    private EditTextPro mInputPasswordEnsure;

    private VerifyCodeButton mVerifyCodeButton;

    private ForgotPasswordContract.Presenter mPresenter;

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        mPresenter = new ForgotPasswordPresenter(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        TopBar topBar = (TopBar) findViewById(R.id.top_bar);
        topBar.setOnTopBarListener(this);

        mInputPhone = (EditTextPro) findViewById(R.id.phone);
        mInputVerify = (EditTextPro) findViewById(R.id.verify_code);
        mInputPassword = (EditTextPro) findViewById(R.id.password);
        mInputPasswordEnsure = (EditTextPro) findViewById(R.id.ensure_password);

        mVerifyCodeButton = (VerifyCodeButton) findViewById(R.id.send_verify_code);
        mVerifyCodeButton.setOnClickListener(this);

        View register = findViewById(R.id.submit);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_verify_code:
                mVerifyCodeButton.showLoading();
                mPresenter.sendVerifiyCode();
                break;
            case R.id.submit:
                String phone = mInputPhone.getInputText();
                String verifyCode = mInputVerify.getInputText();
                String password = mInputPassword.getInputText();
                String passwordEnsure = mInputPasswordEnsure.getInputText();
                mPresenter.resetPassword(this, phone, verifyCode, password, passwordEnsure);
                break;
        }
    }


    @Override
    public void showSendVerifySuccess() {
        mVerifyCodeButton.startTiming();
    }

    @Override
    public void showSendVerifyFail(String message) {
        mVerifyCodeButton.reset();
        dialog(message);
    }

    @Override
    public void showLoading(String message, boolean dialog) {
        mLoadingDialog = LoadingDialog.show(this, mLoadingDialog);
    }

    @Override
    public void dismissLoading(boolean dialog) {
        LoadingDialog.dismiss(mLoadingDialog);
    }

    @Override
    public void showError(String message, boolean dialog, int errorType) {
        dialog(message);
    }

    @Override
    public void showSuccess(String message) {

    }
}
