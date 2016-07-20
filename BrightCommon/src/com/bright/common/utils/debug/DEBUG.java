/**
 * Copyright (C) 2016 yuhaiyang android source project
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

package com.bright.common.utils.debug;

import android.text.TextUtils;
import android.util.Log;

/**
 * debug
 */
public class DEBUG {
    private static final String TAG = "debug";

    public static void d(String tag, String content) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        Log.d(tag, content);
    }
}