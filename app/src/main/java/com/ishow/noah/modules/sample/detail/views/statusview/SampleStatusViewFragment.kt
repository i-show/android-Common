package com.ishow.noah.modules.sample.detail.views.statusview

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.StatusView
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleStatusViewBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yuhaiyang on 2018-10-29.
 * StatusView
 */
class SampleStatusViewFragment : AppBindFragment<FragmentSampleStatusViewBinding, AppBaseViewModel>() {
    override fun getLayout(): Int = R.layout.fragment_sample_status_view


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.statusView.setOnStatusViewListener(this)
        requestLoading()
    }

    fun onViewClick(v: View) {
        when (v.id) {
            R.id.loading -> requestLoading()
            R.id.empty -> binding.statusView.showEmpty()
            R.id.error -> binding.statusView.showError()
        }
    }

    private fun requestLoading() {
        requestLoading1()
        requestLoading2()
        requestLoading3()
    }

    private fun requestLoading1() {
        binding.statusView.showLoading("tag1")
        lifecycleScope.launch {
            delay(3000)
            mainThread {
                binding.statusView.dismiss("tag1")
                binding.statusView.showError()
            }
        }
    }

    private fun requestLoading2() {
        binding.statusView.showLoading("tag2")
        lifecycleScope.launch {
            delay(7000)
            mainThread { binding.statusView.dismiss("tag2") }
        }
    }

    private fun requestLoading3() {
        binding.statusView.showLoading("tag3")
        lifecycleScope.launch {
            delay(10000)
            mainThread { binding.statusView.dismiss("tag3") }
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