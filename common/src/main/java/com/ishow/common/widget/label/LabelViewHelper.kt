package com.ishow.common.widget.label

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log

import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.ishow.common.R
import kotlin.math.sin

class LabelViewHelper(
    private val labelView: ILabelView,
    private val context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) {

    private var mText: String? = null
    private var mDistance: Int = 0
    private var mHeight: Int = 0
    private var mBackgroundColor: Int = 0
    private var mStrokeColor: Int = 0
    private var mStrokeWidth: Int = 0
    private var mTextSize: Int = 0
    private var mTextStyle: Int = 0
    private var mTextColor: Int = 0
    private var mLabelGravity: Int = 0
    private var mAlpha: Int = 0
    private var isEnable: Boolean = false

    private val mRectPaint: Paint
    private val mRectStrokePaint: Paint
    private val mTextPaint: Paint
    // simulator
    private val mRectPath: Path
    private val mTextPath: Path
    private val mTextBound: Rect

    var labelHeight: Int
        get() = mHeight
        set(@DimenRes heightRes) {
            val height = context.resources.getDimensionPixelOffset(heightRes)
            if (mHeight != height) {
                mHeight = height
                invalidate()
            }
        }

    var labelDistance: Int
        get() = mDistance
        set(@DimenRes distanceRes) {
            val distance = context.resources.getDimensionPixelOffset(distanceRes)
            if (mDistance != distance) {
                mDistance = distance
                invalidate()
            }
        }

    var isLabelEnable: Boolean
        get() = isEnable
        set(visual) {
            if (isEnable != visual) {
                isEnable = visual
                invalidate()
            }
        }

    var labelGravity: Int
        get() = mLabelGravity
        set(@Gravity gravity) {
            if (mLabelGravity != gravity) {
                mLabelGravity = gravity
                invalidate()
            }
        }

    var labelTextColor: Int
        get() = mTextColor
        set(@ColorInt textColor) {
            if (mTextColor != textColor) {
                mTextColor = textColor
                invalidate()
            }
        }

    var labelBackgroundColor: Int
        get() = mBackgroundColor
        set(@ColorInt backgroundColor) {
            if (mBackgroundColor != backgroundColor) {
                mBackgroundColor = backgroundColor
                invalidate()
            }
        }

    var labelText: String?
        get() = mText
        set(text) {
            if (!TextUtils.equals(text, mText)) {
                mText = text
                if (!TextUtils.isEmpty(mText)) {
                    mTextPaint.getTextBounds(mText, 0, mText!!.length, mTextBound)
                }
                invalidate()
            }
        }

    var labelTextSize: Int
        get() = mTextSize
        set(@DimenRes textSizeRes) {
            val textSize = context.resources.getDimensionPixelSize(textSizeRes)
            if (mTextSize != textSize) {
                mTextSize = textSize
                invalidate()
            }
        }


    private val measuredWidth: Int
        get() = labelView.view.measuredWidth

    private val measuredHeight: Int
        get() = labelView.view.measuredHeight

    private val defaultTextSize: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.J_title)

    private val defaultHeight: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.dp_30)

    private val defaultDistance: Int
        get() = context.resources.getDimensionPixelOffset(R.dimen.dp_25)

    private val defaultBackgroundColor: Int
        get() = ContextCompat.getColor(context, R.color.color_primary)

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.LabelView, defStyleAttr, 0)
        mText = a.getString(R.styleable.LabelView_text)
        mTextSize = a.getDimensionPixelSize(R.styleable.LabelView_textSize, defaultTextSize)
        mTextColor = a.getColor(R.styleable.LabelView_textColor, Color.WHITE)
        mTextStyle = a.getInt(R.styleable.LabelView_textStyle, 0)

        mStrokeWidth = a.getDimensionPixelSize(R.styleable.LabelView_strokeWidth, 0)
        mStrokeColor = a.getColor(R.styleable.LabelView_strokeColor, Color.TRANSPARENT)

        mHeight = a.getDimensionPixelSize(R.styleable.LabelView_labelHeight, defaultHeight)
        mDistance = a.getDimensionPixelSize(R.styleable.LabelView_labelDistance, defaultDistance)
        mBackgroundColor = a.getColor(R.styleable.LabelView_backgroundColor, defaultBackgroundColor)
        isEnable = a.getBoolean(R.styleable.LabelView_labelEnable, true)
        mLabelGravity = a.getInteger(R.styleable.LabelView_labelGravity, Gravity.RIGHT_TOP)
        a.recycle()

        mRectPaint = Paint()
        mRectPaint.isDither = true
        mRectPaint.isAntiAlias = true
        mRectPaint.style = Paint.Style.FILL

        mRectStrokePaint = Paint()
        mRectStrokePaint.isDither = true
        mRectStrokePaint.isAntiAlias = true
        mRectStrokePaint.style = Paint.Style.STROKE
        mRectStrokePaint.color = mStrokeColor
        mRectStrokePaint.strokeWidth = mStrokeWidth.toFloat()

        mRectPath = Path()
        mRectPath.reset()

        mTextPath = Path()
        mTextPath.reset()

        mTextBound = Rect()
        mTextPaint = Paint()
        mTextPaint.isDither = true
        mTextPaint.isAntiAlias = true
        mTextPaint.strokeJoin = Paint.Join.ROUND
        mTextPaint.strokeCap = Paint.Cap.SQUARE
        mTextPaint.textSize = mTextSize.toFloat()
        mTextPaint.color = mTextColor
        mTextPaint.typeface = Typeface.defaultFromStyle(mTextStyle)
        if (!TextUtils.isEmpty(mText)) {
            mTextPaint.getTextBounds(mText, 0, mText!!.length, mTextBound)
        }

        // 检测有效性
        checkValid()
    }

    /**
     * 检测是否有效
     */
    private fun checkValid() {
    }

    fun onDraw(canvas: Canvas) {
        if (!isEnable || mText == null) {
            return
        }
        val width = measuredWidth
        val height = measuredHeight

        val actualDistance = mDistance + mHeight / 2f
        calculatePath(width, height)

        // 画背景
        mRectPaint.color = mBackgroundColor
        if (mAlpha != 0) {
            mRectPaint.alpha = mAlpha
        }
        canvas.drawPath(mRectPath, mRectPaint)

        // 画描边
        if (mStrokeWidth > 0) {
            canvas.drawPath(mRectPath, mRectStrokePaint)
        }

        // 画文本
        val fontMetrics = mTextPaint.fontMetricsInt
        var x = (actualDistance.toDouble() / sin(Math.PI * 45 / 180) / 2.0 - mTextBound.width() / 2).toFloat()
        if (x < 0) x = 0f

        val y = (fontMetrics.top - fontMetrics.bottom) / 2f - fontMetrics.top
        canvas.drawTextOnPath(mText!!, mTextPath, x, y, mTextPaint)
    }

    /**
     * 计算图形
     */
    private fun calculatePath(width: Int, height: Int) {
        val startX = (width - mDistance - mHeight).toFloat()
        val startY = (height - mDistance - mHeight).toFloat()
        val middle = mHeight / 2f

        when (mLabelGravity) {
            Gravity.LEFT_TOP -> {
                mRectPath.reset()
                mRectPath.moveTo(0f, mDistance.toFloat())
                mRectPath.lineTo(mDistance.toFloat(), 0f)
                mRectPath.lineTo((mDistance + mHeight).toFloat(), 0f)
                mRectPath.lineTo(0f, (mDistance + mHeight).toFloat())
                mRectPath.close()

                mTextPath.reset()
                mTextPath.moveTo(0f, mDistance + middle)
                mTextPath.lineTo(mDistance + middle, 0f)
                mTextPath.close()
            }
            Gravity.RIGHT_TOP -> {
                mRectPath.reset()
                mRectPath.moveTo(startX, 0f)
                mRectPath.lineTo(startX + mHeight, 0f)
                mRectPath.lineTo(width.toFloat(), mDistance.toFloat())
                mRectPath.lineTo(width.toFloat(), (mDistance + mHeight).toFloat())
                mRectPath.close()

                mTextPath.reset()
                mTextPath.moveTo(startX + middle, 0f)
                mTextPath.lineTo(width.toFloat(), mDistance + middle)
                mTextPath.close()
            }
            Gravity.LEFT_BOTTOM -> {
                mRectPath.reset()
                mRectPath.moveTo(0f, startY)
                mRectPath.lineTo((mDistance + mHeight).toFloat(), height.toFloat())
                mRectPath.lineTo(mDistance.toFloat(), height.toFloat())
                mRectPath.lineTo(0f, startY + mHeight)
                mRectPath.close()

                mTextPath.reset()
                mTextPath.moveTo(0f, startY + middle)
                mTextPath.lineTo(mDistance + middle, height.toFloat())
                mTextPath.close()
            }
            Gravity.RIGHT_BOTTOM -> {
                mRectPath.reset()
                mRectPath.moveTo(startX, height.toFloat())
                mRectPath.lineTo(width.toFloat(), startY)
                mRectPath.lineTo(width.toFloat(), startY + mHeight)
                mRectPath.lineTo(startX + mHeight, height.toFloat())
                mRectPath.close()

                mTextPath.reset()
                mTextPath.moveTo(startX + middle, height.toFloat())
                mTextPath.lineTo(width.toFloat(), startY + middle)
                mTextPath.close()
            }
        }
    }


    fun setLabelBackgroundAlpha(alpha: Int) {
        if (mAlpha != alpha) {
            mAlpha = alpha
            invalidate()
        }
    }

    fun setLabelTextStyle(@TextStyle textStyle: Int) {
        if (mTextStyle == textStyle) {
            Log.i(TAG, "setLabelTextStyle: is same Style")
            return
        }

        mTextStyle = textStyle
        mTextPaint.typeface = Typeface.defaultFromStyle(mTextStyle)
        invalidate()
    }

    fun setLabelStrokeColor(@ColorInt color: Int) {
        if (mStrokeColor != color) {
            mStrokeColor = color
            mRectStrokePaint.color = mStrokeColor
            invalidate()
        }
    }

    fun setLabelStrokeWidth(@DimenRes widthRes: Int) {
        val width = context.resources.getDimensionPixelOffset(widthRes)
        if (mStrokeWidth != width) {
            mStrokeWidth = width
            mRectStrokePaint.strokeWidth = mStrokeWidth.toFloat()
            invalidate()
        }
    }

    private fun invalidate() {
        labelView.view.invalidate()
    }


    @IntDef(Gravity.LEFT_TOP, Gravity.RIGHT_TOP, Gravity.LEFT_BOTTOM, Gravity.RIGHT_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Gravity {
        companion object {
            const val LEFT_TOP = 1
            const val RIGHT_TOP = 2
            const val LEFT_BOTTOM = 3
            const val RIGHT_BOTTOM = 4
        }
    }


    @IntDef(TextStyle.NORMAL, TextStyle.BOLD, TextStyle.ITALIC, TextStyle.BOLD_ITALIC)
    @Retention(AnnotationRetention.SOURCE)
    annotation class TextStyle {
        companion object {
            const val NORMAL = 0
            const val BOLD = 1
            const val ITALIC = 2
            const val BOLD_ITALIC = 3
        }
    }

    companion object {
        private const val TAG = "LabelViewHelper"
    }
}