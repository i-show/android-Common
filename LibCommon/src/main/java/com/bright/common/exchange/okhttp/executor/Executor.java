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

package com.bright.common.exchange.okhttp.executor;

import android.text.TextUtils;

import com.bright.common.entries.KeyValue;
import com.bright.common.exchange.okhttp.request.GetRequest;
import com.bright.common.exchange.okhttp.request.Request;
import com.bright.common.utils.StringUtils;

import java.util.List;

/**
 * Created by Bright.Yu on 2017/2/20.
 * Execuor
 */

public abstract class Executor {

    public abstract void init();

    public abstract void execute(Request request);


    /**
     * Format Url
     */
    protected String formatUrl(String url, List<KeyValue> paramList) {
        String paramsString = buildParams(paramList);
        if (TextUtils.isEmpty(paramsString)) {
            return url;
        }

        if (url.contains("?")) {
            return url + "&" + paramsString;
        } else {
            return url + "?" + paramsString;
        }
    }

    /**
     * parsms to string
     */
    private String buildParams(List<KeyValue> paramList) {
        if (paramList == null || paramList.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        for (KeyValue param : paramList) {
            String key = param.getKey();
            Object value = param.getValue();

            if (value instanceof String) {
                builder.append(key);
                builder.append("&");
                builder.append(String.valueOf(value));
            }
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}
