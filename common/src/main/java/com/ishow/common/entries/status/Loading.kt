package com.ishow.common.entries.status

import com.ishow.common.utils.StringUtils
import java.util.*

class Loading {

    var type: Int = 0
    var message: String? = null
    var status: Int = Status.Show
    var tag: String? = null

    companion object {
        /**
         * Dialog展示
         */
        fun dialog(message: String? = StringUtils.EMPTY, tag: String? = null): Loading {
            val loading = Loading()
            loading.type = Type.Dialog
            loading.message = message
            loading.status = Status.Show
            loading.tag = tag
            return loading
        }

        /**
         * View展示
         */
        fun view(message: String? = StringUtils.EMPTY, tag: String? = null): Loading {
            val loading = Loading()
            loading.type = Type.View
            loading.message = message
            loading.status = Status.Show
            loading.tag = tag
            return loading
        }

        /**
         * New
         */
        fun new(type: Int = Type.View, message: String? = StringUtils.EMPTY, tag: String? = null): Loading {
            val loading = Loading()
            loading.type = type
            loading.message = message
            loading.status = Status.Show
            loading.tag = tag
            return loading
        }

        /**
         * Dismiss
         */
        fun dismiss(type: Int = Type.Dialog, message: String? = StringUtils.EMPTY, tag: String? = null): Loading {
            val loading = Loading()
            loading.type = type
            loading.message = message
            loading.status = Status.Dismiss
            loading.tag = tag
            return loading
        }

        /**
         * Dismiss
         */
        fun dismissView(message: String? = StringUtils.EMPTY, tag: String? = null): Loading {
            val loading = Loading()
            loading.type = Type.View
            loading.message = message
            loading.status = Status.Dismiss
            loading.tag = tag
            return loading
        }

        fun randomTag(loading: Loading, loadingTag: Boolean) {
            if (loadingTag) {
                loading.tag = UUID.randomUUID().toString()
            }
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

    object Status {
        /**
         * 通过Dialog样式来展示
         */
        const val Show = 1
        /**
         * 通过View样式来展示
         */
        const val Dismiss = 2
    }
}
