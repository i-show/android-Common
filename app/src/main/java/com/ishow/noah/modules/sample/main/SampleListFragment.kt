package com.ishow.noah.modules.sample.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.utils.ToastUtils
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentSampleListBinding
import com.ishow.noah.modules.base.mvvm.AppBindFragment
import com.ishow.noah.modules.sample.entries.Sample
import kotlinx.android.synthetic.main.fragment_sample_list.*

/**
 * Created by yuhaiyang on 2019-08-20.
 *
 */

class SampleListFragment : AppBindFragment<FragmentSampleListBinding>() {


    override fun getLayout(): Int = R.layout.fragment_sample_list


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = BindAdapter<Sample>(view.context)
        adapter.setOnItemClickListener { gotoDetail(adapter.getItem(it)) }
        adapter.addLayout(R.layout.item_sample_main, BR.item)
        adapter.data = SampleManager.samples
        list.adapter = adapter
    }

    private fun gotoDetail(entry: Sample) {
        val fragment = entry.action.newInstance()
        if (fragment is Fragment) {
            (activity as SampleMainActivity).showDetail(fragment)
        }
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