package com.ishow.common.utils

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import androidx.annotation.StringRes

object SpanUtils {
    class Builder {

        private var body: String? = null

        private var context: Context? = null

        private var spanList = ArrayList<SpanDetail>()

        private var type = Type.FORMAT

        fun with(context: Context?): Builder {
            this.context = context
            return this
        }

        fun body(body: String): Builder {
            this.body = body
            return this
        }

        fun body(@StringRes body: Int): Builder {
            if (context == null) {
                throw IllegalStateException("Need with context")
            }
            this.body = context?.getString(body)
            return this
        }

        /**
         * 添加Span内容
         * @param span 例如 ForegroundColorSpan BackgroundColorSpan
         */
        fun add(span: Any, start: Int, end: Int, flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): Builder {
            val detail = SpanDetail()
            detail.span = span
            detail.start = start
            detail.end = end
            detail.flags = flags
            spanList.add(detail)
            return this
        }

        /**
         * 添加Span内容
         * @param span 例如 ForegroundColorSpan BackgroundColorSpan
         */
        fun add(span: Any, content: String, flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE): Builder {
            val detail = SpanDetail()
            detail.span = span
            detail.content = content
            detail.flags = flags
            spanList.add(detail)
            return this
        }

        /**
         * 添加Span内容
         * @param span 例如 ForegroundColorSpan BackgroundColorSpan
         */
        fun add(span: Any, @StringRes content: Int): Builder {
            val detail = SpanDetail()
            detail.span = span
            detail.content = context?.getString(content)
            detail.flags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            spanList.add(detail)
            return this
        }

        /**
         * 添加Span内容
         * @param span 例如 ForegroundColorSpan BackgroundColorSpan
         */
        fun type(type: Type): Builder {
            this.type = type
            return this
        }


        fun get(): SpannableString {
            if (body.isNullOrEmpty()) {
                throw IllegalStateException("body is null")
            }

            val resultString = when (type) {
                Type.FORMAT -> {
                    getFormatBody()
                }

                Type.PLUS -> {
                    getPlusBody()
                }
            }

            val result = SpannableString(resultString)

            for (span in spanList) {
                if (span.content.isNullOrEmpty()) {
                    result.setSpan(span.span, span.start, span.end, span.flags)
                } else {
                    val content = span.content!!
                    val start = resultString.indexOf(content)
                    val end = start + content.length
                    result.setSpan(span.span, start, end, span.flags)
                }
            }

            return result
        }


        private fun getPlusBody(): String {
            val result = body!!
            for (span in spanList) {
                if (span.content.isNullOrEmpty()) {
                    continue
                }
                result.plus(span.content)
            }
            return result
        }

        private fun getFormatBody(): String {
            val contentList: List<String> = spanList.mapNotNull { it.content }
            return String.format(body!!, *contentList.toTypedArray())
        }
    }

    private class SpanDetail {
        var span: Any? = null
        var content: String? = null
        var start: Int = 0
        var end: Int = 0
        var flags: Int = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    }

    enum class Type {
        /**
         * 累加在后面
         */
        PLUS,
        /**
         * StringFormat的方式
         */
        FORMAT
    }

}