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

package com.ishow.common.utils.image.loader.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.ObjectKey;
import com.ishow.common.utils.image.loader.IImageLoaderExecutor;
import com.ishow.common.utils.image.loader.ImageLoader;
import com.ishow.common.utils.image.loader.ImageLoaderParams;

/**
 * Created by Bright on 2017/4/10.
 * Glide 加载图片
 */
public class GlideImageLoaderExecutor implements IImageLoaderExecutor {
    private static final String TAG = "GlideImageLoaderExecuto";

    @Override
    public void display(@NonNull ImageLoaderParams params, @NonNull ImageView view) {
        Context context = params.getContext();
        if (context == null) {
            Log.i(TAG, "display: context is null");
            return;
        }
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            Log.i(TAG, "display: activity is");
            return;
        }

        params.checkValid();
        // 请求
        RequestBuilder<Drawable> builder = Glide.with(context)
                .load(params.getUrl());

        // 参数配置
        RequestOptions options = new RequestOptions();

        // 配置特殊加载
        setPlan(options, params);
        // setListener
        setListener(builder, params);

        // 设置加载的占位
        if (params.getPlaceholderDrawable() != null) {
            options.placeholder(params.getPlaceholderDrawable());
        } else if (params.getPlaceholderRes() > 0) {
            options.placeholder(params.getPlaceholderRes());
        }

        // 设置加载失败时候的显示
        if (params.getErrorDrawable() != null) {
            options.error(params.getErrorDrawable());
        } else if (params.getErrorRes() > 0) {
            options.error(params.getErrorRes());
        }

        // 设置加载的模式
        switch (params.getMode()) {
            case ImageLoader.LoaderMode.CENTER_CROP:
                options.centerCrop();
                break;
            case ImageLoader.LoaderMode.FIT_CENTER:
                options.fitCenter();
                break;
            case ImageLoader.LoaderMode.CENTER_INSIDE:
                options.centerInside();
                break;
            case ImageLoader.LoaderMode.CIRCLE_CROP:
                options.circleCrop();
                break;
            case ImageLoader.LoaderMode.NONE:
                break;
        }

        builder.apply(options)
                .into(view);
    }


    /**
     * 目的是有些特殊加载可以通过设置一个plan来特殊处理
     */
    private void setPlan(RequestOptions options, final ImageLoaderParams params) {
        switch (params.getPlan()) {
            case ImageLoader.Plan.NORMAL:
                // TODO
                break;
            case ImageLoader.Plan.NEW_LOAD:
                options.signature(new ObjectKey(System.currentTimeMillis()));
                break;
        }
    }


    /**
     * 设置回调监听
     */
    private void setListener(RequestBuilder<Drawable> builder, final ImageLoaderParams params) {
        if (params.getImageLoaderListener() == null) {
            return;
        }

        builder.listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                params.getImageLoaderListener().onFailed();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                params.getImageLoaderListener().onSuccess(resource);
                return false;
            }
        });
    }
}
