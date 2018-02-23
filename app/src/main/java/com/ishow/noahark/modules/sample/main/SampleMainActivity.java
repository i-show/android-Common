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

package com.ishow.noahark.modules.sample.main;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ishow.common.widget.recyclerview.AnimationRecyclerView;
import com.ishow.common.widget.recyclerview.layoutmanager.FlowLayoutManager;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 测试Demo
 */
public class SampleMainActivity extends AppBaseActivity {
    @BindView(R.id.list)
    AnimationRecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_main);
        ButterKnife.bind(this);

        SampleMainAdapter adapter = new SampleMainAdapter(this);
        adapter.setData(SampleManager.getSamples());

        mList.setLayoutManager(new FlowLayoutManager());
        mList.setAdapter(adapter);
    }

    @Override
    protected void initViews() {
        super.initViews();



    }
}
