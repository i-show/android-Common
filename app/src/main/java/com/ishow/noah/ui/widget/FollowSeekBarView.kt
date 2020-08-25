package com.ishow.noah.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.*
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.ishow.common.extensions.*
import com.ishow.noah.R

/**
 * Created by yuhaiyang on 2020/8/22.
 *
 */
class FollowSeekBarView : FrameLayout {
    private var thumbStartX: Int = 0
    private var seekBarTotalX = 0
    private var totalX = 0
    private var startX = 0

    private var seekBar: SeekBar? = null
    private lateinit var timeTv: TextView
    private lateinit var arrowView: ImageView

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()
        val view = inflate(R.layout.layout_with_seekbar_position)
        timeTv = view.findViewById(R.id.timeTv)
        arrowView = view.findViewById(R.id.arrowIv)
        addView(view)
    }

    fun onProgressChanged(progress: Int) {
        computePosition(progress)
    }

    fun withSeekBar(seekBar: SeekBar) {
        this.seekBar = seekBar

        if (seekBar.width <= 0) {
            seekBar.afterMeasured { compute() }
        } else {
            compute()
        }
    }

    private fun compute() {
        val bar = seekBar ?: return
        val barWidth = bar.width
        seekBarTotalX = barWidth - bar.paddingStart - bar.paddingEnd
        thumbStartX = bar.paddingLeft

        totalX = barWidth - width
        startX = width / 2
        computePosition(bar.progress)
    }


    @Suppress("CascadeIf")
    private fun computePosition(progress: Int) {
        val bar = seekBar ?: return
        val barWidth = bar.width
        val seekBarX = thumbStartX + seekBarTotalX * progress / 100F
        val positionX = startX + totalX * progress / 100F
        val idealX = (seekBarX - positionX) + totalX * progress / 100
        // bar.marginStart 设置的marginStart的值。如果还有其他控件则进行累加即可
        // bar.marginEnd 设置marginEnd的值。如果还有其他控件则需要进行累加

        if (idealX < 0) {
            translationX = 0F + bar.marginStart
            arrowView.translationX = idealX
        } else if (idealX + width > barWidth) {
            val x = idealX + width - barWidth
            translationX = totalX.toFloat() + bar.marginEnd
            arrowView.translationX = x
        } else {
            arrowView.translationX = 0F
            translationX = idealX + bar.marginStart
        }
    }
}