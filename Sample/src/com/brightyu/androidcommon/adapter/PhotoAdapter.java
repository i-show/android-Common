/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brightyu.androidcommon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.bright.common.adapter.ListAdapter;
import com.bright.common.widget.dialog.ShowPhotoDialog;
import com.brightyu.androidcommon.R;
import com.bumptech.glide.Glide;


/**
 * 里面图片的Adapter
 */
public class PhotoAdapter extends ListAdapter<String, PhotoAdapter.ViewHolder> {
    FrameLayout.LayoutParams mLayoutParams;

    public PhotoAdapter(GridView gridView, Context context, int width, int spacing, int padding, int numColumns) {
        super(context);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        int itemSize = (width - /**padding**/2 * padding - (numColumns - 1) * spacing) / numColumns;
        mLayoutParams = new FrameLayout.LayoutParams(itemSize, itemSize);
        gridView.getLayoutParams().width = width;
        gridView.setPadding(padding, padding, padding, padding);
        gridView.setHorizontalSpacing(spacing);
        gridView.setVerticalSpacing(spacing);
        gridView.setAdapter(this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_grid_list_photo, null);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        Glide.with(mContext)
                .load(getItem(position))
                .placeholder(R.drawable.no_picture)
                .override(mLayoutParams.width, mLayoutParams.height)
                .into(holder.photo);
        holder.photo.setTag(R.id.tag_01, position);
    }

    public class ViewHolder extends ListAdapter.Holder implements View.OnClickListener {
        ImageView photo;

        public ViewHolder(View item, int type) {
            super(item, type);
            photo = (ImageView) item.findViewById(R.id.photo);
            photo.setLayoutParams(mLayoutParams);
            photo.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_01);
            ShowPhotoDialog dialog = new ShowPhotoDialog(mContext);
            dialog.setBeforeView(v);
            dialog.setData(getData());
            dialog.setCurrentPosition(position);
            dialog.show();
        }
    }
}