package com.ishow.noah.modules.sample.detail.recycle.adapter

import android.os.Bundle
import android.view.View
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.extensions.toast
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleBindAdapterBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.sample.entries.Sample
import kotlinx.android.synthetic.main.f_sample_bind_adapter.*

/**
 * Created by yuhaiyang on 2020-02-14.
 */
class SampleBindAdapterFragment : AppBindFragment<FSampleBindAdapterBinding, SampleBindAdapterViewModel>() {

    val adapter = BindAdapter<Sample>()

    override fun getLayout(): Int = R.layout.f_sample_bind_adapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.addLayout(BR.item, R.layout.item_sample_bind_adapter)
        adapter.setOnItemClickListener {
            toast("点击了 $it")
        }

        adapter.setOnItemChildClickListener(R.id.button) { position, viewId ->
            toast("点击了 $position, id为：$viewId")
        }
        list.adapter = adapter

        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))
        adapter.plusData(Sample("11", SampleBindAdapterFragment::class.java))


    }
}