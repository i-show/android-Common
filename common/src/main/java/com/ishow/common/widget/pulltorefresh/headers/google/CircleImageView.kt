package com.ishow.common.widget.pulltorefresh.headers.google

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.View
import android.view.animation.Animation

import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat

/**
 * Private class created to work around issues with AnimationListeners being
 * called before the animation is actually complete and support shadows on older
 * platforms.
 */
internal class CircleImageView(context: Context, color: Int) : AppCompatImageView(context) {

    private var mListener: Animation.AnimationListener? = null
    var shadowRadius: Int = 0

    init {
        val density = getContext().resources.displayMetrics.density
        val shadowYOffset = (density * Y_OFFSET).toInt()
        val shadowXOffset = (density * X_OFFSET).toInt()

        shadowRadius = (density * SHADOW_RADIUS).toInt()

        val circle: ShapeDrawable
        if (elevationSupported()) {
            circle = ShapeDrawable(OvalShape())
            ViewCompat.setElevation(this, SHADOW_ELEVATION * density)
        } else {
            val oval = OvalShadow(shadowRadius)
            circle = ShapeDrawable(oval)
            setLayerType(View.LAYER_TYPE_SOFTWARE, circle.paint)
            circle.paint.setShadowLayer(
                shadowRadius.toFloat(), shadowXOffset.toFloat(), shadowYOffset.toFloat(),
                KEY_SHADOW_COLOR
            )
            val padding = shadowRadius
            // set padding so the inner image sits correctly within the shadow.
            setPadding(padding, padding, padding, padding)
        }
        circle.paint.color = color
        ViewCompat.setBackground(this, circle)
    }

    private fun elevationSupported(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= 21
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!elevationSupported()) {
            setMeasuredDimension(measuredWidth + shadowRadius * 2, measuredHeight + shadowRadius * 2)
        }
    }

    fun setAnimationListener(listener: Animation.AnimationListener) {
        mListener = listener
    }

    public override fun onAnimationStart() {
        super.onAnimationStart()
        if (mListener !=
            null
        ) {
            mListener!!.onAnimationStart(animation)
        }
    }

    public override fun onAnimationEnd() {
        super.onAnimationEnd()
        if (mListener != null) {
            mListener!!.onAnimationEnd(animation)
        }
    }

    /**
     * Update the background color of the circle image view.
     *
     * @param colorRes Id of a color resource.
     */
    fun setBackgroundColorRes(colorRes: Int) {
        setBackgroundColor(ContextCompat.getColor(context, colorRes))
    }

    override fun setBackgroundColor(color: Int) {
        if (background is ShapeDrawable) {
            (background as ShapeDrawable).paint.color = color
        }
    }

    private inner class OvalShadow internal constructor(shadowRadius: Int) : OvalShape() {
        private var radialGradient: RadialGradient? = null
        private val shadowPaint: Paint = Paint()

        init {
            this@CircleImageView.shadowRadius = shadowRadius
            updateRadialGradient(rect().width().toInt())
        }

        override fun onResize(width: Float, height: Float) {
            super.onResize(width, height)
            updateRadialGradient(width.toInt())
        }

        override fun draw(canvas: Canvas, paint: Paint) {
            val viewWidth = this@CircleImageView.width
            val viewHeight = this@CircleImageView.height
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (viewWidth / 2).toFloat(),
                shadowPaint
            )
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (viewWidth / 2 - shadowRadius).toFloat(),
                paint
            )
        }

        private fun updateRadialGradient(diameter: Int) {
            radialGradient = RadialGradient(
                (diameter / 2).toFloat(), (diameter / 2).toFloat(),
                shadowRadius.toFloat(), intArrayOf(FILL_SHADOW_COLOR, Color.TRANSPARENT), null, Shader.TileMode.CLAMP
            )
            shadowPaint.shader = radialGradient
        }
    }

    companion object {

        private const val KEY_SHADOW_COLOR = 0x1E000000
        private const val FILL_SHADOW_COLOR = 0x3D000000
        // PX
        private const val X_OFFSET = 0f
        private const val Y_OFFSET = 1.75f
        private const val SHADOW_RADIUS = 3.5f
        private const val SHADOW_ELEVATION = 4
    }
}
