/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.bright.common.utils;

import android.content.Context;
import android.widget.Toast;

import com.bright.common.widget.YToast;

import java.lang.ref.WeakReference;

/**
 * 提示信息工具类
 * 可以有效规避 连续弹框的问题
 */
public class ToastUtils {

    private static WeakReference<Toast> mToast;

    public static void showToast(Context context, CharSequence text, int duration) {
        Toast toast;
        if (mToast == null || mToast.get() == null) {
            toast = YToast.makeText(context, text, duration);
            mToast = new WeakReference<>(toast);
        } else {
            toast = mToast.get();
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }

}
