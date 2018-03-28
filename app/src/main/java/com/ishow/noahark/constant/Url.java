package com.ishow.noahark.constant;

import com.ishow.common.utils.StringUtils;

/**
 * Created by yuhaiyang on 2018/3/22.
 * 连接地址
 */

public class Url {
    private static String getBaseUrl() {
        return "https://api.yuhaiyang.net/common";
    }

    /**
     * 登录
     */
    public static String login() {
        return StringUtils.plusString(getBaseUrl(), "/account/login");
    }

    /**
     * 登录
     */
    public static String loginByToken() {
        return StringUtils.plusString(getBaseUrl(), "/account/loginByToken");
    }

    /**
     * 注册
     */
    public static String register() {
        return StringUtils.plusString(getBaseUrl(), "/account/register");
    }

    /**
     * 忘记密码
     */
    public static String forgotPassword() {
        return StringUtils.plusString(getBaseUrl(), "/account/forgotPassword");
    }

    /**
     * 上传头像
     */
    public static String uploadAvatar(){
        return StringUtils.plusString(getBaseUrl(),"/account/uploadAvatar");
    }
}
