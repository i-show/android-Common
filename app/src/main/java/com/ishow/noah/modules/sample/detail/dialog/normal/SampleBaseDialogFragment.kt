package com.ishow.noah.modules.sample.detail.dialog.normal

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleBaseDialogBinding
import com.ishow.noah.modules.base.mvvm.AppBindFragment

class SampleBaseDialogFragment : AppBindFragment<FragmentSampleBaseDialogBinding>() {
    override fun getLayout(): Int = R.layout.fragment_sample_base_dialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.fragment = this
    }

    fun onViewClick(v: View) {
        val context = context!!
        when (v.id) {
            R.id.normal -> {
                BaseDialog.Builder(context)
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
                BaseDialog.Builder(context)
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setPositiveButton(R.string.yes)
                    .setWidthProportion(0.78F)
                    .show()
            }

            R.id.listDialog -> {
                BaseDialog.Builder(context)
                    .setItems(R.array.test_array)
                    .show()
            }


            R.id.bottomDialog -> {
                BaseDialog.Builder(context, R.style.Theme_Dialog_Bottom)
                    .setTitle("提示")
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setNegativeButton(R.string.cancel)
                    .setMessageGravity(Gravity.START)
                    .show()
            }

            R.id.bottomDialogNoTitle -> {
                BaseDialog.Builder(context, R.style.Theme_Dialog_Bottom)
                    .setMessage("前面的例子非常简单，但没有任何实际使用意义")
                    .setNegativeButton(R.string.cancel)
                    .setPositiveButton(R.string.yes)
                    .setMessageGravity(Gravity.START)
                    .show()
            }


        }
    }
}
