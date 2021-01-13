package com.ishow.noah.modules.sample.detail.views.pull2refresh

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.extensions.toJSON
import com.ishow.common.widget.pulltorefresh.headers.google.GoogleStyleHeader
import com.ishow.common.widget.pulltorefresh.recycleview.LoadMoreAdapter
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.databinding.FSamplePull2refreshBinding
import com.ishow.noah.modules.base.mvvm.view.Pull2RefreshFragment
import com.ishow.noah.modules.sample.entries.SampleTestPage
import kotlinx.android.synthetic.main.f_sample_pull2refresh.*

/**
 * Created by yuhaiyang on 2019-09-19.
 *
 */
class SamplePull2RefreshFragment : Pull2RefreshFragment<FSamplePull2refreshBinding, SamplePull2RefreshViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_pull2refresh
    val adapter = BindAdapter<SampleTestPage>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context

        adapter.addLayout(BR.item, R.layout.i_sample_pull2refresh)
        val footer = LoadMoreAdapter(adapter)
        list.adapter = footer

        pull2refresh.setHeader(GoogleStyleHeader(context))
        pull2refresh.setFooter(footer)
    }

    override fun initViewModel(vm: SamplePull2RefreshViewModel) {
        super.initViewModel(vm)
        vm.data.observe(this, {
            Log.i("yhy", "list = " + it?.toJSON())
            adapter.data = it
        })
    }

    override fun onLoadData(v: View, pager: Int, refresh: Boolean) {
        dataBinding.vm?.getData(pager)
    }

    override fun hasStatusView(): Boolean {
        return false
    }
}