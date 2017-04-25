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

package com.brightyu.androidcommon.modules.sample.pickview;

import com.ishow.common.widget.pickview.adapter.PickerAdapter;

/**
 * Created by Bright.Yu on 2017/1/15.
 * Picker的Adapter
 */

public class SamplePickerAdapter extends PickerAdapter<String> {
    private final String city[] = {"山东", "北京", "青岛", "苏州", "上海"};

    @Override
    public int getCount() {
        return city.length;
    }

    @Override
    public String getItem(int position) {
        return city[position];
    }

    @Override
    public String getItemText(int position) {
        return city[position];
    }
}
