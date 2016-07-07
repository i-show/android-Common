/**
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

package com.bright.common.utils;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则表达式验证输入格式
 */
public class RegexValidateUtil {
    private static final String TAG = "RegexValidateUtil";

    /**
     * 验证邮箱
     */
    public static boolean checkEmail(String email) {
        Log.d(TAG, "checkEmail:email = " + email);
        if (TextUtils.isEmpty(email) || !email.contains("@")) {
            return false;
        }

        if (email.split("@").length != 2) {
            return false;
        }

        try {
            String check = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证手机号码
     */
    public static boolean checkMobileNumber(String number) {
        boolean flag = false;
        try {
            Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(17([0-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(number);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}