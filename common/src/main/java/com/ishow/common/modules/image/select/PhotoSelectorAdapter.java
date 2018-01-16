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

package com.ishow.common.modules.image.select;

import android.content.Context;
import android.support.annotation.IntRange;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.ishow.common.R;
import com.ishow.common.adapter.RecyclerAdapter;
import com.ishow.common.entries.Photo;
import com.ishow.common.modules.image.show.ShowPhotoDialog;
import com.ishow.common.utils.ToastUtils;
import com.ishow.common.utils.image.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bright.Yu on 2017/1/23.
 * 图片选择期的Adapter
 */

class PhotoSelectorAdapter extends RecyclerAdapter<Photo, PhotoSelectorAdapter.ViewHolder> {

    private List<Photo> mSelectedPhotos;
    private OnSelectedChangedListener mSelectedChangedListener;
    private int mMaxCount;

    PhotoSelectorAdapter(Context context) {
        super(context);
        mSelectedPhotos = new ArrayList<>();
    }

    /**
     * 获取 选中照片
     */
    List<Photo> getSelectedPhotos() {
        return mSelectedPhotos;
    }

    void setMaxCount(@IntRange(from = 1) int maxCount) {
        mMaxCount = maxCount;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_photo_selector, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        Photo entry = getItem(position);

        ImageLoader.with(mContext)
                .load(entry.path)
                .mode(ImageLoader.LoaderMode.CENTER_CROP)
                .into(holder.photo);

        holder.getItemView().setTag(entry);
        holder.statusContainer.setTag(entry);
        holder.status.setChecked(entry.isSelected);
        holder.mask.setVisibility(entry.isSelected ? View.VISIBLE : View.GONE);
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
            item.setOnClickListener(this);
            photo = (ImageView) item.findViewById(R.id.photo);
            mask = item.findViewById(R.id.mask);

            status = (CheckBox) item.findViewById(R.id.status);
            statusContainer = item.findViewById(R.id.status_container);
            statusContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Photo entry = (Photo) v.getTag();
            if (v.getId() == R.id.status_container) {
                selectPhoto(entry);
            } else {
                showBigPhoto(v, entry);
            }
        }

        /**
         * 选择照片
         */
        private void selectPhoto(Photo entry) {
            int alreadyCount = mSelectedPhotos.size();
            // 如果当前数量已经是最大数量，当前图片还没有选中
            if (alreadyCount >= mMaxCount && !entry.isSelected) {
                String tip = mContext.getString(R.string.already_select_max, mMaxCount);
                ToastUtils.show(mContext, tip);
                return;
            }

            entry.isSelected = !entry.isSelected;
            status.setChecked(entry.isSelected);
            mask.setVisibility(entry.isSelected ? View.VISIBLE : View.GONE);
            if (entry.isSelected) {
                mSelectedPhotos.add(entry);
            } else {
                mSelectedPhotos.remove(entry);
            }
            notifySelectedChanged();
        }

        /**
         * 看大图
         */
        private void showBigPhoto(View v, Photo entry) {
            ShowPhotoDialog dialog = new ShowPhotoDialog(mContext);
            dialog.setData(entry.path);
            dialog.setBeforeView(v);
            dialog.show();
        }
    }


    /**
     * 选中状态修改的Listener
     */
    interface OnSelectedChangedListener {
        void onSelectedChanged(int selectCount);
    }

    public void setSelectedChangedListener(OnSelectedChangedListener listener) {
        mSelectedChangedListener = listener;
    }

    private void notifySelectedChanged() {
        if (mSelectedChangedListener != null) {
            mSelectedChangedListener.onSelectedChanged(mSelectedPhotos.size());
        }
    }
}
