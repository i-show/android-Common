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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装后的RecycleAdapter
 */
public abstract class RecyclerAdapter<DATA, HOLDER extends RecyclerAdapter.Holder> extends RecyclerView.Adapter<HOLDER> {
    /**
     * 类型 是头部
     */
    @SuppressWarnings("unused")
    public static final int TYPE_HEADER = 0;
    /**
     * 类型是body
     */
    @SuppressWarnings("WeakerAccess")
    public static final int TYPE_BODY = 1;
    /**
     * 类型是最后一个
     */
    @SuppressWarnings("unused")
    public static final int TYPE_FOOTER = 2;

    private List<DATA> mData = new ArrayList<>();
    protected Context mContext;
    protected LayoutInflater mLayoutInflater;
    private OnItemClickListener mOnItemClickListener;
    protected boolean disableOnItemClickListener = false;
    /**
     * 当前绑定的RecycleView
     * {@link #onAttachedToRecyclerView(RecyclerView)} 中进行赋值操作
     * {@link #onDetachedFromRecyclerView(RecyclerView)} 会进行值空
     */
    private RecyclerView mRecyclerView;

    public RecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * 添加数据
     */
    public void setData(List<DATA> data) {
        setData(data, true);
    }

    /**
     * @param data  设定的值
     * @param force 是否强制添加值
     */
    public void setData(List<DATA> data, boolean force) {
        if (data != null) {
            boolean canAni = mData.isEmpty();
            mData = data;
            notifyDataSetChanged();
            if (canAni) animation();
        } else if (force) {
            final int size = mData.size();
            mData.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * 是否为空
     */
    public boolean isEmpty() {
        return mData == null || mData.isEmpty();
    }

    /**
     * 增加数据
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public void plusData(List<DATA> data) {
        if (data != null) {
            int lastIndex = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(lastIndex, data.size());
        }
    }

    /**
     * 获取数据
     */
    public List<DATA> getData() {
        return mData;
    }


    /**
     * 清空数据
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size() + getHeaderCount() + getFooterCount();
    }

    /**
     * 获取移除Header的Count
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    public int getRealCount() {
        return mData.size();
    }

    /**
     * 获取真正的 DATA index
     */
    @SuppressWarnings("unused")
    public int getRealPosition(int position) {
        return position - getHeaderCount();
    }

    public DATA getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获取移除 Header的Item
     */
    @SuppressWarnings("unused")
    public DATA getRealItem(int position) {
        return mData.get(position - getHeaderCount());
    }

    // 获取View的type
    public int getItemViewType(int position) {
        return TYPE_BODY;
    }

    /**
     * 获取头部数量
     */
    @SuppressWarnings("WeakerAccess")
    public int getHeaderCount() {
        return 0;
    }

    /**
     * 获取Footer数量
     */
    @SuppressWarnings("WeakerAccess")
    public int getFooterCount() {
        return 0;
    }

    @NonNull
    @Override
    public abstract HOLDER onCreateViewHolder(@NonNull ViewGroup parent, int type);

    public abstract void onBindViewHolder(HOLDER holder, int position, int type);

    @Override
    public void onBindViewHolder(@NonNull HOLDER holder, int position) {
        holder.getItemView().setTag(R.id.tag_view_holder_recycle_item_click, position);
        if(!disableOnItemClickListener) {
            holder.getItemView().setOnClickListener(new ItemViewClickListener());
        }
        onBindViewHolder(holder, position, holder.getItemViewType());
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    /**
     * 执行动画
     */
    private void animation() {
        if (mRecyclerView != null) {
            mRecyclerView.scheduleLayoutAnimation();
        }
    }

    public static abstract class Holder extends RecyclerView.ViewHolder {
        private View mItemView;
        private int mType;

        public Holder(View item, int type) {
            super(item);
            mItemView = item;
            mType = type;
        }


        public int getType() {
            return mType;
        }

        public View getItemView() {
            return mItemView;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private class ItemViewClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick((Integer) v.getTag(R.id.tag_view_holder_recycle_item_click));
            }
        }
    }
}
