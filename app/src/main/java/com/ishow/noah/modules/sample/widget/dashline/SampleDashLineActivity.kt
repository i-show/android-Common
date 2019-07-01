package com.ishow.noah.modules.sample.widget.dashline

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_sample_widget_dash_line.*


/**
 * Created by yuhaiyang on 2018-09-06.
 * 破折线的测试
 */
class SampleDashLineActivity : AppBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_widget_dash_line)
        colorSet.setOnClickListener(this)
        gapSet.setOnClickListener(this)
        widthSet.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.colorSet -> {
                var color = colorInput.text.toString()
                if (!color.startsWith("#")) {
                    color = "#$color"
                }
                dashLine.dashColor = Color.parseColor(color)
            }
            R.id.gapSet -> dashLine.dashGap = gapInput.text.toString().toInt()
            R.id.widthSet -> dashLine.dashWidth = widthInput.text.toString().toInt()
        }
    }
}