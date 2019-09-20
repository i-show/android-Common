package com.ishow.common.entries.status

import androidx.annotation.StringRes
import com.ishow.common.utils.StringUtils

class Error {
    var showType: Int = 0
    var errorType: Int = 0
    var message: String? = null
    var messageRes: Int = 0

    companion object {
        /**
         * Dialog展示
         */
        fun dialog(message: String?, errorType: Int = 0): Error {
            val error = Error()
            error.showType = Type.Dialog
            error.errorType = errorType
            error.message = message
            return error
        }

        /**
         * Dialog展示
         */
        fun dialog(@StringRes messageRes: Int, errorType: Int = 0): Error {
            val error = Error()
            error.showType = Type.Dialog
            error.errorType = errorType
            error.messageRes = messageRes
            return error
        }

        /**
         * Toast展示
         */
        fun toast(message: String?, errorType: Int = 0): Error {
            val error = Error()
            error.showType = Type.Toast
            error.errorType = errorType
            error.message = message
            return error
        }

        /**
         * Toast展示
         */
        fun toast(@StringRes messageRes: Int, errorType: Int = 0): Error {
            val error = Error()
            error.showType = Type.Toast
            error.errorType = errorType
            error.messageRes = messageRes
            return error
        }

        /**
         * View展示
         */
        fun view(message: String? = StringUtils.EMPTY, errorType: Int = 0): Error {
            val error = Error()
            error.showType = Type.View
            error.errorType = errorType
            error.message = message
            return error
        }

        /**
         * View展示
         */
        fun view(@StringRes messageRes: Int, errorType: Int = 0): Error {
            val error = Error()
            error.showType = Type.View
            error.errorType = errorType
            error.messageRes = messageRes
            return error
        }

        /**
         * New
         */
        fun new(showType: Int = Type.View, message: String?, errorType: Int = 0): Error {
            val error = Error()
            error.showType = showType
            error.errorType = errorType
            error.message = message
            return error
        }

        /**
         * New
         */
        fun new(showType: Int = Type.View, messageRes: Int, errorType: Int = 0): Error {
            val error = Error()
            error.showType = showType
            error.errorType = errorType
            error.messageRes = messageRes
            return error
        }
    }

    object Type {
        /**
         * 通过Dialog样式来展示
         */
        const val Dialog = 1
        /**
         * 通过View样式来展示
         */
        const val View = 2
        /**
         * 通过Toast样式来展示
         */
        const val Toast = 3
    }
}
