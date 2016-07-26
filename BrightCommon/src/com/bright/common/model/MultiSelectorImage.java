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

package com.bright.common.model;

/**
 * 图片实体
 */
public class MultiSelectorImage {
    public String path;
    public String name;
    public long time;

    public MultiSelectorImage(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            MultiSelectorImage other = (MultiSelectorImage) o;
            return this.path.equalsIgnoreCase(other.path);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return super.equals(o);
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
         * 是否显示相机，boolean类型
         */
        public static final String EXTRA_SHOW_CAMERA = "show_camera";
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
        public static final int DEAFULT_MAX_COUNT = 9;
    }
}
