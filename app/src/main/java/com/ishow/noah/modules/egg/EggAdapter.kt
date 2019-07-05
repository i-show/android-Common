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

package com.ishow.noah.modules.egg

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ishow.common.adapter.RecyclerAdapter

import com.ishow.noah.R
import com.ishow.noah.entries.egg.Egg

/**
 * Created by yuhaiyang on 2017/6/5.
 * 彩蛋
 */

internal class EggAdapter(context: Context) : RecyclerAdapter<Egg, EggAdapter.ViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val item = mLayoutInflater.inflate(R.layout.item_egg_list, parent, false)
        return ViewHolder(item, type)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        val entry = getItem(position)
        holder.title.text = entry.name
        holder.title.tag = entry
    }

    internal inner class ViewHolder(item: View, type: Int) : RecyclerAdapter.Holder(item, type), View.OnClickListener {
        var title: TextView = item.findViewById(R.id.title)

        init {
            title.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val entry = v.tag as Egg
            mContext.startActivity(entry.getFormatAction(mContext))
        }
    }
}
