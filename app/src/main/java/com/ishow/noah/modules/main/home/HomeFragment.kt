package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.adapter.BindAdapter
import com.ishow.common.extensions.inflate
import com.ishow.common.utils.ToastUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.BR
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity
import kotlinx.android.synthetic.main.fragement_tab_1.*

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class HomeFragment : AppBaseFragment() {

    private var mRootView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView != null) {
            return mRootView
        }

        mRootView = container?.inflate(R.layout.fragement_tab_1)
        return mRootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topBar.setOnTopBarListener(this)

        val adapter = BindAdapter<String>(context!!)
        adapter.addLayout(R.layout.test_item, BR.item)
        adapter.setOnItemClickListener { ToastUtils.show(context, "it = $it") }
        recyclerView.adapter = adapter

        val dataList = ArrayList<String>()
        dataList.add("张三")
        dataList.add("李四")
        dataList.add("王五")
        adapter.data = dataList

        test.text =
            Build.MANUFACTURER + " \n " + Build.VERSION.SDK_INT + " \n " + Build.MODEL + " \n " + Build.DISPLAY + " \n" + Build.PRODUCT
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
