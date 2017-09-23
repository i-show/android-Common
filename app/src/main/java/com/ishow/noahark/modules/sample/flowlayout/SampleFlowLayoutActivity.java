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

import android.os.Bundle;

import com.ishow.common.widget.flowlayout.FlowLayout;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试Demo
 */
public class SampleFlowLayoutActivity extends AppBaseActivity {


    private SampleFlowAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_flow_layout);
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAdapter = new SampleFlowAdapter(this);
        FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow);
        flowLayout.setAdapter(mAdapter);
        mAdapter.setData(getData());
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("HELLO");
        data.add("WORLD");
        return data;
    }


}
