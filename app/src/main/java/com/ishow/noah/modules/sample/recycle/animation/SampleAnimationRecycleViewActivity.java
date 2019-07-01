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

package com.ishow.noah.modules.sample.recycle.animation;

import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.AppBaseActivity;

/**
 * Created by yuhaiyang on 2017/6/2.
 * Sample 进入的带动画
 */

public class SampleAnimationRecycleViewActivity extends AppBaseActivity {
    private AnimationRecycleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_ani_recycle);
        //mAdapter.setData(Test.getPhotosList(50));
    }

    @Override
    protected void initViews() {
        super.initViews();
        mAdapter = new AnimationRecycleAdapter(this);

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(this, 2));
        list.setAdapter(mAdapter);
    }
}
