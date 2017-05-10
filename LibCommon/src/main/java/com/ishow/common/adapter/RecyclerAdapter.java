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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public abstract class RecyclerAdapter<DATA, HOLDER extends RecyclerAdapter.Holder> extends RecyclerView.Adapter<HOLDER> {
    private static final String TAG = "RecyclerAdapter";
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

    /**
     * 可能会参数
     */
    private Object mParam;
    /**
     * 请求地址
     */
    private String mUrl;

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
            mData = data;
            notifyDataSetChanged();
        } else if (force) {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * 只添加一组数据
     */
    @SuppressWarnings("unused")
    public void setOnlyOneData(DATA data) {
        if (data != null) {
            mData.clear();
            mData.add(data);
            notifyDataSetChanged();
        } else {
            Log.i(TAG, "setOnlyOneData data is null");
        }
    }

    /**
     * 增加数据
     */
    @SuppressWarnings("unused")
    public void plusData(List<DATA> data) {
        if (data != null) {
            mData.addAll(data);
            notifyDataSetChanged();
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
    @SuppressWarnings("unused")
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
     * 获取请求参数
     */
    public Object getParam() {
        return mParam;
    }

    /**
     * 设定请求参数
     */
    public void setParam(Object param) {
        mParam = param;
    }

    /**
     * 获取请求的地址
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * 请求的地址
     */
    public void setUrl(String url) {
        mUrl = url;
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

    @Override
    public abstract HOLDER onCreateViewHolder(ViewGroup parent, int type);

    public abstract void onBindViewHolder(HOLDER holder, int position, int type);

    @Override
    public void onBindViewHolder(HOLDER holder, int position) {
        onBindViewHolder(holder, position, holder.getItemViewType());
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
}
