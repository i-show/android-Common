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
        listDialog.setOnClickListener(this)
        bottomDialog.setOnClickListener(this)
        bottomDialogNoTitle.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.normal -> {
                BaseDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setMessageGravity(Gravity.START or Gravity.TOP)
                        .setPositiveButton(R.string.yes, null)
                        .setNegativeButton(R.string.cancel, null)
                        .setButtonLineColor(Color.LTGRAY)
                        .setNegativeButtonTextColor(Color.GRAY)
                        .show()
            }

            R.id.noTitleDialog -> {
                BaseDialog.Builder(this)
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setPositiveButton(R.string.yes)
                        .setWidthProportion(0.78F)
                        .show()
            }

            R.id.listDialog -> {
                BaseDialog.Builder(this)
                        .setItems(R.array.test_array)
                        .show()
            }




            R.id.bottomDialog -> {
                BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                        .setTitle("提示")
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setNegativeButton(R.string.cancel)
                        .setMessageGravity(Gravity.START)
                        .show()
            }

            R.id.bottomDialogNoTitle -> {
                BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setNegativeButton(R.string.cancel)
                        .setPositiveButton(R.string.yes)
                        .setMessageGravity(Gravity.START)
                        .show()
            }



        }
    }
}
