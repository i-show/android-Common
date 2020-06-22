package com.ishow.common.entries.status

import androidx.annotation.StringRes

class Success {
    var message: String? = null
    var messageRes: Int = 0
    var code: Int = 0
    var obj: Any? = null
    var obj2: Any? = null

    companion object {

        /**
         * New
         */
        fun new(code: Int = 0, message: String? = ""): Success {
            val success = Success()
            success.code = code
            success.message = message
            return success
        }

        /**
         * New
         */
        fun new(code: Int = 0, @StringRes messageRes: Int): Success {
            val success = Success()
            success.code = code
            success.messageRes = messageRes
            return success
        }
    }
}
