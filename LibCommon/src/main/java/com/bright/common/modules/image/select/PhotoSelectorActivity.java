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

package com.bright.common.modules.image.select;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bright.common.R;
import com.bright.common.app.activity.BaseActivity;
import com.bright.common.widget.recyclerview.itemdecoration.GridSpacingItemDecoration;

/**
 * Created by Bright.Yu on 2017/1/23.
 * 选择照片的Activity
 */

public class PhotoSelectorActivity extends BaseActivity {
    private PhotoSelectorAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selector);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mPhotoAdapter = new PhotoSelectorAdapter(this);
        RecyclerView photoList = (RecyclerView) findViewById(R.id.list);
        photoList.setLayoutManager(new GridLayoutManager(this, 3));
        photoList.addItemDecoration(new GridSpacingItemDecoration(this, R.dimen.photo_selector_item_gap, true));
        photoList.setAdapter(mPhotoAdapter);
    }
}
