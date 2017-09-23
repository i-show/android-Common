/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
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

package com.ishow.common.widget.dialog.picker;

import android.content.Context;


import com.ishow.common.entries.IUnitPicker;
import com.ishow.common.widget.pickview.adapter.PickerAdapter;

import java.util.ArrayList;
import java.util.List;


class PickerDialogAdapter<T extends IUnitPicker> extends PickerAdapter {

    private List<T> mData;
    private Context mContext;

    PickerDialogAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public void setData(List<T> datas) {
        if (datas == null) {
            mData.clear();
        } else {
            mData = datas;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int index) {
        return mData.get(index);
    }

    @Override
    public String getItemText(int position) {
        IUnitPicker picker = mData.get(position);
        return picker.getTitle(mContext);
    }

}