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

package com.bright.common.utils.http.okhttp.callback;

import android.content.Context;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 普通字符回调
 */
public abstract class StringCallBack extends CallBack<String> {

    public StringCallBack() {
        this(null, false);
    }

    public StringCallBack(Context context) {
        this(context, true);
    }

    public StringCallBack(Context context, boolean showTip) {
        mContext = context;
        isShowTip = showTip;
    }

    @Override
    public void generateFinalResult(Call call, Response response, int id) throws Exception {
        String result = response.body().string();
        sendSuccessResultCallback(result, id);
    }
}
