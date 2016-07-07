/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

package com.bright.common;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bright.common.widget.TopBar;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;

public abstract class BaseFragment extends Fragment implements TopBar.OnTopBarListener {
    /**
     * 保存变量
     */
    private SharedPreferences mSharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //************************ 数据保存区域*********************** //

    /**
     * 获取保存的变量
     */
    protected SharedPreferences getSharedPreferences() {
        if (mSharedPreferences == null && isAdded()) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }
        return mSharedPreferences;
    }

    /**
     * 保存 int 类型数据
     */
    protected void saveInt(String key, int values) {
        if (getSharedPreferences() != null) {
            mSharedPreferences.edit().putInt(key, values).commit();
        }
    }

    /**
     * 保存 Boolean 类型数据
     */
    protected void saveBoolean(String key, boolean values) {
        if (getSharedPreferences() != null) {
            mSharedPreferences.edit().putBoolean(key, values).commit();
        }
    }

    /**
     * 保存String 类型数据
     */
    protected void saveString(String key, String values) {
        if (getSharedPreferences() != null) {
            mSharedPreferences.edit().putString(key, values).commit();
        }
    }

    /**
     * 保存long型数据
     */
    protected void saveLong(String key, long values) {
        if (getSharedPreferences() != null) {
            mSharedPreferences.edit().putLong(key, values).commit();
        }
    }


    /**
     * TopBar的左侧点击事件
     */
    @Override
    public void onLeftClick(View v) {
        //TODO
    }

    /**
     * TopBar的右侧点击事件
     */
    @Override
    public void onRightClick(View v) {
        //TODO
    }

    /**
     * TopBar的中间Title点击事件
     */
    @Override
    public void onTitleClick(View v) {
        //TODO
    }


    // ******************** 提示区域 ***************************//

    /**
     * 提示 Toast简易封装操作
     */
    public void toast(String toast) {
        if (isAdded()) {
            YToast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 提示 Toast简易封装操作
     */
    public void toast(int toast) {
        if (isAdded()) {
            YToast.makeText(getActivity(), toast, Toast.LENGTH_SHORT).show();
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
     * @param finishSelf 是否关闭acitivity
     */
    protected Dialog dialog(@StringRes int message, boolean finishSelf) {
        return dialog(message, finishSelf, false);
    }

    /**
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭acitivity
     */
    protected Dialog dialog(@StringRes int title, @StringRes int message, boolean finishSelf) {
        return dialog(title, message, finishSelf, false);
    }

    /**
     * @param message    显示的内容
     * @param finishSelf 是否关闭acitivity
     * @param cancelable 是否可以点击取消
     */
    protected Dialog dialog(@StringRes int message, final boolean finishSelf, boolean cancelable) {
        return dialog(0, message, finishSelf, cancelable);
    }


    /**
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭acitivity
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
     * @param finishSelf 是否关闭acitivity
     */
    protected Dialog dialog(String message, boolean finishSelf) {
        return dialog(message, finishSelf, false);
    }


    /**
     * 显示Dialog
     *
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭acitivity
     */
    protected Dialog dialog(String title, String message, boolean finishSelf) {
        return dialog(title, message, finishSelf, false);
    }

    /**
     * @param message    显示的内容
     * @param finishSelf 是否关闭acitivity
     * @param cancelable 是否可以点击取消
     */
    protected Dialog dialog(String message, final boolean finishSelf, boolean cancelable) {
        return dialog(null, message, finishSelf, cancelable);
    }


    /**
     * @param title      标题
     * @param message    显示的内容
     * @param finishSelf 是否关闭acitivity
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
                if (finishSelf) {
                    getActivity().finish();
                }
            }
        });
        bulider.setCancelable(cancelable);

        BaseDialog dialog = bulider.create();
        dialog.show();
        return dialog;
    }
}
