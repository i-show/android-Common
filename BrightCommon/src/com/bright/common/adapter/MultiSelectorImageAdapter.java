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

package com.bright.common.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.model.MultiSelectorImage;
import com.bright.common.widget.YToast;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 */
public class MultiSelectorImageAdapter extends ListAdapter<MultiSelectorImage, MultiSelectorImageAdapter.ViewHolde> {
    private static final String TAG = "MultiImageAdapter";
    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private boolean isShowCamera = true;
    private boolean isMultiSelector = true;
    private int mItemSize;
    private int mMaxSelectedCount;

    private List<MultiSelectorImage> mSelectedImages = new ArrayList<>();
    private GridView.LayoutParams mItemLayoutParams;
    private CallBack mCallBack;

    public MultiSelectorImageAdapter(Context context, boolean showCamera) {
        super(context);
        isShowCamera = showCamera;
        mItemLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    /**
     * 显示选择指示器
     */
    public void setMultiSelector(boolean show) {
        isMultiSelector = show;
    }

    public void setMaxSelectedCount(int count) {
        mMaxSelectedCount = count;
    }

    public void setShowCamera(boolean show) {
        if (isShowCamera == show) {
            return;
        }
        isShowCamera = show;
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
        for (MultiSelectorImage image : getData()) {
            if (image.path.equalsIgnoreCase(path)) {
                return image;
            }
        }
        return null;
    }

    /**
     * 设置数据集
     */
    public void setData(List<MultiSelectorImage> images) {
        super.setData(images);
        mSelectedImages.clear();
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
        holder.indicator.setVisibility(isMultiSelector ? View.VISIBLE : View.GONE);
        holder.indicator.setSelected(entry.isSelected);
        holder.mask.setVisibility(entry.isSelected ? View.VISIBLE : View.GONE);

        holder.getItemView().setTag(R.id.tag_01, entry);
        holder.getItemView().setTag(R.id.tag_02, position);

        // 显示图片
        Glide.with(mContext)
                .load(entry.path)
                .placeholder(R.drawable.no_picture)
                .crossFade()
                .centerCrop()
                .into(holder.image);
    }

    public class ViewHolde extends ListAdapter.Holder implements View.OnClickListener {
        ImageView image;
        ImageView indicator;
        View mask;

        public ViewHolde(View item, int type) {
            super(item, type);
            item.setLayoutParams(mItemLayoutParams);
            item.setOnClickListener(this);

            image = (ImageView) item.findViewById(R.id.image);
            indicator = (ImageView) item.findViewById(R.id.checkmark);
            mask = item.findViewById(R.id.mask);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag(R.id.tag_02);
            if (getItemViewType(position) == TYPE_HEADER) {
                Log.i(TAG, "onClick: is header todo go camera ");
                if (mCallBack != null) {
                    mCallBack.onClickCamera();
                }
                return;
            }

            MultiSelectorImage entry = (MultiSelectorImage) v.getTag(R.id.tag_01);
            if (!entry.isSelected && mMaxSelectedCount <= mSelectedImages.size()) {
                YToast.makeText(mContext, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                return;
            }

            entry.isSelected = !entry.isSelected;

            if (entry.isSelected) {
                mSelectedImages.add(entry);
            } else {
                mSelectedImages.remove(entry);
            }

            if (mCallBack != null) {
                mCallBack.onSelectImage(entry);
            }

            indicator.setVisibility(isMultiSelector ? View.VISIBLE : View.GONE);
            indicator.setSelected(entry.isSelected);
            mask.setVisibility(entry.isSelected ? View.VISIBLE : View.GONE);
        }

    }

    public interface CallBack {
        void onClickCamera();

        void onSelectImage(MultiSelectorImage entry);
    }

}
