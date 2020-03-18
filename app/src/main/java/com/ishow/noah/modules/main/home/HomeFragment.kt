package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.findDrawable
import com.ishow.common.extensions.inflate
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity
import com.ishow.common.utils.http.ip.IpUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
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

    var count = 1

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrintView.init(printView)

        send.setOnClickListener {
            channelTest()
        }

        reset.setOnClickListener {
           val ipInfo = IpUtils.getIp(context)
        }
        show.setOnClickListener(this::test3)
    }


    private fun test3(v: View) {
        val drawable = context?.findDrawable(R.drawable.footer_tab_1_normal) ?: return

        drawable.setTint(0x33FF0000.toInt())
        imageView3.setImageDrawable(drawable)
    }

    private val channel1 = Channel<String>()
    private val channel2 = Channel<String>()
    private fun channelTest() {
        GlobalScope.launch {
            Log.i("yhy", "channelTest: start = " + DateUtils.now())
            val result = channel1.receive()
            Log.i("yhy", "channelTest: mid = " + DateUtils.now() + " result = $result")
            val result2 = channel2.receive()
            Log.i("yhy", "channelTest: end = " + DateUtils.now() + " result = $result2")
        }
    }

    fun test() {
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
