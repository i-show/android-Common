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

import android.content.Context;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ishow.common.adapter.RecyclerAdapter;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.noah.R;

/**
 * Created by yuhaiyang on 2017/6/2.
 * 进入的时候带动画
 */

class AnimationRecycleAdapter extends RecyclerAdapter<String, AnimationRecycleAdapter.ViewHolder> {

    AnimationRecycleAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View item = getMLayoutInflater().inflate(R.layout.item_sample_only_photo, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        String path = getItem(position);
        ImageLoader.with(getMContext())
                .load(path)
                .mode(ImageLoader.LoaderMode.CENTER_CROP)
                .into(holder.photo);
    }

    class ViewHolder extends RecyclerAdapter.Holder {
        ImageView photo;

        ViewHolder(View item, int type) {
            super(item, type);
            photo = (ImageView) item.findViewById(R.id.photo);
        }
    }
}
