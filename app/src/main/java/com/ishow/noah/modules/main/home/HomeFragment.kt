package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.inflate
import com.ishow.common.manager.DialogManager
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.sample.main.SampleMainActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.util.regex.Pattern

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

        youku.setOnClickListener {
           gotoYouku()
        }


        qq.setOnClickListener {
            gotoTenvideo()
        }

        iqiyi.setOnClickListener { gotoIQiYi() }

        mago.setOnClickListener { gotoMago() }
    }

    private fun gotoTenvideo() {
        // h0033co604b
        // mzc00200r5zvu2q
        // n0033rr1tsn
        // g00330i69rb
        // 每一集的ID是不同的，根据ID来播放是哪一集
        val uriStr = "tenvideo2://?action=5&video_id=n0033rr1tsn"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriStr)
        startActivity(intent)
    }

    private fun gotoYouku() {
        // XNTgwNzI5OTky
        // XNDU3MDY3NjQ3Mg
        // XNDQ1OTc1MDcwMA==

        val uriStr = "youku://play?vid=XNDQ1OTc1MDcwMA=="
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriStr)
        startActivity(intent)
    }


    private fun gotoIQiYi() {
        // 13195166700 天心法师3
        // 13195183200 天心法师3
        val uriStr = "iqiyi://mobile/player?tvid=13195183200"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriStr)
        startActivity(intent)
    }


    private fun gotoMago() {
        // 13195166700 天心法师3
        // 7824898 天心法师3
        // 7739188 天心法师3
        val uriStr = "imgotv://player?videoId=7739188"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(uriStr)
        startActivity(intent)
    }

    private fun testDialog() {
        repeat(10) {
            val dialog = BaseDialog.Builder(activity)
                .setMessage("这是第${it}次弹窗")
                .setPositiveButton(R.string.register_timing_text)
                .create()

            DialogManager.instance.addDialog(activity, dialog)
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
