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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.ishow.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础的Adapter
 */
public abstract class ListAdapter<DATA, HOLDER extends ListAdapter.Holder> extends BaseAdapter {

    private static final String TAG = "ListAdapter";
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


    public ListAdapter(Context context) {
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
    @SuppressWarnings("WeakerAccess")
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
    @SuppressWarnings("unused")
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size() + getHeaderCount() + getFooterCount();
    }

    /**
     * 获取移除Header的Count
     */
    @SuppressWarnings("WeakerAccess")
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

    @Override
    public DATA getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获取移除 Header的Item
     */
    @SuppressWarnings("WeakerAccess")
    public DATA getRealItem(int position) {
        return mData.get(position - getHeaderCount());
    }

    @SuppressWarnings("unused")
    public DATA getRealItem(ListView list, int position) {
        int index = position - list.getHeaderViewsCount() - getHeaderCount();
        if (index >= 0 && mData.size() > 0) {
            return mData.get(index);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        HOLDER holder;
        final int type = getItemViewType(position);
        if (convertView == null) {
            holder = onCreateViewHolder(parent, type);
            convertView = holder.getItemView();
        } else {
            holder = (HOLDER) convertView.getTag(R.id.tag_view_holder);
            // 如果当前的View Type 和 已经缓存的不同就重新加载
            int _type = holder.getType();
            if (type != _type) {
                holder = onCreateViewHolder(parent, type);
                convertView = holder.getItemView();
            }

        }
        onBindViewHolder(parent, holder, position, type);
        return convertView;
    }

    /**
     * 创建View容器
     *
     * @param type 当前view的type
     * @return 当前view的容器
     */
    public abstract HOLDER onCreateViewHolder(ViewGroup parent, int type);

    /**
     * 通过容器来 绑定view
     *
     * @param holder   当前view的容器
     * @param position 当前的 position
     * @param type     当前view的type
     */
    public abstract void onBindViewHolder(ViewGroup parent, HOLDER holder, int position, int type);


    /**
     * ViewHolder复用View的
     */
    public static abstract class Holder {
        private View item;
        private int mType;

        public Holder(View item, int type) {
            mType = type;
            if (item == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.item = item;
            item.setTag(R.id.tag_view_holder, this);
        }

        public int getType() {
            return mType;
        }

        public View getItemView() {
            return item;
        }
    }

}
