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
                        .setNegativeButtonTextColor(Color.GRAY)
                        .create()

                dialog.show()
            }

            R.id.noTitleDialog -> {
                val dialog = BaseDialog.Builder(this)
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setPositiveButton(R.string.yes, null)
                        .create()

                dialog.show()
            }

            R.id.bottomDialog -> {
                val dialog = BaseDialog.Builder(this, R.style.Theme_Dialog_Bottom_IOS_NoTitle)
                        .setTitle("Title")
                        .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                        .setNegativeButton(R.string.cancel, null)
                        .fromBottom(true)
                        .create()

                dialog.show()
            }
        }
    }
}
