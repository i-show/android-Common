package com.ishow.noah.modules.sample.pickview

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.dialog

import com.ishow.common.utils.DateUtils
import com.ishow.common.widget.pickview.DateTimePicker
import com.ishow.common.widget.pickview.DateTimePickerDialog
import com.ishow.common.widget.pickview.PickerView
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_sample_pickview.*

/**
 * 选择View的Sample
 */
class SamplePickerActivity : AppBaseActivity(), View.OnClickListener {

    private var mDateTimePicker: DateTimePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_pickview)
    }

    override fun initViews() {
        super.initViews()
        val pickerView = findViewById<View>(R.id.picker_view) as PickerView
        pickerView.setAdapter(SamplePickerAdapter())


        val getTime = findViewById<View>(R.id.get_time)
        getTime.setOnClickListener(this)

        val dialogTest = findViewById<View>(R.id.dialog_test)
        dialogTest.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.get_time -> {
                val time = DateUtils.format(datePicker.currentTime, DateUtils.FORMAT_YMDHMS)
                dialog(time)
            }

            R.id.dialog_test -> {
                val dialog = DateTimePickerDialog(this)
                dialog.show()
            }
        }
    }
}
