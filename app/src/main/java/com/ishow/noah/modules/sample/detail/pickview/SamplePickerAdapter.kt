/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.sample.detail.pickview

import com.ishow.common.widget.pickview.adapter.PickerAdapter

/**
 * Created by Bright.Yu on 2017/1/15.
 * Picker的Adapter
 */

class SamplePickerAdapter : PickerAdapter<String>() {
    private val city = arrayOf("山东", "北京", "青岛", "苏州", "上海")

    override fun getCount(): Int {
        return city.size
    }

    override fun getItem(position: Int): String {
        return city[position]
    }

    override fun getItemText(position: Int): String {
        return city[position]
    }
}
