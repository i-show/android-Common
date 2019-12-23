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
package com.ishow.common.entries

import android.net.Uri
import java.util.*

/**
 * 图片实体
 */
class Image(
    var id: Long,
    /**
     * 图片的地址
     */
    var uri: Uri,
    /**
     * 图片的名称
     */
    var name: String,
    /**
     * 修改时间
     */
    var modifyDate: Long,
    /**
     * 包含本图片的名字
     */
    var folderName: String,
    /**
     * 图片的位置
     */
    var position: Int
) {
    /**
     * 是否选中
     */
    @JvmField
    var selected = false

    /**
     * 是否取消选中
     */
    var cancelSelected = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val photo = other as Image
        return id == photo.id
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    object Key {
        /**
         * 最大图片选择次数，int类型
         */
        const val EXTRA_SELECT_COUNT = "max_select_count"
        /**
         * 图片选择模式，int类型
         */
        const val EXTRA_SELECT_MODE = "select_count_mode"
        /**
         * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
         */
        const val EXTRA_RESULT = "select_result"
        /**
         * 单选
         */
        const val MODE_SINGLE = 0
        /**
         * 多选
         */
        const val MODE_MULTI = 1
        /**
         * 默认最大选择数量
         */
        const val DEFAULT_MAX_COUNT = 9

        const val POSITION = "key_position"
    }

}