package com.ishow.noahark.entries;

/**
 * Created by yuhaiyang on 2018/3/27.
 * 登录信息
 */

public class UserContainer {
    /**
     * 用户基础信息
     */
    private User user;

    /**
     * 用户的token
     */
    private Token token;


    public static final class Key {
        /**
         * 保存User
         */
        public static final String CACHE = "key_cache_user";
        /**
         * 保存账户的Key
         */
        public static final String ACCOUNT = "key_login_account";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
