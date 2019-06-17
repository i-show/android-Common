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

import android.widget.ImageView;
import androidx.annotation.NonNull;

/**
 * Created by Bright on 2017/4/7.
 * 执行
 */
@SuppressWarnings("WeakerAccess,unused")
public interface IImageLoaderExecutor {
    /**
     * Display 用来显示图片
     */
    void display(@NonNull ImageLoaderParams params, @NonNull ImageView view);
}
