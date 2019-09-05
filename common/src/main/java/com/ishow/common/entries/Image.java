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

package com.ishow.common.entries;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.BindingMethod;

import com.ishow.common.R;

import java.util.Objects;

/**
 * 图片实体
 */
public class Image {
    public long id;
    /**
     * 图片的绝对路径
     */
    public String path;
    /**
     * 图片的名称
     */
    public String name;
    /**
     * 包含本图片的名字
     */
    public String folderName;
    /**
     * 修改时间
     */
    public long modifyDate;

    public boolean isSelected;
    public boolean isUnSelected;


    public Image(long id, String path, String name, long modifyDate, String folderName) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.modifyDate = modifyDate;
        this.folderName = folderName;
    }

    public String getPath() {
        return path;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image photo = (Image) o;
        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @NonNull
    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", folderName='" + folderName + '\'' +
                ", modifyDate=" + modifyDate +
                ", isSelected=" + isSelected +
                ", isUnSelected=" + isUnSelected +
                '}';
    }

    public static final class Key {
        /**
         * 最大图片选择次数，int类型
         */
        public static final String EXTRA_SELECT_COUNT = "max_select_count";
        /**
         * 图片选择模式，int类型
         */
        public static final String EXTRA_SELECT_MODE = "select_count_mode";
        /**
         * 默认选择的数据集
         */
        public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
        /**
         * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
         */
        public static final String EXTRA_RESULT = "select_result";

        /**
         * 单选
         */
        public static final int MODE_SINGLE = 0;
        /**
         * 多选
         */
        public static final int MODE_MULTI = 1;
        /**
         * 默认最大选择数量
         */
        public static final int DEFAULT_MAX_COUNT = 9;
    }
}
