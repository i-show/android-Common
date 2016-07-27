/**
 * Copyright (C) 2016 yuhaiyang android source project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bright.common.R;
import com.bright.common.utils.ScreenUtils;
import com.bright.common.widget.dialog.ShowPhotoDialog;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 查看大图
 */
public class ShowPhotoAdapter extends PagerAdapter implements PhotoViewAttacher.OnViewTapListener {
    private static final String TAG = "ShowPhotoAdapter";
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
     * 加载动画
     */
    private GlideAnimation mLoadAnimation;
    /**
     * 当前的Dialog
     */
    private ShowPhotoDialog mDialog;

    private boolean isShowThumb = true;

    public ShowPhotoAdapter(Context context) {
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
            final int[] screen = ScreenUtils.getScreenSize(mContext);
            mLoadAnimation = new GlideAnimation(width, height);
            mThumbLayoutParams.width = width;
            mThumbLayoutParams.height = height;

            width = Math.min(screen[0] / 5, width * 4 / 7);
            height = Math.min(screen[1] / 5, height * 4 / 7);

            mProgressParams.width = width;
            mProgressParams.height = height;
        }
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String url = mUrls.get(position);
        View root = mLayoutInflater.inflate(R.layout.item_show_photo, null);
        final PhotoView imageHD = (PhotoView) root.findViewById(R.id.image);
        imageHD.setOnViewTapListener(this);

        final PhotoView imageThumb = (PhotoView) root.findViewById(R.id.thumbnails);
        imageThumb.setEnabled(false);
        imageThumb.setOnViewTapListener(this);
        imageThumb.setLayoutParams(mThumbLayoutParams);
        if (mBeforeView instanceof ImageView) {
            ImageView image = (ImageView) mBeforeView;
            imageThumb.setScaleType(image.getScaleType());
        }

        final ProgressBar progress = (ProgressBar) root.findViewById(R.id.progress);
        progress.setLayoutParams(mProgressParams);
        
        if (isShowThumb) {
            progress.setVisibility(View.VISIBLE);
            imageThumb.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(url)
                    .into(imageThumb);
        } else {
            progress.setVisibility(View.GONE);
            imageThumb.setVisibility(View.GONE);
        }

        DrawableTypeRequest<String> request = Glide.with(mContext)
                .load(url);

        DrawableRequestBuilder<String> bulider;

        if (mLoadAnimation == null) {
            bulider = request.crossFade();
        } else {
            bulider = request.animate(mLoadAnimation);
        }

        bulider.listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                imageThumb.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                imageThumb.setEnabled(true);
                progress.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                imageThumb.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                return false;
            }
        })
                .into(imageHD);

        container.addView(root);
        return root;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object child) {
        container.removeView((View) child);
    }

    @Override
    public void onViewTap(View view, float v, float v1) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    private class GlideAnimation implements ViewPropertyAnimation.Animator {
        private int mWidth;
        private int mHeight;

        public GlideAnimation(int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        @Override
        public void animate(View view) {
            float width = view.getWidth();
            float height = view.getHeight();
            float startX = Math.min(mWidth / width + 0.2f, 1f);
            float startY = Math.min(mHeight / height + 0.2f, 1f);
            PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("scaleX", startX, 1f);
            PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleY", startY, 1f);
            ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2).setDuration(500).start();
        }
    }
}