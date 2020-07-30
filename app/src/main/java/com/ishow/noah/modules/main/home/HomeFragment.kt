package com.ishow.noah.modules.main.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.ishow.common.extensions.*
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.entries.Token
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.common.manager.CacheManager
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.*


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    private var job: Job? = null

    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)
        PrintView.reset()

        image1.setImageResource(R.drawable.test_banner)
        image1.setOnClickListener {
            val intent = Intent("tenvideo2://?action=5&video_id=uecexs5vdqcsfkf")
            startActivity(intent)
        }

        image2.setImageResource(R.drawable.test_banner)
        image2.setOnClickListener {
            val token = Token()
            token.accessToken = "11111111111"

            CacheManager.cache("1", token)
        }

        image3.setImageResource(R.drawable.test_banner)
        image3.setOnClickListener {
            val egg: Token? = CacheManager.get("1")
            Log.i("yhy", "egg = " + egg?.toJSON())
        }

        root.fitKeyBoard(neeShow)
    }

    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, Observer { PrintView.print(it.toString()) })
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .action("com.yuhaiyang.androidcommon.Test")
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