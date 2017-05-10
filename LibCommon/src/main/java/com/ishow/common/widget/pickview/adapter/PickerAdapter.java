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

package com.ishow.common.widget.pickview.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;

public abstract class PickerAdapter<T> {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    /**
     * Gets items count
     *
     * @return the count of wheel items
     */
    public abstract int getCount();

    /**
     * Gets a wheel item by index.
     */
    public abstract T getItem(int position);

    /**
     * Gets show data
     */
    public abstract String getItemText(int position);

    /**
     * 注册监听
     */
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    /**
     * 取消注册监听
     */
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

}
