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

package com.ishow.common.utils.image.loader;

import android.content.Context;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import com.ishow.common.utils.image.loader.glide.GlideImageLoaderExecutor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Bright on 2017/4/7.
 * 图片加载的框架
 */

public class ImageLoader {


    @IntDef({LoaderMode.NONE, LoaderMode.FIT_CENTER, LoaderMode.CENTER_CROP, LoaderMode.CENTER_INSIDE, LoaderMode.CIRCLE_CROP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoaderMode {
        /**
         * 居中
         */
        int NONE = 0;
        /**
         * 居中
         */
        int FIT_CENTER = 1;
        /**
         * 剪切
         */
        int CENTER_CROP = 2;
        /**
         * CENTER
         */
        int CENTER_INSIDE = 3;

        /**
         * CIRCLE
         */
        int CIRCLE_CROP = 4;
    }

    @IntDef({Plan.NORMAL, Plan.NEW_LOAD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Plan {
        /**
         * 方案-普通
         * 普通加载方案
         */
        int NORMAL = 1;
        /**
         * 方案- 每次都是重新哪最新的
         */
        int NEW_LOAD = 2;
    }

    private static ImageLoader sInstance;
    private IImageLoaderExecutor mExecutor;
    private ImageLoaderConfigure mConfigure;


    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化相关配置
     *
     * @param context 暂时备用的一个参数
     */
    public static void init(@NonNull Context context) {
        init(context, ImageLoaderConfigure.getDefaultConfigure());
    }


    /**
     * 初始化相关配置
     *
     * @param context   暂时备用的一个参数
     * @param configure 配置文件
     */
    public static void init(@NonNull Context context, @NonNull ImageLoaderConfigure configure) {
        init(context, new GlideImageLoaderExecutor(), configure);
    }

    /**
     * 初始化相关配置
     *
     * @param context  暂时备用的一个参数
     * @param executor 执行者
     */
    public static void init(@NonNull Context context, @NonNull IImageLoaderExecutor executor) {
        init(context, executor, ImageLoaderConfigure.getDefaultConfigure());
    }


    /**
     * 初始化相关配置
     *
     * @param context   暂时备用的一个参数
     * @param executor  执行者
     * @param configure 配置文件
     */
    public static void init(@NonNull Context context, @NonNull IImageLoaderExecutor executor, @NonNull ImageLoaderConfigure configure) {
        ImageLoader loader = getInstance();
        loader.mExecutor = executor;
        loader.mConfigure = configure;
    }


    public static ImageLoaderParams with(Context context) {
        return new ImageLoaderParams(context);
    }


    public static IImageLoaderExecutor getExecutor() {
        return getInstance().mExecutor;
    }

    public static ImageLoaderConfigure getConfigure() {
        return getInstance().mConfigure;
    }

}
