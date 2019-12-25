package com.ishow.common.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.ishow.common.R
import com.ishow.common.extensions.dp2px
import com.ishow.common.extensions.findColor
import com.ishow.common.extensions.sp2px
import com.ishow.common.utils.log.LogUtils
import kotlinx.coroutines.*

/**
 * Created by yuhaiyang on 2019-11-05.
 * 输出log的View
 */
class PrintView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ScrollViewPro(context, attrs, defStyleAttr) {

    private var showJob: Job? = null
    /**
     * TextQueue 队列
     */
    private var textQueue = mutableListOf<String>()
    /**
     * 上一次展示的Log
     */
    private var lastLog: String? = null
    /**
     * 是否在打印
     */
    private var isPrinting = false

    private val textView: TextView = TextView(context)

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.PrintView)
        val minHeight = a.getDimensionPixelSize(R.styleable.PrintView_android_minHeight, 200.dp2px())
        val maxHeight = a.getDimensionPixelSize(R.styleable.PrintView_maxHeight, 300.dp2px())
        val textSize = a.getDimensionPixelSize(R.styleable.PrintView_textSize, 14.sp2px())
        val textColor = a.getColor(R.styleable.PrintView_textColor, context.findColor(R.color.text_grey))
        val textStyle = a.getInt(R.styleable.PrintView_textStyle, 0)
        a.recycle()

        minimumHeight = minHeight
        setMaxHeight(maxHeight)

        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        textView.setTextColor(textColor)
        textView.typeface = Typeface.defaultFromStyle(textStyle)
        addView(textView)
    }


    private fun reset() {
        showJob?.let {
            if (it.isActive) it.cancel()
        }

        textQueue.clear()
        lastLog = null
        isPrinting = false
        textView.text = null
    }

    private fun next() {
        if (isPrinting) {
            return
        }
        if (textQueue.isEmpty()) {
            return
        }
        val text = textQueue[0]
        textQueue.removeAt(0)
        val lastIndex = lastLog?.length ?: 1
        lastLog = if (lastLog == null) {
            text
        } else {
            "$lastLog \n$text"
        }
        print(lastLog, lastIndex)
    }

    private fun print(text: String?, len: Int) {
        if (text.isNullOrEmpty()) {
            return
        }
        isPrinting = true
        print(text.toCharArray(), len, text.length)
    }

    private fun print(text: CharArray, len: Int, size: Int) {
        if (len - 1 >= size) {
            // 打印完成当前的 就从队列里面取出来
            isPrinting = false
            next()
            return
        }

        textView.setText(text, 0, len)
        post { fullScroll(View.FOCUS_DOWN) }

        showJob = GlobalScope.launch(Dispatchers.Main) {
            delay(30)
            print(text, len + 1, size)
        }
    }

    private fun add(text: String?) {
        text?.let { textQueue.add(text) }
        next()
    }


    companion object {
        private const val TAG = "PrintView"

        private var worker: PrintView? = null
        fun init(worker: PrintView?) {
            this.worker = worker
        }

        fun print(log: String?) {
            print(TAG, log)
        }

        fun print(tag: String, log: String?) {
            worker?.add(log)
            LogUtils.i(tag, log)
        }

        fun reset() {
            worker?.reset()
        }
    }
}