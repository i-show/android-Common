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

package com.ishow.common.mvp.base;

import androidx.annotation.StringRes;

/**
 * Created by yuhaiyang on 2017/5/2.
 * View状态的更新
 */

public interface IViewStatus {
    /**
     * 显示加载动画
     */
    void showLoading(String message, boolean dialog);

    /**
     * 隐藏加载动画
     */
    void dismissLoading(boolean dialog);

    /**
     * 加载失败
     *
     * @param errorType 预防点击退出
     */
    void showError(String message, boolean dialog, int errorType);

    /**
     * 加载失败
     *
     * @param errorType 预防点击退出
     */
    void showError(@StringRes int message, boolean dialog, int errorType);

    /**
     * 加载成功
     */
    void showSuccess(String message);

    /**
     * 加载成功
     */
    void showSuccess(@StringRes int message);

    /**
     * 展示空数据
     */
    void showEmpty(String message);

    /**
     * 展示空数据
     */
    void showEmpty(@StringRes int message);
}
