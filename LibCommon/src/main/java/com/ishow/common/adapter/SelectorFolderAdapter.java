/*
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.common.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ishow.common.R;
import com.ishow.common.entries.Folder;
import com.bumptech.glide.Glide;


/**
 * 文件夹Adapter
 */
public class SelectorFolderAdapter extends ListAdapter<Folder, SelectorFolderAdapter.ViewHolder> {

    public SelectorFolderAdapter(Context context) {
        super(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View view = mLayoutInflater.inflate(R.layout.item_photo_selector_folder, null);
        return new ViewHolder(view, type);
    }

    @Override
    public void onBindViewHolder(ViewGroup parent, ViewHolder holder, int position, int type) {

        Folder entry = getItem(position);
        holder.name.setText(entry.name);
        holder.size.setText(mContext.getString(R.string.link_sheet, entry.photoList.size()));
        // 显示图片
        Glide.with(mContext)
                .load(entry.cover.path)
                .placeholder(R.drawable.no_picture)
                .centerCrop()
                .crossFade()
                .into(holder.cover);

        holder.state.setChecked(entry.isSelected);
    }


    public class ViewHolder extends ListAdapter.Holder {
        ImageView cover;
        TextView name;
        TextView size;
        RadioButton state;

        public ViewHolder(View item, int type) {
            super(item, type);
            cover = (ImageView) item.findViewById(R.id.cover);
            name = (TextView) item.findViewById(R.id.name);
            size = (TextView) item.findViewById(R.id.size);
            state = (RadioButton) item.findViewById(R.id.state);
        }
    }

}
