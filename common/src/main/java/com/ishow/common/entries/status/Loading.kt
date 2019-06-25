package com.ishow.common.entries.status

import com.ishow.common.utils.StringUtils

class Loading {

    var type: Int = 0
    var message: String? = null

    companion object {
        /**
         * Dialog展示
         */
        fun dialog(message: String = StringUtils.EMPTY): Loading {
            val loading = Loading()
            loading.type = Type.Dialog
            loading.message = message
            return loading
        }

        /**
         * View展示
         */
        fun view(message: String = StringUtils.EMPTY): Loading {
            val loading = Loading()
            loading.type = Type.View
            loading.message = message
            return loading
        }

        /**
         * New
         */
        fun new(type: Int = Type.View, message: String = StringUtils.EMPTY): Loading {
            val loading = Loading()
            loading.type = type
            loading.message = message
            return loading
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
    }
}
