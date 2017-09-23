package com.ishow.noahark.modules.sample.pulltorefresh;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishow.common.adapter.RecyclerAdapter;
import com.ishow.common.adapter.RecyclerPullToRefreshAdapter;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.noahark.R;
import com.ishow.noahark.modules.sample.entries.Job;

/**
 * Created by yuhaiyang on 2017/6/29.
 * 首页热门
 */

class SamplePullToRefreshAdapter extends RecyclerPullToRefreshAdapter<Job, SamplePullToRefreshAdapter.ViewHolder> {


    SamplePullToRefreshAdapter(Context context) {
        super(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View item = mLayoutInflater.inflate(R.layout.item_sample_pulltorefresh, parent, false);
        return new ViewHolder(item, type);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, int type) {
        Job entry = getItem(position);
        holder.getItemView().setTag(entry);

        ImageLoader.with(mContext)
                .load(entry.imagePath)
                .mode(ImageLoader.LoaderMode.CENTER_CROP)
                .into(holder.image);

        holder.name.setText(entry.getTitle());
    }

    class ViewHolder extends RecyclerAdapter.Holder {
        private ImageView image;
        private TextView name;

        ViewHolder(View item, int type) {
            super(item, type);
            image = (ImageView) item.findViewById(R.id.image);
            name = (TextView) item.findViewById(R.id.name);
        }
    }
}
