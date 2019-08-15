package com.ishow.common.entries.status

import androidx.annotation.StringRes

class Success {
    var message: String? = null
    var messageRes: Int = 0

    companion object {

        /**
         * New
         */
        fun new(message: String?): Success {
            val success = Success()
            success.message = message
            return success
        }

        /**
         * New
         */
        fun new(@StringRes messageRes: Int): Success {
            val success = Success()
            success.messageRes = messageRes
            return success
        }
    }
}
