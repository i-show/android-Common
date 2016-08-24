/**
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

package com.brightyu.androidcommon.entries;

import com.alibaba.fastjson.annotation.JSONField;
import com.bright.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bright.Yu on 2016/7/15.
 */
public class TextJson<T> {

    public String name;
    public List<T> list;

    public String getName() {
        return name;
    }

    @JSONField(name = "username")
    public void setName(String name) {
        this.name = name;
    }

    public void addData(T t) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(t);
    }

    @JSONField(serialize = false)
    public String getFormatName() {
        return StringUtils.plusString(name, name);
    }
}
