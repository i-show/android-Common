package com.ishow.common.widget.watermark

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.RequiresApi
import com.ishow.common.R
import com.ishow.common.utils.log.LogUtils

class WaterMarkView : View {
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val params = WaterMarkHelp.Params()
        val a = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkView)
        params.text = a.getString(R.styleable.WaterMarkView_text)
        params.textSize = a.getDimensionPixelSize(R.styleable.WaterMarkView_textSize, WaterMarkHelp.getDefaultTextSize(context))
        params.textColor = a.getColor(R.styleable.WaterMarkView_textColor, WaterMarkHelp.getDefaultTextColor(context))
        params.alpha = a.getFloat(R.styleable.WaterMarkView_waterMarkAlpha, WaterMarkHelp.defaultAlpha)
        params.enable = a.getBoolean(R.styleable.WaterMarkView_waterMarkEnable, true)
        params.angle = a.getInt(R.styleable.WaterMarkView_waterMarkAngle, WaterMarkHelp.defaultAngle)
        params.topPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_topPadding, WaterMarkHelp.getDefaultPadding(context))
        params.bottomPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_bottomPadding, WaterMarkHelp.getDefaultPadding(context))
        params.startPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_startPadding, WaterMarkHelp.getDefaultPadding(context))
        params.endPadding = a.getDimensionPixelSize(R.styleable.WaterMarkView_endPadding, WaterMarkHelp.getDefaultPadding(context))
        a.recycle()
        mWaterMarkHelp = WaterMarkHelp()
        mWaterMarkHelp.init(this, params)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mWaterMarkHelp.draw(canvas, measuredWidth, measuredHeight)
    }


    companion object {
        private const val TAG = "WaterMarkView"

        /**
         * 设置是否可见
         */
        fun setVisibility(activity: Activity?, visibility: Int) {
            if (activity == null || activity.isFinishing) {
                return
            }
            val markView = activity.findViewById<WaterMarkView>(R.id.water_mark_view) ?: return
            markView.visibility = visibility
        }

        /**
         * 添加到attachView
         */
        fun attachToActivity(activity: Activity?) {
            if (activity == null || activity.isFinishing) {
                return
            }
            var markView: WaterMarkView? = activity.findViewById(R.id.water_mark_view)
            if (markView != null) {
                LogUtils.i(TAG, "already add")
                return
            }
            markView = WaterMarkView(activity)
            markView.id = R.id.water_mark_view
            val root = activity.findViewById<ViewGroup>(android.R.id.content)

            root.addView(markView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        }

        /**
         * 从Activity中移除
         */
        fun detachedFromActivity(activity: Activity?) {
            if (activity == null || activity.isFinishing) {
                return
            }
            val markView = activity.findViewById<WaterMarkView>(R.id.water_mark_view) ?: return
            val root = activity.findViewById<ViewGroup>(android.R.id.content)
            root.removeView(markView)
        }
    }
}
