package com.ishow.common.widget.viewpager.looping;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ishow.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图的Adapter
 */
public abstract class LoopingViewPagerAdapter<DATA, HOLDER extends LoopingViewPagerAdapter.Holder> extends PagerAdapter {
    private static final String TAG = "LoopingViewPagerAdapter";
    /**
     * 数据内容
     */
    private List<DATA> mData = new ArrayList<>();

    protected LayoutInflater mLayoutInflater;
    protected Context mContext;

    public LoopingViewPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    /**
     * @param data 设定的值
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
     * 获取当前数据item内容
     * 真实数据
     */
    public DATA getItem(int realPosition) {
        return mData.get(realPosition);
    }

    /**
     * 获取轮播图中图片数量
     * 第一个和最后一个 加一个可以轮播
     */
    @Override
    public int getCount() {
        int realCount = getRealCount();
        if (realCount <= 1) {
            return realCount;
        } else {
            return realCount * 3;
        }
    }

    /**
     * 获取轮播图的真实数量
     */
    @SuppressWarnings("WeakerAccess")
    public int getRealCount() {
        return mData.size();
    }

    /**
     * 根据轮播的position来获取真实的position
     */
    @SuppressWarnings("WeakerAccess")
    public int getRealPosition(int innerPosition) {
        int realCount = getRealCount();
        if (realCount <= 1) {
            return 0;
        }

        int realPosition = innerPosition % realCount;
        if (realPosition < 0) {
            realPosition += realCount;
        }
        return realPosition;
    }

    /**
     * 根据真实的position来获取轮播的position
     */
    public int getInnerPosition(int realPosition) {
        int realCount = getRealCount();
        if (realCount <= 1) {
            return realPosition;
        } else {
            return getRealCount() + realPosition;
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int realPosition = getRealPosition(position);
        Holder holder = onCreateView(container, realPosition, position);
        View item = holder.getItemView();
        onBindView((HOLDER) holder, realPosition, position);
        container.addView(item);
        return item;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof View) {
            View item = (View) object;
            container.removeView(item);
            recycle(item, position);
        } else {
            super.destroyItem(container, position, object);
        }
    }

    /**
     * 销毁View
     */
    protected void recycle(View item, int position) {
        //TODO
    }

    /**
     * 创建View容器
     *
     * @param position 当前的 position
     * @return 当前view的容器
     */
    @SuppressWarnings("WeakerAccess")
    public abstract HOLDER onCreateView(ViewGroup parent, int position, int innerPosition);

    /**
     * 通过容器来 绑定view
     *
     * @param holder   当前view的容器
     * @param position 当前的 position
     */
    @SuppressWarnings("WeakerAccess")
    public abstract void onBindView(HOLDER holder, int position, int innerPosition);


    /**
     * ViewHolder复用View的
     */
    public static abstract class Holder {
        private View item;
        private int mType;

        public Holder(View item, int type) {
            mType = type;
            if (item == null) {
                throw new IllegalArgumentException("itemView is null");
            }
            this.item = item;
            item.setTag(R.id.tag_looping_viewpager_holder, this);
        }

        public int getType() {
            return mType;
        }

        @SuppressWarnings("WeakerAccess")
        public View getItemView() {
            return item;
        }
    }
}
