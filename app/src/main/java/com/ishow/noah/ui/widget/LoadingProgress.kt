package com.ishow.noah.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.ishow.noah.R
import kotlin.math.min
import kotlin.math.max


/**
 * Created by yuhaiyang on 2020/8/21.
 * Loading 进度条
 */
class LoadingProgress : View {
    private val wavePaint = Paint()
    private val clearPaint = Paint()
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private var waveStepSize: Int = 0
    private var waveHeight: Int = 0

    private var wave1MoveX: Int = 0
    private var wave2MoveX: Int = 0

    private var maskBitmap: Bitmap
    private var bgBitmap: Bitmap

    private lateinit var waveBitmap: Bitmap
    private lateinit var waveCanvas: Canvas
    private val waveXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var progress: Int = 0

    init {
        wavePaint.isAntiAlias = true
        wavePaint.isDither = true
        wavePaint.color = Color.parseColor("#DDFFFFFF")
        wavePaint.style = Paint.Style.FILL

        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        wave2MoveX = waveStepSize / 2

        maskBitmap = BitmapFactory.decodeResource(resources, R.drawable.loading_mask)
        bgBitmap = BitmapFactory.decodeResource(resources, R.drawable.loading_image)

        waveStepSize = (bgBitmap.width * 1.6).toInt()
    }

    init {
        startAnimation()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 画进度
        waveBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        waveCanvas = Canvas(waveBitmap)

        //控件的宽高
        mWidth = width
        mHeight = height


        //水波的高度
        waveHeight = (mHeight * 0.1).toInt()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = bgBitmap.width
        val height = bgBitmap.height
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 画背景
        canvas.drawBitmap(bgBitmap, 0F, 0F, wavePaint)
        waveCanvas.drawPaint(clearPaint)
        drawWave(waveCanvas, wave1MoveX)
        drawWave(waveCanvas, wave2MoveX)

        val layerCount = canvas.saveLayer(0F, 0F, width.toFloat(), height.toFloat(), null)
        canvas.drawBitmap(maskBitmap, 0F, 0F, wavePaint)
        wavePaint.xfermode = waveXfermode
        canvas.drawBitmap(waveBitmap, 0F, 0F, wavePaint)
        wavePaint.xfermode = null
        canvas.restoreToCount(layerCount)
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    private fun startAnimation() {
        val half = waveStepSize / 2
        val valueAnimator = ValueAnimator.ofInt(0, waveStepSize)
        valueAnimator.duration = 2000
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener {
            wave1MoveX = it.animatedValue as Int
            wave2MoveX = if (half + wave1MoveX > waveStepSize) {
                half + wave1MoveX - waveStepSize
            } else {
                half + wave1MoveX
            }
            invalidate()
        }
        valueAnimator.start()
    }

    private fun drawWave(canvas: Canvas, movedX: Int) {
        val nowProgress = progress / 100F
        val y = 1 - max(0.2F, min(0.96F, nowProgress))
        val path = Path()
        path.reset()
        path.moveTo((-waveStepSize + movedX).toFloat(), mHeight * y)
        var i: Int = -waveStepSize

        while (i < width + waveStepSize) {
            path.rQuadTo((waveStepSize / 4F), (-waveHeight).toFloat(), waveStepSize / 2F, 0F)
            path.rQuadTo((waveStepSize / 4F), waveHeight.toFloat(), (waveStepSize / 2F), 0F)
            i += waveStepSize
        }
        //绘制封闭的区域
        path.lineTo(mWidth.toFloat(), mHeight.toFloat())
        path.lineTo(0F, mHeight.toFloat())
        path.close()
        canvas.drawPath(path, wavePaint)
    }
}