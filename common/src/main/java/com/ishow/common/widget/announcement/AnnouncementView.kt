package com.ishow.common.widget.announcement

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher
import com.ishow.common.R
import com.ishow.common.extensions.inflate
import com.ishow.common.utils.UnitUtils
import com.ishow.common.widget.textview.MarqueeTextView
import java.util.*

class AnnouncementView : FrameLayout, ViewSwitcher.ViewFactory, View.OnClickListener {
    private lateinit var mTextSwitcher: TextSwitcher
    private lateinit var mData: MutableList<IAnnouncementData>

    private var mTextColor: Int = 0
    private var mTextSize: Float = 0F
    private var mTextLines: Int = 0
    private var mTextEllipsize: Int = 0
    private var mDelayTime: Long = 0L
    private var mTextSpacingAdd: Float = 0F
    private var mTextSpacingMultiplier: Float = 0F
    private var isMarqueeEnable: Boolean = false
    private var mOnAnnouncementChangedListener: OnAnnouncementChangedListener? = null
    private var textGravity: Int = Gravity.CENTER_VERTICAL

    @SuppressLint("HandlerLeak")
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (currentIndex >= mData.size - 1) {
                currentIndex = 0
            } else {
                currentIndex++
            }
            updateView()
            sendEmptyMessageDelayed(0, mDelayTime)
        }
    }

    var currentIndex: Int = 0
        private set

    var data: MutableList<IAnnouncementData>?
        get() = mData
        set(data) {
            if (data == null) {
                mData.clear()
            } else {
                mData = data
            }
            currentIndex = 0
            updateView()

            mHandler.removeCallbacksAndMessages(null)
            if (mData.size > 1) {
                mHandler.sendEmptyMessageDelayed(0, 1000)
            }
        }

    val currentData: IAnnouncementData?
        get() = if (mData.isEmpty()) null else mData[currentIndex]

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AnnouncementView)
        mTextColor = a.getColor(R.styleable.AnnouncementView_textColor, Color.LTGRAY)
        mTextSize = a.getDimensionPixelSize(R.styleable.AnnouncementView_textSize, UnitUtils.dip2px(12)).toFloat()
        mTextLines = a.getInt(R.styleable.AnnouncementView_textLines, -1)
        mTextEllipsize = a.getInt(R.styleable.AnnouncementView_textEllipsize, -1)
        mTextSpacingAdd = a.getDimensionPixelSize(R.styleable.AnnouncementView_textLineSpacingExtra, 0).toFloat()
        mTextSpacingMultiplier = a.getFloat(R.styleable.AnnouncementView_textLineSpacingMultiplier, 1.0f)
        isMarqueeEnable = a.getBoolean(R.styleable.AnnouncementView_marqueeEnable, false)
        mDelayTime = a.getInt(R.styleable.AnnouncementView_delayTime, DELAY_TIME).toLong()
        textGravity = a.getInt(R.styleable.AnnouncementView_textGravity, Gravity.CENTER_VERTICAL)
        val cancelEnable = a.getBoolean(R.styleable.AnnouncementView_cancelEnable, false)
        a.recycle()

        mData = ArrayList()
        inflate(R.layout.widget_announcement_view, true)

        mTextSwitcher = findViewById(R.id.switcher)
        mTextSwitcher.setFactory(this)
        mTextSwitcher.inAnimation = inAnimation()
        mTextSwitcher.outAnimation = outAnimation()
        mTextSwitcher.foregroundGravity = Gravity.CENTER

        val exit = findViewById<View>(R.id.exit)
        exit.visibility = if (cancelEnable) View.VISIBLE else View.GONE
        exit.setOnClickListener(this)
    }


    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE && mData.size > 1) {
            mHandler.sendEmptyMessageDelayed(0, mDelayTime)
        } else {
            mHandler.removeCallbacksAndMessages(null)
        }
    }

    override fun makeView(): View {
        val textView: TextView
        if (isMarqueeEnable) {
            textView = MarqueeTextView(context)
        } else {
            textView = TextView(context)
        }

        setInputEllipsize(textView)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        textView.setTextColor(mTextColor)
        textView.setLineSpacing(mTextSpacingAdd, mTextSpacingMultiplier)
        textView.gravity = textGravity
        if (mTextLines > 0) textView.setLines(mTextLines)
        return textView
    }

    fun setLineSpacing(add: Float, mult: Float) {
        mTextSpacingAdd = add
        mTextSpacingMultiplier = mult
        requestLayout()
    }

    private fun updateView() {
        if (mData.isEmpty()) {
            visibility = View.GONE
            return
        }

        visibility = View.VISIBLE
        val text = mData[currentIndex].title
        mTextSwitcher.setText(text)
        mOnAnnouncementChangedListener?.onChanged(mData[currentIndex], currentIndex)
    }


    /**
     * 定义从右侧进入的动画效果
     */
    private fun inAnimation(): Animation {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f
        )
        animation.duration = 500
        animation.interpolator = AccelerateInterpolator()
        return animation
    }

    /**
     * 定义从左侧退出的动画效果
     */
    private fun outAnimation(): Animation {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, -1.0f
        )
        animation.duration = 500
        animation.interpolator = AccelerateInterpolator()
        return animation
    }


    override fun onClick(v: View) {
        visibility = View.GONE
    }

    /**
     * 设置样式
     */
    private fun setInputEllipsize(view: TextView) {
        when (mTextEllipsize) {
            1 -> view.ellipsize = TextUtils.TruncateAt.START
            2 -> view.ellipsize = TextUtils.TruncateAt.MIDDLE
            3 -> view.ellipsize = TextUtils.TruncateAt.END
        }
    }

    fun setOnAnnouncementChangedListener(listener: OnAnnouncementChangedListener) {
        mOnAnnouncementChangedListener = listener
    }

    interface OnAnnouncementChangedListener {
        fun onChanged(data: IAnnouncementData, position: Int)
    }

    companion object {
        /**
         * 延迟时间
         */
        private const val DELAY_TIME = 5000
    }
}
