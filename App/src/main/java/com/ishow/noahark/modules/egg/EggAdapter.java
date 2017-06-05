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

package com.ishow.noahark.modules.egg;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.common.adapter.RecyclerAdapter;
import com.ishow.noahark.R;
import com.ishow.noahark.entries.egg.Egg;

/**
 * Created by yuhaiyang on 2017/6/5.
 * 彩蛋
 */

class EggAdapter extends RecyclerAdapter<Egg, EggAdapter.ViewHolder> {

    public EggAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_egg_list, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        Egg entry = getItem(position);
        holder.title.setText(entry.name);
        holder.title.setTag(entry);
    }

    class ViewHolder extends RecyclerAdapter.Holder implements View.OnClickListener {
        TextView title;

        ViewHolder(View item, int type) {
            super(item, type);
            title = (TextView) item.findViewById(R.id.title);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Egg entry = (Egg) v.getTag();
            mContext.startActivity(entry.getFormatAction(mContext));
        }
    }
}
