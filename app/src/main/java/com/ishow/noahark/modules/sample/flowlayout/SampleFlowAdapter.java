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

package com.ishow.noahark.modules.sample.flowlayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.widget.flowlayout.FlowAdapter;

/**
 * Created by yuhaiyang on 2017/8/9.
 * 测试额Adapter
 */

class SampleFlowAdapter extends FlowAdapter<String, SampleFlowAdapter.ViewHolder> {


    public SampleFlowAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {

    }

    class ViewHolder extends FlowAdapter.Holder {

        public ViewHolder(View item, int type) {
            super(item, type);
        }
    }
}
