/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noahark.modules.account.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.ishow.common.utils.StringUtils;
import com.ishow.common.utils.router.AppRouter;
import com.ishow.common.widget.edittext.EditTextPro;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.account.password.forgot.ForgotPasswordActivity;
import com.ishow.noahark.modules.account.register.RegisterActivity;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.main.MainActivity;


/**
 * 登录界面
 */
public class LoginActivity extends AppBaseActivity implements LoginContract.View, View.OnClickListener {
    /**
     * 是不是只关闭
     */
    public static final String KEY_ONLY_FINISH = "key_login_only_finish";
    /**
     * 是不是去首页
     */
    public static final String KEY_GOTO_MAIN = "key_go_to_main";

    private LoginContract.Presenter mPresenter;
    private EditTextPro mEditAccount;
    private EditTextPro mEditPassword;

    private boolean isOnlyFinish;
    private boolean isGoToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter(this);
        mPresenter.start(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isOnlyFinish = intent.getBooleanExtra(KEY_ONLY_FINISH, false);
        isGoToMain = intent.getBooleanExtra(KEY_GOTO_MAIN, false);
    }

    @Override
    protected void initNecessaryData() {
        super.initNecessaryData();
        isOnlyFinish = getIntent().getBooleanExtra(KEY_ONLY_FINISH, false);
        isGoToMain = getIntent().getBooleanExtra(KEY_GOTO_MAIN, false);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mEditAccount = findViewById(R.id.account);
        mEditAccount.addInputWatcher(mTextWatcher);
        mEditPassword = findViewById(R.id.password);

        View login = findViewById(R.id.login);
        login.setOnClickListener(this);

        View register = findViewById(R.id.regist);
        register.setOnClickListener(this);

        View forgotPassword = findViewById(R.id.forget_password);
        forgotPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                String account = mEditAccount.getInputText();
                String name = mEditPassword.getInputText();
                mPresenter.login(this, account, name);
                break;
            case R.id.regist:
                AppRouter.with(this)
                        .target(RegisterActivity.class)
                        .start();
                break;
            case R.id.forget_password:
                AppRouter.with(this)
                        .target(ForgotPasswordActivity.class)
                        .start();
                break;
        }
    }


    @Override
    public void updateUI(String account) {
        mEditAccount.setInputText(account);
    }


    @Override
    public void showSuccess(String message) {
        if (!isOnlyFinish) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEditAccount.removeInputWatcher(mTextWatcher);
    }

    @Override
    public void onBackPressed() {
        if (isGoToMain) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mEditPassword.setInputText(StringUtils.EMPTY);
        }
    };
}
