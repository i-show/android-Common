package com.ishow.noah.modules.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class HomeFragment : AppBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .target(SampleMainActivity::class.java)
            .start()
    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
