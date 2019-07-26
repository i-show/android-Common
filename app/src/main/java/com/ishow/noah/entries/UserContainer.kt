package com.ishow.noah.entries

/**
 * Created by yuhaiyang on 2018/3/27.
 * 登录信息
 */

class UserContainer {
    /**
     * 用户基础信息
     */
    var user: User? = null

    /**
     * 用户的token
     */
    var token: Token? = null


    object Key {
        /**
         * 保存User
         */
        const val CACHE = "key_cache_user"
        /**
         * 保存账户的Key
         */
        const val ACCOUNT = "key_login_account"
    }

    override fun toString(): String {
        return "UserContainer(user=$user, token=$token)"
    }


}
