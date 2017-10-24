package com.ishow.noahark.modules.sample.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishow.common.adapter.RecyclerAdapter;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.sample.entries.Sample;

/**
 * Created by yuhaiyang on 2017/10/12.
 * Sample Main Adapter
 */
class SampleMainAdapter extends RecyclerAdapter<Sample, SampleMainAdapter.ViewHolder> {

    SampleMainAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_sample_main, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        Sample entry = getItem(position);
        holder.name.setText(entry.getName(mContext));
        holder.name.setTag(entry);
    }

    class ViewHolder extends RecyclerAdapter.Holder implements View.OnClickListener {
        TextView name;

        ViewHolder(View item, int type) {
            super(item, type);
            name = (TextView) item.findViewById(R.id.sample_name);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Sample entry = (Sample) v.getTag();
            entry.startAction(mContext);
        }
    }
}
