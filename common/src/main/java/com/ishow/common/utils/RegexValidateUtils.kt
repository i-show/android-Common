/*
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

package com.ishow.common.utils

import java.util.regex.Pattern

/**
 * 使用正则表达式验证输入格式
 */
object RegexValidateUtils {
    /**
     * 邮箱的正则表达式
     */
    private const val EMAIL_PATTERN = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$"
    /**
     * 手机号码的正则表达式
     */
    private const val PHONE_PATTERN = "^((1[3-9][0-9])\\d{8})\$"
    /**
     * 身份证的正则表达式
     */
    private const val ID_CARD_PATTERN = "([0-9]{17}([0-9]|X))|([0-9]{15})"

    /**
     * 验证邮箱
     */
    fun checkEmail(email: String?): Boolean {
        if (email.isNullOrBlank() || !email.contains("@")) {
            return false
        }

        if (email.split("@").size != 2) {
            return false
        }

        return try {
            Pattern.matches(EMAIL_PATTERN, email)
        } catch (e: Exception) {
            false
        }

    }

    /**
     * 验证手机号码
     */
    fun checkMobileNumber(number: String?): Boolean {
        if (number.isNullOrBlank() || number.length != 11) {
            return false
        }
        val checkNumber = number.replace(" ", "")
        return try {
            Pattern.matches(PHONE_PATTERN, checkNumber)
        } catch (e: Exception) {
            false
        }
    }


    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    fun checkIdCard(idCard: String?): Boolean {
        if (idCard.isNullOrBlank()) {
            return false
        }

        if (idCard.length != 15 && idCard.length != 18) {
            return false
        }

        return try {
            Pattern.matches(ID_CARD_PATTERN, idCard)
        } catch (e: Exception) {
            false
        }
    }


    /**
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     * 校验银行卡卡号
     */
    fun checkBankCard(bankCard: String): Boolean {
        if (bankCard.length < 15 || bankCard.length > 19) {
            return false
        }
        val bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length - 1))
        return if (bit == 'N') {
            false
        } else {
            bankCard[bankCard.length - 1] == bit
        }
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    private fun getBankCardCheckCode(nonCheckCodeBankCard: String?): Char {
        if (nonCheckCodeBankCard.isNullOrBlank() || !nonCheckCodeBankCard.matches("\\d+".toRegex())) {
            //如果传的不是数据返回N
            return 'N'
        }

        val chs = nonCheckCodeBankCard.trim { it <= ' ' }.toCharArray()
        var luhmSum = 0
        var i = chs.size - 1
        var j = 0
        while (i >= 0) {
            var k = chs[i] - '0'
            if (j % 2 == 0) {
                k *= 2
                k = k / 10 + k % 10
            }
            luhmSum += k
            i--
            j++
        }
        return if (luhmSum % 10 == 0) '0' else (10 - luhmSum % 10 + '0'.toInt()).toChar()
    }

}