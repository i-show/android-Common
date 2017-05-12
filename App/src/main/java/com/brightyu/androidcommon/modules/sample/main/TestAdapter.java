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

package com.brightyu.androidcommon.modules.sample.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.common.adapter.ListPullToRefreshAdapter;

/**
 * Created by yuhaiyang on 2017/5/12.
 */

public class TestAdapter extends ListPullToRefreshAdapter<String, TestAdapter.ViewHolder> {

    public TestAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        TextView textView = new TextView(mContext);
        textView.setHeight(100);
        return new ViewHolder(textView, type);
    }

    @Override
    public void onBindViewHolder(ViewGroup parent, ViewHolder holder, int position, int type) {
        TextView item = (TextView) holder.getItemView();
        item.setText(position + "");
    }

    class ViewHolder extends ListPullToRefreshAdapter.Holder {

        public ViewHolder(View item, int type) {
            super(item, type);
        }
    }
}
