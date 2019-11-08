package com.ishow.common.widget

import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import com.ishow.common.utils.log.LogUtils
import kotlinx.coroutines.*

/**
 * Created by yuhaiyang on 2019-11-05.
 * 输出log的View
 */
class PrintView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    TextView(context, attrs, defStyleAttr) {

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


    init {
        movementMethod = ScrollingMovementMethod.getInstance()
    }

    fun reset() {
        showJob?.let {
            if (it.isActive) it.cancel()
        }

        textQueue.clear()
        lastLog = null
        isPrinting = false
        text = null
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

        setText(text, 0, len)
        showJob = GlobalScope.launch(Dispatchers.Main) {
            delay(50)
            print(text, len + 1, size)
        }
    }

    private fun add(text: String?) {
        text?.let { textQueue.add(text) }
        next()
    }


    companion object {
        private const val TAG = "PrintView"

        var printer: PrintView? = null

        fun print(log: String?) {
            printer?.add(log)
            LogUtils.i(TAG, log)
        }
    }
}