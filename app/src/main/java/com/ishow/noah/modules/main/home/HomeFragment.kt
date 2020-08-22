package com.ishow.noah.modules.main.home

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.ishow.common.extensions.fitKeyBoard
import com.ishow.common.extensions.setFont
import com.ishow.common.extensions.timing
import com.ishow.common.extensions.toJSON
import com.ishow.common.manager.CacheManager
import com.ishow.common.utils.DateUtils
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.entries.Token
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.log.LogDatabase
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.Request


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    private var job: Job? = null

    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        test1.setFont("iconfont.ttf")
        test1.text = "\ue7a3"
        test1.setOnClickListener {
            timing(100, 300) {
                Log.i("yhy", "progress = " + (100 - it))
                loading.setProgress(100 - it)
            }
        }
    }


    private class AA(val count: Int, val ok: Boolean) : Comparable<AA> {
        override fun compareTo(other: AA): Int {
            return if (ok == other.ok) {
                this.count.compareTo(other.count)
            } else {
                ok.compareTo(other.ok)
            }
        }

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