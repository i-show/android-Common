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

package com.ishow.common.widget.flowlayout;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bright.Yu on 2017/3/14.
 * FlowAdapter
 */

@SuppressWarnings("WeakerAccess")
public abstract class FlowAdapter<DATA, HOLDER extends FlowAdapter.Holder> {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    private List<DATA> mData;
    private Context mContext;
    protected LayoutInflater mLayoutInflater;

    public FlowAdapter(Context context) {
        mData = new ArrayList<>();
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
     * 获取数据
     */
    public List<DATA> getData() {
        return mData;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 清空数据
     */
    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public int getCount() {
        return mData.size();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
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
    public abstract void onBindViewHolder(HOLDER holder, int position, int type);


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
