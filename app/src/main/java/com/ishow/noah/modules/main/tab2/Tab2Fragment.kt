package com.ishow.noah.modules.main.tab2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.common.widget.StatusView
import kotlinx.android.synthetic.main.fragment_tab_2.*

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class Tab2Fragment : AppBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusView.showLoading()
        set.setOnClickListener { test.text = System.currentTimeMillis().toString() }
    }

    companion object {

        fun newInstance(): Tab2Fragment {

            val args = Bundle()

            val fragment = Tab2Fragment()
            fragment.arguments = args
            return fragment
        }
    }
}
