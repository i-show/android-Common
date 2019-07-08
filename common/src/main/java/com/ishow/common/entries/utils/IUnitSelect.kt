/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.common.entries.utils

import android.content.Context

/**
 * Created by Bright.Yu on 2016/12/29.
 * 选中的ISelect
 */

interface IUnitSelect {
    /**
     * 获取标题
     */
    fun getTitle(context: Context): String

    /**
     * 获取副标题
     */
    fun getSubTitle(context: Context): String?

    fun gravity(): Int
}
