/*
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

package com.ishow.noah.entries

/**
 * 用户信息的实体类
 */
class User {

    var id: String? = null
    var account: String? = null
    var phone: String? = null
    var nickName: String? = null
    var realName: String? = null
    var avatar: String? = null

    override fun toString(): String {
        return "User(id=$id, account=$account, phone=$phone, nickName=$nickName, realName=$realName, avatar=$avatar)"
    }
}
