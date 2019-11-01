package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.inflate
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        mRootView = container?.inflate(R.layout.fragment_home)
        return mRootView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        test.setOnClickListener {
            val testText =
                "你好吗？Hello，World...网上有许多代理ip，免费的、付费的。大多数公司爬虫会买这些专业版，对于普通人来说，免费的基本满足我们需要了，不过免费有一个弊端，时效性不强，不稳定，所以我们就需要对采集的ip进行一个简单的验证。".toCharArray()
            setText(testText, 1, testText.size)
        }
    }


    private fun setText(text: CharArray, len: Int, size: Int) {
        if (len - 1 >= size) return
        display.setText(text, 0, len)
        GlobalScope.launch(Dispatchers.Main) {
            delay(50)
            setText(text, len + 1, size)
        }
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
