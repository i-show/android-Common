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

package com.ishow.common.modules.image.show;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.viewpager.widget.PagerAdapter;
import com.ishow.common.R;
import com.ishow.common.utils.DeviceUtils;
import com.ishow.common.utils.image.loader.IImageLoaderListerner;
import com.ishow.common.utils.image.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 查看大图
 */
public class ShowPhotoAdapter extends PagerAdapter implements PhotoViewAttacher.OnViewTapListener {
    private LayoutInflater mLayoutInflater;
    private List<String> mUrls;
    private Context mContext;
    /**
     * 点击的View 用来获取大小
     */
    private View mBeforeView;
    /**
     * 缩略图的配置
     */
    private FrameLayout.LayoutParams mThumbLayoutParams;
    /**
     * 进度条配置
     */
    private FrameLayout.LayoutParams mProgressParams;
    /**
     * 当前的Dialog
     */
    private ShowPhotoDialog mDialog;

    private boolean isShowThumb = true;

    ShowPhotoAdapter(Context context) {
        mContext = context;
        mUrls = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
        mThumbLayoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mThumbLayoutParams.gravity = Gravity.CENTER;

        mProgressParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mProgressParams.gravity = Gravity.CENTER;
    }

    public void setDialog(ShowPhotoDialog dialog) {
        mDialog = dialog;
    }

    public void setData(List<String> url) {
        if (url == null) {
            mUrls = new ArrayList<>();
        } else {
            mUrls = url;
        }
        notifyDataSetChanged();
    }

    public void setShowThumb(boolean showThumb) {
        isShowThumb = showThumb;
    }

    public void setBeforView(View image) {
        mBeforeView = image;
        if (mBeforeView != null) {
            int width = mBeforeView.getWidth();
            int height = mBeforeView.getHeight();
            final int[] screen = DeviceUtils.INSTANCE.getScreenSize();
            //mLoadAnimation = new GlideAnimation(width, height);
            mThumbLayoutParams.width = width;
            mThumbLayoutParams.height = height;

            width = Math.min(screen[0] / 6, width * 3 / 7);
            height = Math.min(screen[1] / 6, height * 3 / 7);

            mProgressParams.width = width;
            mProgressParams.height = height;
        }
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final String url = mUrls.get(position);
        View root = mLayoutInflater.inflate(R.layout.item_show_photo, container, false);
        final PhotoView imageHD = root.findViewById(R.id.image);
        imageHD.setOnViewTapListener(this);

        final PhotoView imageThumb = root.findViewById(R.id.thumbnails);
        imageThumb.setEnabled(false);
        imageThumb.setOnViewTapListener(this);
        imageThumb.setLayoutParams(mThumbLayoutParams);
        if (mBeforeView instanceof ImageView) {
            ImageView image = (ImageView) mBeforeView;
            imageThumb.setScaleType(image.getScaleType());
        }

        final ProgressBar progress = root.findViewById(R.id.progress);
        progress.setLayoutParams(mProgressParams);

        if (isShowThumb) {
            progress.setVisibility(View.VISIBLE);
            imageThumb.setVisibility(View.VISIBLE);
            ImageLoader.with(mContext)
                    .load(url)
                    .into(imageThumb);
        } else {
            progress.setVisibility(View.GONE);
            imageThumb.setVisibility(View.GONE);
        }


        ImageLoader.with(mContext)
                .load(url)
                .listener(new IImageLoaderListerner() {
                    @Override
                    public void onFailed() {
                        imageThumb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                        imageThumb.setEnabled(true);
                        progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(Drawable drawable) {
                        imageThumb.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                    }
                })
                .into(imageHD);

        container.addView(root);
        return root;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object child) {
        container.removeView((View) child);
    }

    @Override
    public void onViewTap(View view, float v, float v1) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}