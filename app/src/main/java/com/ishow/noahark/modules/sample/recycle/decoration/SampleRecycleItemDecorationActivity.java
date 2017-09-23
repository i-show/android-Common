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

package com.ishow.noahark.modules.sample.recycle.decoration;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ishow.common.widget.recyclerview.itemdecoration.LinearSpacingItemDecoration;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.base.AppBaseActivity;
import com.ishow.noahark.modules.sample.Test;

/**
 * Created by yuhaiyang on 2017/6/2.
 * Sample ItemDecoration
 */

public class SampleRecycleItemDecorationActivity extends AppBaseActivity {
    private ItemDecorationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_recycle_item_decoration);
        mAdapter.setData(Test.getPhotosList(50));
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAdapter = new ItemDecorationAdapter(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new LinearSpacingItemDecoration(this, R.dimen.gap_grade_2));
        list.setAdapter(mAdapter);
    }
}
