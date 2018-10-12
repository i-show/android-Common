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
        singleChoiceDialog.setOnClickListener(this)
        multiChoiceDialog.setOnClickListener(this)
        bottomDialog.setOnClickListener(this)
        bottomDialogNoTitle.setOnClickListener(this)
        singleChoiceBottomDialog.setOnClickListener(this)
        singleChoiceBottomDialog2.setOnClickListener(this)
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
                        .setPositiveButton(R.string.yes, null)
                        .setWidthProportion(0.78F)
                        .show()
            }

            R.id.listDialog -> {
                BaseDialog.Builder(this)
                        .setItems(R.array.test_array, null)
                        .show()
            }

            R.id.singleChoiceDialog -> {
                BaseDialog.Builder(this)
                        .setTitle("选择方式")
                        .setSingleChoiceItems(R.array.test_array, 0, null)
                        .setPositiveButton(R.string.yes, null)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
            }

            R.id.multiChoiceDialog -> {
                BaseDialog.Builder(this)
                        .setMultiChoiceItems(R.array.test_array, null, null)
                        .setPositiveButton(R.string.yes, null)
                        .show()
            }

            R.id.bottomDialog -> {
                BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                        .setTitle("提示")
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setNegativeButton(R.string.cancel, null)
                        .setMessageGravity(Gravity.START)
                        .show()
            }

            R.id.bottomDialogNoTitle -> {
                BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.yes, null)
                        .setMessageGravity(Gravity.START)
                        .show()
            }

            R.id.singleChoiceBottomDialog -> {
                BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                        .setTitle("选择方式")
                        .setSingleChoiceItems(R.array.test_array, 0, null)
                        .setPositiveButton(R.string.yes, null)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
            }

            R.id.singleChoiceBottomDialog2 -> {
                BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom)
                        .setSingleChoiceItems(R.array.test_array, 0, null)
                        .setNegativeButton(R.string.cancel, null)
                        .show()
            }
        }
    }
}
