package com.ishow.common.utils.image.loader;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.ishow.common.utils.image.loader.glide.GlideImageLoaderExecutor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Bright on 2017/4/7.
 * 图片加载的框架
 */

public class ImageLoader {

    /**
     * 居中
     */
    @SuppressWarnings("WeakerAccess")
    public static final int MODE_FIT_CENTER = 1;
    /**
     * 剪切
     */
    @SuppressWarnings("WeakerAccess")
    public static final int MODE_CENTER_CROP = 2;


    /**
     * 方案-普通
     * 普通加载方案
     */
    @SuppressWarnings("WeakerAccess")
    public static final int PLAN_NORMAL = 1;

    @IntDef({MODE_FIT_CENTER, MODE_CENTER_CROP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface mode {
    }

    @IntDef({PLAN_NORMAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface plan {
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


    public static ImageLoaderParams width(Context context) {
        return new ImageLoaderParams(context);
    }


    public static IImageLoaderExecutor getExecutor() {
        return getInstance().mExecutor;
    }

    public static ImageLoaderConfigure getConfigure() {
        return getInstance().mConfigure;
    }

}
