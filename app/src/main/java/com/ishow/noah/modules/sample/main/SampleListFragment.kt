package com.ishow.noah.modules.sample.main

import android.os.Bundle
import android.view.View
import com.ishow.common.adapter.BindAdapter
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleListBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.base.mvvm.viewmodel.AppBaseViewModel
import com.ishow.noah.modules.sample.SampleManager
import com.ishow.noah.modules.sample.entries.Sample

/**
 * Created by yuhaiyang on 2019-08-20.
 *
 */

class SampleListFragment : AppBindFragment<FragmentSampleListBinding, AppBaseViewModel>() {


    override fun getLayout(): Int = R.layout.fragment_sample_list


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BindAdapter<Sample>()
        adapter.setOnItemClickListener { gotoDetail(adapter.getItem(it)) }
        adapter.addLayout(BR.item, R.layout.item_sample_main)
        adapter.data = SampleManager.samples
        binding.list.adapter = adapter
    }

    private fun gotoDetail(entry: Sample) {
        (activity as SampleMainActivity).showDetail(entry)
    }

    companion object {
        fun newInstance(): SampleListFragment {
            val args = Bundle()
            val fragment = SampleListFragment()
            fragment.arguments = args
            return fragment
        }
    }

}