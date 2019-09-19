package com.ishow.noah.modules.sample.detail.statusview

import android.os.Bundle
import android.view.View
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.StatusView
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleStatusViewBinding
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.android.synthetic.main.fragment_sample_status_view.*

/**
 * Created by yuhaiyang on 2018-10-29.
 * StatusView
 */
class SampleStatusViewFragment : AppBindFragment<FragmentSampleStatusViewBinding, AppBaseViewModel>() {
    override fun getLayout(): Int = R.layout.fragment_sample_status_view


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusView.setOnStatusViewListener(this)
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.loading -> statusView.showLoading()
            R.id.empty -> statusView.showEmpty()
            R.id.error -> statusView.showError()
        }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {
        when (which) {
            StatusView.Which.Reload -> ToastUtils.show(context, "reloadClick")
            StatusView.Which.Title -> ToastUtils.show(context, "Title")
            StatusView.Which.SubTitle -> ToastUtils.show(context, "SubTitle")
        }
    }

}