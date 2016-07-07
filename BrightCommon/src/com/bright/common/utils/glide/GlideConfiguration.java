/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bright.common.utils.glide;

import android.content.Context;

import com.bright.common.R;
import com.bright.common.utils.glide.okhttp.OkHttpUrlLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.InputStream;

/**
 * Glide 加载配置文件
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.tag_glide);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // 设置OKHttp为默认引擎
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }
}