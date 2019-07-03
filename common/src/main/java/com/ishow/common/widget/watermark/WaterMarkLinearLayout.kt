package com.ishow.common.widget.watermark

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.ishow.common.R

/**
 * Created by yuhaiyang on 2018/1/23.
 * 带有水印的LinearLayout
 */

class WaterMarkLinearLayout : LinearLayout, IWaterMark {

    private lateinit var mWaterMarkHelp: WaterMarkHelp

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        val params = WaterMarkHelp.Params()
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkLinearLayout)
        params.text = a.getString(R.styleable.WaterMarkLinearLayout_text)
        params.textSize = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_textSize, WaterMarkHelp.getDefaultTextSize(context))
        params.textColor = a.getColor(R.styleable.WaterMarkLinearLayout_textColor, WaterMarkHelp.getDefaultTextColor(context))
        params.alpha = a.getFloat(R.styleable.WaterMarkLinearLayout_waterMarkAlpha, WaterMarkHelp.defaultAlpha)
        params.enable = a.getBoolean(R.styleable.WaterMarkLinearLayout_waterMarkEnable, true)
        params.angle = a.getInt(R.styleable.WaterMarkLinearLayout_waterMarkAngle, WaterMarkHelp.defaultAngle)
        params.topPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_topPadding, WaterMarkHelp.getDefaultPadding(context))
        params.bottomPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_bottomPadding, WaterMarkHelp.getDefaultPadding(context))
        params.startPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_startPadding, WaterMarkHelp.getDefaultPadding(context))
        params.endPadding = a.getDimensionPixelSize(R.styleable.WaterMarkLinearLayout_endPadding, WaterMarkHelp.getDefaultPadding(context))
        a.recycle()
        mWaterMarkHelp = WaterMarkHelp()
        mWaterMarkHelp.init(this, params)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        mWaterMarkHelp.draw(canvas, measuredWidth, measuredHeight)
    }

    override fun setWaterMarkEnable(enable: Boolean) {
        mWaterMarkHelp.setWaterMarkEnable(enable)
    }
}
