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
package com.ishow.common.utils.permission;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

@SuppressWarnings("all")
public interface Permission {


    /**
     * Here to fill in all of this to apply for permission, can be a, can be more.
     *
     * @param permissions one or more permissions.
     * @return {@link Permission}.
     */
    @NonNull
    Permission permission(String... permissions);

    /**
     * Request code.
     *
     * @param requestCode int, the first parameter in callback {@code onRequestPermissionsResult(int, String[],
     *                    int[])}}.
     * @return {@link Permission}.
     */
    @NonNull
    Permission requestCode(int requestCode);

    /**
     * 注解回调的设置
     *
     * @param obj @param obj  必须是已经实现了的对象,不能输入 A.class 这种类型,否则方法不可执行
     */
    Permission annotationClass(@NonNull Object obj);

    /**
     * 提示消息
     */
    Permission message(String message);

    /**
     * 提示消息
     */
    Permission message(@StringRes int message);

    /**
     * 提示方式 默认为Toast
     */
    Permission promptType(@PermissionPromptType int type);

    /**
     * Request permission.
     */
    void send();

}
