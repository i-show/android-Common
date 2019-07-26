package com.ishow.noah.entries

/**
 * Created by yuhaiyang on 2018/3/27.
 * Token
 */

class Token {
    var accessToken: String? = null
    var accessTokenExpire: Long = 0
    var refreshToken: String? = null
    var refreshTokenExpire: Long = 0
    var deviceToken: String? = null


    override fun toString(): String {
        return "Token(accessToken=$accessToken, accessTokenExpire=$accessTokenExpire, refreshToken=$refreshToken, refreshTokenExpire=$refreshTokenExpire, deviceToken=$deviceToken)"
    }
}
