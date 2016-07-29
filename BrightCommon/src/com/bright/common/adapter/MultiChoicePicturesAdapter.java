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
import android.util.Log;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bright.common.R;
import com.bright.common.model.MultiSelectorImage;
import com.bright.common.widget.YToast;
import com.bright.common.widget.dialog.ShowPhotoDialog;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片Adapter
 * Created by Nereo on 2015/4/7.
 */
public class MultiChoicePicturesAdapter extends ListAdapter<MultiSelectorImage, MultiChoicePicturesAdapter.ViewHolde> {
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

    public MultiChoicePicturesAdapter(Context context, boolean showCamera) {
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


    /**
     * 设定已经选择了的照片
     */
    public void setSelectedImages(List<MultiSelectorImage> selectedImages) {
        if (selectedImages == null) {
            mSelectedImages = new ArrayList<>();
        } else {
            mSelectedImages = selectedImages;
        }
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
            view = mLayoutInflater.inflate(R.layout.item_multi_choice_camera, null);
        } else {
            view = mLayoutInflater.inflate(R.layout.item_multi_choice_image, null);
        }
        return new ViewHolde(view, type);
    }

    @Override
    public void onBindViewHolder(ViewHolde holder, int position, int type) {
        MultiSelectorImage entry = getRealItem(position);
        holder.getItemView().setTag(R.id.tag_01, entry);
        holder.getItemView().setTag(R.id.tag_02, position);

        holder.state.setTag(R.id.tag_01, entry);
        holder.state.setChecked(entry.isSelected);

        holder.mask.setVisibility(entry.isSelected ? View.VISIBLE : View.GONE);

        // 显示图片
        Glide.with(mContext)
                .load(entry.path)
                .crossFade()
                .centerCrop()
                .into(holder.image);
    }

    public class ViewHolde extends ListAdapter.Holder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView image;
        CheckBox state;
        View mask;

        public ViewHolde(View item, int type) {
            super(item, type);
            item.setLayoutParams(mItemLayoutParams);
            item.setOnClickListener(this);

            image = (ImageView) item.findViewById(R.id.image);

            state = (CheckBox) item.findViewById(R.id.state);
            state.setVisibility(isMultiSelector ? View.VISIBLE : View.GONE);
            state.setOnCheckedChangeListener(this);

            mask = item.findViewById(R.id.mask);
        }

        @Override
        public void onClick(View v) {
            onItemClick(v);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            selectImage(buttonView, isChecked);
        }

        private void selectImage(CompoundButton v, boolean isChecked) {
            MultiSelectorImage entry = (MultiSelectorImage) v.getTag(R.id.tag_01);

            if (entry.isSelected == isChecked) {
                Log.i(TAG, "selectImage: state is same just return");
                return;
            }

            if (isChecked && mMaxSelectedCount <= mSelectedImages.size()) {
                YToast.makeText(mContext, R.string.already_select_max, Toast.LENGTH_SHORT).show();
                v.setChecked(false);
                return;
            }

            entry.isSelected = isChecked;

            if (entry.isSelected) {
                mSelectedImages.add(entry);
            } else {
                mSelectedImages.remove(entry);
            }

            if (mCallBack != null) {
                mCallBack.onSelectImage(entry, entry.isSelected);
            }

        }

        private void onItemClick(View v) {
            int position = (int) v.getTag(R.id.tag_02);
            if (getItemViewType(position) == TYPE_HEADER) {
                Log.i(TAG, "onClick: is header todo go camera ");
                if (mCallBack != null) {
                    mCallBack.onClickCamera();
                }
                return;
            }

            MultiSelectorImage entry = (MultiSelectorImage) v.getTag(R.id.tag_01);
            ShowPhotoDialog dialog = new ShowPhotoDialog(mContext);
            dialog.setData(entry.path);
            dialog.setShowThumb(false);
            dialog.show();
        }


    }

    public interface CallBack {
        /**
         * 点击Camera
         */
        void onClickCamera();

        /**
         * 点击图片
         */
        void onSelectImage(MultiSelectorImage entry, boolean selected);
    }

}
