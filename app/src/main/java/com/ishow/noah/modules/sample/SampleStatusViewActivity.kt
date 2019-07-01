package com.ishow.noah.modules.sample

import android.os.Bundle
import android.view.View
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.StatusView
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_sample_status_view.*

/**
 * Created by yuhaiyang on 2018-10-29.
 * StatusView
 */
class SampleStatusViewActivity : AppBaseActivity(), View.OnClickListener, StatusView.OnStatusViewListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_status_view)
    }

    override fun initViews() {
        super.initViews()
        statusView.setOnStatusViewListener(this)
        loading.setOnClickListener(this)
        empty.setOnClickListener(this)
        error.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loading -> statusView.showLoading()
            R.id.empty -> statusView.showEmpty()
            R.id.error -> statusView.showError()
        }
    }

    override fun onStatusClick(v: View, which: StatusView.Which) {
        when (which) {
            StatusView.Which.Reload -> ToastUtils.show(this, "reloadClick")
            StatusView.Which.Title -> ToastUtils.show(this, "Title")
            StatusView.Which.SubTitle -> ToastUtils.show(this, "SubTitle")
        }
    }

}