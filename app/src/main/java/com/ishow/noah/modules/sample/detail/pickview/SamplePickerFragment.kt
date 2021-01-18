package com.ishow.noah.modules.sample.detail.pickview

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.dialog
import com.ishow.common.utils.DateUtils
import com.ishow.common.widget.pickview.DateTimePickerDialog
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSamplePickviewBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel

/**
 * 选择View的Sample
 */
class SamplePickerFragment : AppBindFragment<FragmentSamplePickviewBinding, AppBaseViewModel>() {
    override fun getLayout(): Int = R.layout.fragment_sample_pickview

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.pickerView.setAdapter(SamplePickerAdapter())
    }

    fun onViewClick(v: View?) {
        when (v?.id) {
            R.id.getTime -> {
                val time = DateUtils.format(binding.datePicker.currentTime, DateUtils.FORMAT_YMDHMS)
                dialog(time)
            }

            R.id.dialogSample -> {
                context?.let {
                    val dialog = DateTimePickerDialog(it)
                    dialog.show()
                }

            }
        }
    }
}
