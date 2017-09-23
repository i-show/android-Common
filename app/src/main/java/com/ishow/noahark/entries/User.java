/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
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

package com.ishow.noahark.entries;

/**
 * 用户信息的实体类
 */
public class User {

    public static final class Key {
        /**
         * 保存User
         */
        public static final String KEY_CACHE_USER = "key_cache_user";
        /**
         * 保存账户的Key
         */
        public static final String ACCOUNT = "key_login_account";
        /**
         * 保存密码的key
         */
        public static final String PASSWORD = "key_login_password";
        /**
         * 自动登录
         */
        public static final String AUTO_LOGIN = "key_auto_login";
    }
}
