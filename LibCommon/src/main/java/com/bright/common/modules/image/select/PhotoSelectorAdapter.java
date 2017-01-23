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

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bright.common.R;
import com.bright.common.adapter.RecyclerAdapter;
import com.bright.common.entries.Photo;
import com.bumptech.glide.Glide;

/**
 * Created by Bright.Yu on 2017/1/23.
 * 图片选择期的Adapter
 */

class PhotoSelectorAdapter extends RecyclerAdapter<Photo, PhotoSelectorAdapter.ViewHolder> {

    PhotoSelectorAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_photo_selector, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        // Photo entry = getItem(position);

        Glide.with(mContext)
                .load("http://pic18.nipic.com/20120113/9262696_160127381000_2.jpg")
                .crossFade()
                .centerCrop()
                .into(holder.photo);
    }

    class ViewHolder extends RecyclerAdapter.Holder implements View.OnClickListener {
        /**
         * 图片
         */
        ImageView photo;
        /**
         * 图片选中状态
         */
        CheckBox status;
        /**
         * 因为CheckBox不好点击所以增加的点击层
         */
        View statusContainer;
        /**
         * 遮罩层
         */
        View mask;

        ViewHolder(View item, int type) {
            super(item, type);
            photo = (ImageView) item.findViewById(R.id.photo);
            status = (CheckBox) item.findViewById(R.id.state);
            statusContainer = item.findViewById(R.id.status_container);
            mask = item.findViewById(R.id.mask);
        }

        @Override
        public void onClick(View v) {
            status.setChecked(!status.isChecked());
        }
    }
}
