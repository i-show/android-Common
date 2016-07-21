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

import com.alibaba.fastjson.JSONException;
import com.bright.common.utils.Utils;
import com.bright.common.utils.json.JsonValidator;

import okhttp3.Call;
import okhttp3.Response;

/**
 * JSON
 */
public abstract class JsonCallBack extends CallBack<String> {
    private static final String TAG = "JsonCallBack";

    public JsonCallBack() {
        this(null, false);
    }

    public JsonCallBack(Context context) {
        this(context, true);
    }

    public JsonCallBack(Context context, boolean showTip) {
        mContext = context;
        isShowTip = showTip;
    }

    @Override
    public void generateFinalResult(final Call call, final Response response, final int id) throws Exception {
        String result = response.body().string();
        // 检测json是否有效！如果无效不进行返回
        JsonValidator validator = new JsonValidator();
        boolean valid = validator.validate(result);
        if (!valid) {
            String message = Utils.plusString(result, " is not a json");
            sendFailResult(call, new JSONException(message), null, 0, id);
            return;
        }
        sendSuccessResultCallback(result, id);
    }


}
