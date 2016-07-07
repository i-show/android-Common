/**
 * Copyright (C) 2015  Haiyang Yu Android Source Project
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

package com.bright.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    private static final String TAG = Utils.class.getSimpleName();
    public static final String EMPTY = "";
    public static final String JSON_EMPTY = "{}";
    public static final String NEW_LINE = "\n";
    public static final String TAB = "\t";
    public static final String SLADH = "/";

    /**
     * 获取屏幕的宽和高
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        
        return new int[]{dm.widthPixels, dm.heightPixels};
    }

    /**
     * 获取状态懒的高度
     */
    public static int getStatusBarHeight(Activity context) {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }


    /**
     * 字符串累加
     */
    public static String plusString(Object... strs) {
        StringBuilder builder = new StringBuilder();
        for (Object str : strs) {
            String _str = String.valueOf(str);
            if (!TextUtils.isEmpty(_str)) {
                builder.append(str);
            }
        }
        return builder.toString();
    }
}
