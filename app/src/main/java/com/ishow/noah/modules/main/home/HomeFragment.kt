package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.inflate
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PrintView.init(printView)

        text1.setOnClickListener {
            val intent = Intent("com.android.settings.TRUSTED_CREDENTIALS_USER")
            startActivity(intent)
        }


        text2.setOnClickListener {
            val str1 = "1234567890"
            val str2 = "12345"
            PrintView.print("str1 = " + str1.substring(0, str2.length))
            PrintView.print("str2 = " + str1.substring(str2.length))
        }
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
