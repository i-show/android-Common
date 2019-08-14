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

package com.ishow.common.widget.pickview.adapter

import android.database.DataSetObservable
import android.database.DataSetObserver

abstract class PickerAdapter<T> {

    private val mDataSetObservable = DataSetObservable()

    /**
     * Gets items count
     */
    abstract fun getCount(): Int

    /**
     * Gets a wheel item by index.
     */
    abstract fun getItem(position: Int): T

    /**
     * Gets show data
     */
    abstract fun getItemText(position: Int): String

    /**
     * 注册监听
     */
    fun registerDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.registerObserver(observer)
    }

    /**
     * 取消注册监听
     */
    fun unregisterDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.unregisterObserver(observer)
    }


    fun notifyDataSetChanged() {
        mDataSetObservable.notifyChanged()
    }

    fun isEmpty(): Boolean {
        return getCount() == 0;
    }
}
