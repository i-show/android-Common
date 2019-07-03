package com.ishow.common.widget.scale

import android.view.View
import android.view.ViewGroup

class ScaleHelper(var widthRatio: Int, var heightRatio: Int) {



    fun onMeasure(layoutParams: ViewGroup.LayoutParams?, widthSpec: Int, heightSpec: Int):IntArray {
        return if (layoutParams != null && (layoutParams.width > 0 || layoutParams.height > 0)) {
            measureByLayoutParams(layoutParams, widthSpec, heightSpec)
        } else {
            measureByMeasureSpec(widthSpec, heightSpec)
        }
    }

    fun setScale(width:Int, height:Int){
        widthRatio = width
        heightRatio = height
    }

    /**
     * 通过LayoutParams 来获取值 进行计算
     * 目的:父类为RelativeLayout 时候MeasureSpec 会错乱
     */
    private fun measureByLayoutParams(layoutParams: ViewGroup.LayoutParams, widthSpec: Int, heightSpec: Int): IntArray {
        var widthMeasureSpec = widthSpec
        var heightMeasureSpec = heightSpec
        if (layoutParams.width > 0) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.width, View.MeasureSpec.EXACTLY)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getHeight(layoutParams.width), View.MeasureSpec.EXACTLY)
        } else if (layoutParams.height > 0) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWidth(layoutParams.height), View.MeasureSpec.EXACTLY)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(layoutParams.height, View.MeasureSpec.EXACTLY)
        }
        return intArrayOf(widthMeasureSpec, heightMeasureSpec)
    }

    /**
     * 最原始的measure方法
     */
    private fun measureByMeasureSpec(widthSpec: Int, heightSpec: Int): IntArray {
        var widthMeasureSpec = widthSpec
        var heightMeasureSpec = heightSpec
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (widthMode == View.MeasureSpec.EXACTLY) {
            val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(getHeight(widthSize), View.MeasureSpec.EXACTLY)
        } else if (heightMode == View.MeasureSpec.EXACTLY) {
            val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(getWidth(heightSize), View.MeasureSpec.EXACTLY)
        }
        return intArrayOf(widthMeasureSpec, heightMeasureSpec)
    }

    private fun getHeight(width: Int): Int {
        return width * heightRatio / widthRatio
    }

    private fun getWidth(height: Int): Int {
        return height * widthRatio / heightRatio
    }
}