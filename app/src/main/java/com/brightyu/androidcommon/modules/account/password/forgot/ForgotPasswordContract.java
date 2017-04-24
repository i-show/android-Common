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


import android.content.Context;

import com.brightyu.androidcommon.modules.base.mvp.BasePresenter;
import com.brightyu.androidcommon.modules.base.mvp.BaseView;

/**
 * This specifies the contract between the view and the presenter.
 */
interface ForgotPasswordContract {

    interface View extends BaseView {

        void showSendVerifySuccess();

        void showSendVerifyFail(String message);

    }

    abstract class Presenter extends BasePresenter<View> {
        Presenter(View view) {
            super(view);
        }

        /**
         * 重置密码
         */
        abstract void resetPassword(Context context, String name, String verifyCode, String password, String ensurePassword);

        /**
         * 发送验证码
         */
        abstract void sendVerifiyCode();
    }
}
