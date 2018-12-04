package com.ishow.noah.modules.main.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.inflate

import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.TopBar
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.entries.TestA
import com.ishow.noah.modules.sample.main.SampleMainActivity
import com.ishow.noah.ui.widget.announcement.IAnnouncementData
import kotlinx.android.synthetic.main.fragement_tab_1.*
import java.util.ArrayList

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class HomeFragment : AppBaseFragment() {

    private var mRootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView != null) {
            return mRootView
        }

        mRootView = container?.inflate(R.layout.fragement_tab_1)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topBar.setOnTopBarListener(this)


        val dataList = ArrayList<TestA>()
        dataList.add(TestA("你好啊"))
        dataList.add(TestA("Hello Hello445奋达科技豆腐干了肯德基发过来的看法就搞定了开放个建档立卡房管局考虑到交付给"))
        testSwitcher.setData(dataList as List<IAnnouncementData>?)
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onRightClick(v: View) {
        super.onRightClick(v)

        AppRouter.with(context)
                .target(SampleMainActivity::class.java)!!
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
