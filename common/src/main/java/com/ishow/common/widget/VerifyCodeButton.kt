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

package com.ishow.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.net.http.SslCertificate.restoreState
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.ishow.common.R
import com.ishow.common.utils.StorageUtils

/**
 * Created by yuhaiyang on 2019/6/27.
 * 发送验证码的button
 */
class VerifyCodeButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var mProgressBar: ProgressBar
    private var mDisplayView: TextView
    /**
     * 时间监听
     */
    private var mTimingListener: OnTimingListener? = null

    private var mTextStr: String? = null
    private val mTextSize: Int
    private var mTextColor: ColorStateList? = null
    /**
     * 正在倒计时时候展示的文案
     */
    private val mTimingTextStr: String?

    /**
     * 当前时间
     */
    private var mCurrentTime: Int = 0
    private var mMaxTime: Int = 0
    /**
     * 当前状态
     */
    private var mStatus = STATE_IDLE
    private val mStatusKey: String

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                HANDLER_TIME -> handleTiming()
                HANDLER_RESET_TIME -> handleReset()
            }
        }
    }

    /**
     * 获取默认的布局参数
     */
    private val defaultParam: LayoutParams
        get() = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

    /**
     * 默认padding
     */
    private val defaultPadding: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.dp_3)

    /**
     * 默认边距
     */
    private val defaultTextSize: Int
        get() = context.resources.getDimensionPixelSize(R.dimen.H_title)

    /**
     * 获取默认的颜色值
     */
    private val defaultTextColor: ColorStateList
        get() = ContextCompat.getColorStateList(context, R.color.text_grey_light_normal)!!


    init {
        isClickable = true

        val a = context.obtainStyledAttributes(attrs, R.styleable.VerifyCodeButton)
        val padding = a.getDimensionPixelSize(R.styleable.VerifyCodeButton_android_padding, defaultPadding)
        mTextStr = a.getString(R.styleable.VerifyCodeButton_text)
        mTextSize = a.getDimensionPixelSize(R.styleable.VerifyCodeButton_textSize, defaultTextSize)
        mTextColor = a.getColorStateList(R.styleable.VerifyCodeButton_textColor)
        mTimingTextStr = a.getString(R.styleable.VerifyCodeButton_timingText)
        a.recycle()

        if (mTextColor == null) {
            mTextColor = defaultTextColor
        }

        if (mTextStr.isNullOrEmpty()) {
            mTextStr = resources.getString(R.string.get_verify_code)
        }

        setPadding(padding, padding, padding, padding)

        mProgressBar = getProgressBar()
        mProgressBar.visibility = View.INVISIBLE
        addView(mProgressBar)

        mDisplayView = getDisplayView()
        mDisplayView.visibility = View.VISIBLE
        addView(mDisplayView)

        mMaxTime = DEFAULT_VERIFY_MAX_TIME

        val contextName = context.javaClass.name
        mStatusKey = contextName.replace(".", "_") + "_" + id
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mDisplayView.isEnabled = enabled
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        restoreState()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mHandler.removeMessages(HANDLER_TIME)
    }

    /**
     * 显示Loading
     */
    fun showLoading() {
        mStatus = STATE_SENDING
        mTimingListener?.onSending()
        mProgressBar.visibility = View.VISIBLE
        mDisplayView.visibility = View.INVISIBLE
        isClickable = false
    }

    /**
     * 开始计时
     */
    @JvmOverloads
    fun startTiming(maxTime: Int = DEFAULT_VERIFY_MAX_TIME) {
        startTiming(maxTime, maxTime)
    }

    /**
     * 开始计时
     */
    private fun startTiming(maxTime: Int, currentTime: Int) {
        isClickable = false
        mMaxTime = maxTime
        this.mCurrentTime = currentTime
        mStatus = STATE_TIMING
        mProgressBar.visibility = View.INVISIBLE
        mDisplayView.visibility = View.VISIBLE
        mHandler.sendEmptyMessage(HANDLER_TIME)

        val status = Status()
        status.startDate = System.currentTimeMillis()
        status.remainTime = currentTime
        status.maxTime = maxTime

        StorageUtils.with(context)
            .param(mStatusKey, Gson().toJson(status))
            .save()
    }

    /**
     * 重新计时
     */
    fun reset() {
        mHandler.sendEmptyMessage(HANDLER_RESET_TIME)
        StorageUtils.with(context)
            .key(mStatusKey)
            .remove()
    }

    /**
     * 添加时间监听
     */
    fun setOnTimingListener(listener: OnTimingListener?) {
        mTimingListener = listener
    }

    /**
     * 获取当前时间
     */
    fun getCurrentTime(): Int = mCurrentTime

    /**
     * 是否正在倒计时
     */
    fun isTiming(): Boolean = mCurrentTime > 0

    /**
     * 获取时间显示
     */
    private fun getDisplayView(): TextView {
        val padding = defaultPadding
        val textView = TextView(context)
        textView.includeFontPadding = false
        textView.layoutParams = defaultParam
        textView.gravity = Gravity.CENTER
        textView.text = mTextStr
        textView.setTextColor(mTextColor)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize.toFloat())
        textView.setPadding(padding * 2, padding, padding * 2, padding)
        return textView
    }

    /**
     * 获取进度条
     */
    private fun getProgressBar(): ProgressBar {
        val padding = defaultPadding
        val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleSmall)
        progressBar.layoutParams = defaultParam
        progressBar.setPadding(padding, padding, padding, padding)
        return progressBar
    }

    private fun handleTiming() {
        mCurrentTime--
        if (mCurrentTime <= 0) {
            mHandler.sendEmptyMessage(HANDLER_RESET_TIME)
        } else {
            mTimingListener?.onTiming(mCurrentTime)
            updateTiming()
            mHandler.sendEmptyMessageDelayed(HANDLER_TIME, 1000)
        }
    }

    private fun handleReset() {
        mStatus = STATE_IDLE
        mHandler.removeMessages(HANDLER_TIME)

        mDisplayView.text = mTextStr
        mDisplayView.visibility = View.VISIBLE
        mProgressBar.visibility = View.INVISIBLE
        mCurrentTime = -1
        isClickable = true
        mTimingListener?.onTimingEnd()
    }

    /**
     * 更新计时器
     */
    private fun updateTiming() {
        when {
            mTimingTextStr.isNullOrEmpty() -> {
                mDisplayView.text = context.getString(R.string.second_timing, mCurrentTime)
            }
            mTimingTextStr.contains("%d") -> {
                mDisplayView.text = String.format(mTimingTextStr, mCurrentTime)
            }
            mTimingTextStr.contains("%s") -> {
                mDisplayView.text = String.format(mTimingTextStr, mCurrentTime.toString())
            }
            else -> throw IllegalStateException("timingText must %d or %s")
        }
    }

    /**
     * 重新展示的时候回复当前状态
     */
    private fun restoreState() {
        val lastStatus = StorageUtils.with(context)
            .key(mStatusKey)
            .get()

        if (lastStatus.isEmpty()) {
            return
        }
        val status = Gson().fromJson(lastStatus, Status::class.javaObjectType)

        val remainTime = status.remainTime - (System.currentTimeMillis() - status.startDate).toInt() / 1000
        if (remainTime > 0) {
            startTiming(status.maxTime, remainTime)
        } else {
            StorageUtils.with(context)
                .key(mStatusKey)
                .remove()
        }
    }

    /**
     * 时间监听
     */
    interface OnTimingListener {
        /**
         * 正在发送验证码
         */
        fun onSending()

        /**
         * 正在倒计时
         *
         * @param time 当前时间
         */
        fun onTiming(time: Int)

        /**
         * 倒计时结束
         */
        fun onTimingEnd()
    }


    class Status {
        /**
         * 开始计时的日期
         */
        var startDate: Long = 0
        /**
         * 剩余时间
         */
        var remainTime: Int = 0
        /**
         * 最大时间
         */
        var maxTime: Int = 0
    }

    companion object {
        /**
         * 最大时间
         */
        private const val DEFAULT_VERIFY_MAX_TIME = 60
        /**
         * 开始计时
         */
        private const val HANDLER_TIME = 1000
        /**
         * 重新计时
         */
        private const val HANDLER_RESET_TIME = 1001

        /**
         * 空闲状态
         */
        private const val STATE_IDLE = 1000
        /**
         * 发送状态
         */
        private const val STATE_SENDING = 1001
        /**
         * 计时状态
         */
        private const val STATE_TIMING = 1002
    }
}
