package com.ishow.noah.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout

class ViewPropertiesLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        Log.i("yhy", "checkLayoutParams")
        return super.checkLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        Log.i("yhy", "generateLayoutParams111111")
        return super.generateLayoutParams(attrs)
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): LayoutParams {
        Log.i("yhy", "generateLayoutParams22222")
        return super.generateLayoutParams(lp)
    }


    class MyLayoutParams : LayoutParams {
        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs) {

        }

        constructor(width: Int, height: Int) : super(width, height) {

        }

        constructor(source: LayoutParams?) : super(source) {

        }
    }
}