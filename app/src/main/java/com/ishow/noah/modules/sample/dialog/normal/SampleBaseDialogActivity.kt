package com.ishow.noah.modules.sample.dialog.normal

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.ishow.common.widget.dialog.BaseDialog

import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_sample_base_dialog.*

class SampleBaseDialogActivity : AppBaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_base_dialog)
    }

    override fun initViews() {
        super.initViews()
        normal.setOnClickListener(this)
        noTitleDialog.setOnClickListener(this)
        bottomDialog.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.normal -> {
                val dialog = BaseDialog.Builder(this)
                        .setTitle("Title")
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setMessageGravity(Gravity.START or Gravity.TOP)
                        .setPositiveButton(R.string.yes, null)
                        .setNegativeButton(R.string.cancel, null)
                        .setButtonLineColor(Color.LTGRAY)
                        .setNegativeButtonTextColor(Color.GRAY)
                        .create()

                dialog.show()
            }

            R.id.noTitleDialog -> {
                val dialog = BaseDialog.Builder(this)
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setPositiveButton(R.string.yes, null)
                        .setWidthProportion(0.7F)
                        .create()

                dialog.show()
            }

            R.id.bottomDialog -> {
                val dialog = BaseDialog.Builder(this, R.style.Theme_Dialog_SelectPhoto)
                        .setTitle("Title")
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setNegativeButton(R.string.cancel, null)
                        .setMessageGravity(Gravity.START)
                        .create()

                dialog.show()
            }
        }
    }
}
