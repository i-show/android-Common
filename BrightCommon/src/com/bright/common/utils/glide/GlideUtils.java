/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

package com.bright.common.utils.glide;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bright.common.BaseActivity;
import com.bright.common.BaseFragment;
import com.bright.common.R;
import com.bumptech.glide.Glide;


/**
 * 图片加载的封装
 */
public class GlideUtils {

    public static void load(BaseActivity activity, String url, ImageView view) {
        Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.no_picture)
                .crossFade()
                .into(view);
    }

    public static void load(BaseActivity activity, @DrawableRes int url, ImageView view) {
        Glide.with(activity)
                .load(url)
                .placeholder(R.drawable.no_picture)
                .crossFade()
                .into(view);
    }


    public static void load(BaseFragment fragment, String url, ImageView view) {
        Glide.with(fragment)
                .load(url)
                .placeholder(R.drawable.no_picture)
                .crossFade()
                .into(view);
    }


    public static void load(BaseFragment fragment, @DrawableRes int url, ImageView view) {
        load(fragment, url, R.drawable.no_picture, view);
    }

    public static void load(BaseFragment fragment, @DrawableRes int url, ImageView view, boolean hasHolder) {
        if (hasHolder) {
            load(fragment, url, R.drawable.no_picture, view);
        } else {
            Glide.with(fragment)
                    .load(url)
                    .crossFade()
                    .into(view);
        }
    }


    public static void load(BaseFragment activity, String url, @DrawableRes int holder, ImageView view) {
        Glide.with(activity)
                .load(url)
                .placeholder(holder)
                .crossFade()
                .into(view);
    }

    public static void load(BaseFragment activity, @DrawableRes int url, @DrawableRes int holder, ImageView view) {
        Glide.with(activity)
                .load(url)
                .placeholder(holder)
                .crossFade()
                .into(view);
    }


    public static void load(Context context, String url, ImageView view) {
        load(context, url, view, true);
    }

    public static void load(Context context, String url, ImageView view, boolean isCenterCrop) {
        if (isCenterCrop) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.no_picture)
                    .crossFade()
                    .centerCrop()
                    .into(view);
        } else {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.no_picture)
                    .crossFade()
                    .into(view);
        }
    }


    public static void load(Context context, @DrawableRes int url, ImageView view) {
        load(context, url, R.drawable.no_picture, view);
    }

    public static void load(Context context, @DrawableRes int url, ImageView view, boolean hasHolder) {
        if (hasHolder) {
            load(context, url, R.drawable.no_picture, view);
        } else {
            Glide.with(context)
                    .load(url)
                    .crossFade()
                    .into(view);
        }
    }

    public static void load(Context context, @DrawableRes int url, @DrawableRes int holder, ImageView view) {
        Glide.with(context)
                .load(url)
                .placeholder(holder)
                .crossFade()
                .into(view);
    }


    public static void loadHeader(BaseActivity activity, String url, ImageView view) {
        Glide.with(activity)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(view);
    }

    public static void loadHeader(BaseFragment fragment, String url, ImageView view) {
        Glide.with(fragment)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(view);
    }

    public static void loadHeader(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(view);
    }
}
