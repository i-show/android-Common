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

package com.ishow.common.app.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import com.ishow.common.R;
import com.ishow.common.mvp.base.IViewStatus;
import com.ishow.common.utils.ToastUtils;
import com.ishow.common.utils.http.rest.Http;
import com.ishow.common.utils.permission.PermissionManager;
import com.ishow.common.widget.StatusView;
import com.ishow.common.widget.TopBar;
import com.ishow.common.widget.dialog.BaseDialog;
import com.ishow.common.widget.loading.LoadingDialog;

public abstract class BaseFragment extends Fragment implements
        StatusView.OnStatusViewListener,
        IViewStatus,
        TopBar.OnTopBarListener {

    /**
     * Loading的Dialog
     */
    protected LoadingDialog mLoadingDialog;
    /**
     * 状态的View
     */
    protected StatusView mStatusView;
    /**
     * 用来回收的Handler
     */
    protected Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Http.cancel(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    //************************ 重写 各种事件区域*********************** //

    /**
     * TopBar的左侧点击事件
     */
    @Override
    public void onLeftClick(View v) {
    }

    /**
     * TopBar的右侧点击事件
     */
    @Override
    public void onRightClick(View v) {
    }

    /**
     * TopBar的中间Title点击事件
     */
    @Override
    public void onTitleClick(View v) {
    }


    // ******************** 提示区域 ***************************//

    /**
     * 提示 Toast简易封装操作
     */
    @SuppressWarnings("unused")
    public void toast(String toast) {
        if (isAdded()) {
            ToastUtils.show(getActivity(), toast);
        }
    }

    /**
     * 提示 Toast简易封装操作
     */
    @SuppressWarnings("unused")
    public void toast(int toast) {
        if (isAdded()) {
            ToastUtils.show(getActivity(), toast);
        }
    }

    /**
     * 显示Dialog
     *
     * @param message 显示的内容
     */
    protected Dialog dialog(@StringRes int message) {
        return dialog(message, false, false);
    }

    /**
     * 显示Dialog
     *
     * @param message 显示的内容
     */
    protected Dialog dialog(@StringRes int title, @StringRes int message) {
        return dialog(title, message, false, false);
    }

    /**
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     */
    protected Dialog dialog(@StringRes int message, boolean finishSelf) {
        return dialog(message, finishSelf, false);
    }

    /**
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     */
    protected Dialog dialog(@StringRes int title, @StringRes int message, boolean finishSelf) {
        return dialog(title, message, finishSelf, false);
    }

    /**
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     * @param cancelable 是否可以点击取消
     */
    protected Dialog dialog(@StringRes int message, final boolean finishSelf, boolean cancelable) {
        return dialog(0, message, finishSelf, cancelable);
    }


    /**
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     * @param cancelable 是否可以点击取消
     */
    protected Dialog dialog(@StringRes int title, @StringRes int message, final boolean finishSelf, boolean cancelable) {

        String titleString;
        try {
            titleString = getText(title).toString();
        } catch (Resources.NotFoundException e) {
            titleString = null;
        }

        String messageString;
        try {
            messageString = getText(message).toString();
        } catch (Resources.NotFoundException e) {
            messageString = null;
        }
        return dialog(titleString, messageString, finishSelf, cancelable);
    }

    /**
     * 显示Dialog
     *
     * @param message 显示的内容
     */
    protected Dialog dialog(String message) {
        return dialog(message, false, false);
    }

    /**
     * 显示Dialog
     *
     * @param title   标题
     * @param message 显示的内容
     */
    protected Dialog dialog(String title, String message) {
        return dialog(title, message, false, false);
    }

    /**
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     */
    protected Dialog dialog(String message, boolean finishSelf) {
        return dialog(message, finishSelf, false);
    }


    /**
     * 显示Dialog
     *
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     */
    protected Dialog dialog(String title, String message, boolean finishSelf) {
        return dialog(title, message, finishSelf, false);
    }

    /**
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     * @param cancelable 是否可以点击取消
     */
    protected Dialog dialog(String message, final boolean finishSelf, boolean cancelable) {
        return dialog(null, message, finishSelf, cancelable);
    }


    /**
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭activity
     * @param cancelable 是否可以点击取消
     */
    protected Dialog dialog(String title, String message, final boolean finishSelf, boolean cancelable) {
        BaseDialog.Builder bulider = new BaseDialog.Builder(getActivity());
        // 设置标题
        if (!TextUtils.isEmpty(title)) {
            bulider.setTitle(title);
        }
        // 设置message
        bulider.setMessage(message);
        bulider.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (finishSelf && getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        bulider.setCancelable(cancelable);

        BaseDialog dialog = bulider.create();
        dialog.show();
        return dialog;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }


    @Override
    public void showLoading(String message, boolean dialog) {
        if (dialog) {
            mLoadingDialog = LoadingDialog.show(getActivity(), mLoadingDialog);
        } else if (mStatusView != null) {
            mStatusView.showLoading();
        }
    }

    @Override
    public void dismissLoading(boolean dialog) {
        if (dialog) {
            LoadingDialog.dismiss(mLoadingDialog);
        } else if (mStatusView != null) {
            mStatusView.dismiss();
        }
    }

    @Override
    public void showError(String message, boolean dialog, int errorType) {
        if (dialog) {
            dialog(message);
        } else if (mStatusView != null) {
            mStatusView.showError();
        }
    }

    @Override
    public void showError(int message, boolean dialog, int errorType) {
        if (dialog) {
            dialog(message);
        } else if (mStatusView != null) {
            mStatusView.showError();
        }
    }

    @Override
    public void showSuccess(String message) {

    }

    @Override
    public void showSuccess(int message) {

    }

    @Override
    public void showEmpty(String message) {
        if (mStatusView != null) {
            mStatusView.showEmpty();
        }
    }

    @Override
    public void showEmpty(int message) {
        if (mStatusView != null) {
            mStatusView.showEmpty();
        }
    }

    @Override
    public void onStatusClick(View v, StatusView.Which which) {

    }
}
