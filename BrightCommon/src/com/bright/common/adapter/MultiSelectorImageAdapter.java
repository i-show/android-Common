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

package com.bright.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.bright.common.R;
import com.bright.common.model.MultiSelectorImage;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 */
public class MultiSelectorImageAdapter extends ListAdapter<MultiSelectorImage, MultiSelectorImageAdapter.ViewHolde> {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private boolean isShowCamera = true;
    private boolean isShowSelectIndicator = true;

    private List<MultiSelectorImage> mImages = new ArrayList<>();
    private List<MultiSelectorImage> mSelectedImages = new ArrayList<>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;

    public MultiSelectorImageAdapter(Context context, boolean showCamera) {
        super(context);
        isShowCamera = showCamera;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }


    /**
     * 显示选择指示器
     */
    public void showSelectIndicator(boolean show) {
        isShowSelectIndicator = show;
    }

    public void setShowCamera(boolean show) {
        if (isShowCamera == show) {
            return;
        }
        isShowCamera = show;
        notifyDataSetChanged();
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     */
    public void select(MultiSelectorImage image) {
        if (mSelectedImages.contains(image)) {
            mSelectedImages.remove(image);
        } else {
            mSelectedImages.add(image);
        }
        notifyDataSetChanged();
    }

    /**
     * 通过图片路径设置默认选择
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for (String path : resultList) {
            MultiSelectorImage image = getImageByPath(path);
            if (image != null) {
                mSelectedImages.add(image);
            }
        }
        if (mSelectedImages.size() > 0) {
            notifyDataSetChanged();
        }
    }

    private MultiSelectorImage getImageByPath(String path) {
        if (mImages != null && mImages.size() > 0) {
            for (MultiSelectorImage image : mImages) {
                if (image.path.equalsIgnoreCase(path)) {
                    return image;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     */
    public void setData(List<MultiSelectorImage> images) {
        mSelectedImages.clear();

        if (images != null && images.size() > 0) {
            mImages = images;
        } else {
            mImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     */
    public void setItemSize(int columnWidth) {

        if (mItemSize == columnWidth) {
            return;
        }
        mItemSize = columnWidth;
        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);
        notifyDataSetChanged();
    }

    @Override
    public int getHeaderCount() {
        return isShowCamera ? 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowCamera && position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_NORMAL;
        }
    }


    @Override
    public ViewHolde onCreateViewHolder(int position, int type) {
        View view;
        if (type == TYPE_CAMERA) {
            view = mLayoutInflater.inflate(R.layout.item_multi_select_camera, null);
        } else {
            view = mLayoutInflater.inflate(R.layout.item_multi_select_image, null);
        }
        return new ViewHolde(view, type);
    }

    @Override
    public void onBindViewHolder(ViewHolde holder, int position, int type) {
        MultiSelectorImage entry = getRealItem(position);
        // 处理单选和多选状态
        if (isShowSelectIndicator) {
            holder.indicator.setVisibility(View.VISIBLE);
            if (mSelectedImages.contains(entry)) {
                // 设置选中状态
                holder.indicator.setImageResource(R.drawable.ic_multi_selected);
                holder.mask.setVisibility(View.VISIBLE);
            } else {
                // 未选择
                holder.indicator.setImageResource(R.drawable.ic_multi_unselected);
                holder.mask.setVisibility(View.GONE);
            }
        } else {
            holder.indicator.setVisibility(View.GONE);
        }

        // 显示图片
        Glide.with(mContext)
                .load(entry.path)
                .placeholder(R.drawable.no_picture)
                .crossFade()
                .centerCrop()
                .into(holder.image);
    }

    public class ViewHolde extends ListAdapter.Holder {
        ImageView image;
        ImageView indicator;
        View mask;

        public ViewHolde(View item, int type) {
            super(item, type);
            item.setLayoutParams(mItemLayoutParams);

            image = (ImageView) item.findViewById(R.id.image);
            indicator = (ImageView) item.findViewById(R.id.checkmark);
            mask = item.findViewById(R.id.mask);
        }

    }

}
