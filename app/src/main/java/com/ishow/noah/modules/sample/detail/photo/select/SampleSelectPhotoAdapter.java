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

package com.ishow.noah.modules.sample.detail.photo.select;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ishow.common.adapter.RecyclerAdapter;
import com.ishow.common.modules.image.show.ShowPhotoDialog;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.noah.R;

/**
 * Created by Bright.Yu on 2017/1/16.
 * 用来显示图片的Adapter
 */

class SampleSelectPhotoAdapter extends RecyclerAdapter<String, SampleSelectPhotoAdapter.ViewHolder> {

    SampleSelectPhotoAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View item = getMLayoutInflater().inflate(R.layout.item_sample_select_photo, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        String path = getItem(position);
        holder.photo.setTag(path);
       
        ImageLoader.with(getMContext())
                .load(path)
                .placeholder(R.drawable.no_picture)
                .mode(ImageLoader.LoaderMode.CENTER_CROP)
                .into(holder.photo);

    }

    class ViewHolder extends RecyclerAdapter.Holder implements View.OnClickListener {
        ImageView photo;

        ViewHolder(View item, int type) {
            super(item, type);
            photo = (ImageView) item.findViewById(R.id.photo);
            photo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String path = (String) v.getTag();
            ShowPhotoDialog dialog = new ShowPhotoDialog(getMContext());
            dialog.setData(path);
            dialog.setBeforeView(v);
            dialog.show();
        }
    }
}
