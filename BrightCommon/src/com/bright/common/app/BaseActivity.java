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

package com.bright.common.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.utils.SharedPreferencesUtils;
import com.bright.common.utils.http.okhttp.OkHttpUtils;
import com.bright.common.widget.TopBar;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.BaseDialog;


public abstract class BaseActivity extends AppCompatActivity implements TopBar.OnTopBarListener {
    /**
     * Activity的TYPE
     */
    public static final String KEY_TYPE = "activity_type";
    /**
     * 默认返回的result
     */
    public static final String KEY_RESULT = "activity_result";
    protected Handler mHandler;
    /**
     * 保存变量
     */
    private SharedPreferences mSharedPreferences;

    //************************ 生命周期 区域*********************** //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetStatusBar();
    }

    /**
     * 用来设置全屏样式
     */
    protected void resetStatusBar() {
        // TODO
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消请求接口
        OkHttpUtils.getInstance().cancelTag(this);
        // 清除Handler预防内存泄露
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    //************************ 初始化 区域*********************** //
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initNecessaryData();
        initViews();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initNecessaryData();
        initViews(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initNecessaryData();
        initViews(view, params);
    }

    protected void initViews() {
        //TODO
    }

    protected void initViews(View view) {
        //TODO
    }

    protected void initViews(View view, ViewGroup.LayoutParams params) {
        //TODO
    }

    /**
     * 有一些数据要在initViews之前处理的在这个方法中处理
     * <p>
     * 注意：尽量少用
     */
    protected void initNecessaryData() {
        //TODO
    }

    //************************ 数据保存区域*********************** //
    protected void save(String key, Object value) {
        save(key, value, false);
    }

    protected void save(String key, Object value, boolean isCache) {
        SharedPreferencesUtils.save(this, key, value, isCache);
    }

    protected <T> T get(String key, T defaultValue) {
        return get(key, defaultValue, false);
    }

    protected <T> T get(String key, T defaultValue, boolean isCache) {
        return SharedPreferencesUtils.get(this, key, defaultValue, isCache);
    }
    //************************ 重写 各种事件区域*********************** //

    /**
     * TopBar的左侧点击事件
     */
    @Override
    public void onLeftClick(View v) {
        onBackPressed();
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
    public void toast(String toast) {
        YToast.makeText(this, toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 提示 Toast简易封装操作
     */
    public void toast(int toast) {
        YToast.makeText(this, toast, Toast.LENGTH_SHORT).show();
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
        BaseDialog.Builder bulider = new BaseDialog.Builder(this);
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
                    BaseActivity.this.finish();
                }
            }
        });
        bulider.setCancelable(cancelable);

        BaseDialog dialog = bulider.create();
        dialog.show();
        return dialog;
    }
}
