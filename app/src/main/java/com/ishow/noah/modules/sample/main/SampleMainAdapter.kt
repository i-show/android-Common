package com.ishow.noah.modules.sample.main

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ishow.common.adapter.RecyclerAdapter

import com.ishow.noah.R
import com.ishow.common.extensions.inflate
import com.ishow.noah.modules.sample.entries.Sample

/**
 * Created by yuhaiyang on 2017/10/12.
 * Sample Main Adapter
 */
internal class SampleMainAdapter(context: Context) : RecyclerAdapter<Sample, SampleMainAdapter.ViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): ViewHolder {
        val item =  parent.inflate(R.layout.item_sample_main)
        return ViewHolder(item, type)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, type: Int) {
        val entry = getItem(position)
        holder.name.text = entry.getName(mContext)
        holder.name.tag = entry
    }

    internal inner class ViewHolder(item: View, type: Int) : RecyclerAdapter.Holder(item, type), View.OnClickListener {
        var name: TextView

        init {
            name = item.findViewById<View>(R.id.sample_name) as TextView
            name.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val entry = v.tag as Sample
            entry.startAction(mContext)
        }
    }
}
