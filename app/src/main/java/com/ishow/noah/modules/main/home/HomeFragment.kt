package com.ishow.noah.modules.main.home

import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ishow.common.extensions.appScope
import com.ishow.common.extensions.dp2px
import com.ishow.common.extensions.toast
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.load.LoadSir
import com.ishow.common.widget.load.Loader
import com.ishow.common.widget.load.ext.sirDismiss
import com.ishow.common.widget.load.ext.sirEmpty
import com.ishow.common.widget.load.ext.sirLoading
import com.ishow.common.widget.load.ext.withLoadSir
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.ui.widget.load.AppEmptyLoad
import com.ishow.noah.ui.widget.load.AppLoadingStatus
import kotlinx.android.synthetic.main.f_home.*
import kotlin.system.measureNanoTime


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loader = Loader.new()
            .empty(AppEmptyLoad())
            .loading(AppLoadingStatus())
            .build()
        LoadSir.init(loader = loader)

        test1.withLoadSir()
            .emptyText(R.id.empty, "Hello Empty2222")
            .marginTop(40.dp2px())
            .setOnLoaderClickListener { type, view -> toast("点击了Empty") }

        loading.setOnClickListener { test1.sirLoading() }
        loading2.setOnClickListener { test1.sirEmpty() }
        dis.setOnClickListener { test1.sirDismiss() }
    }


    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, { PrintView.print(it.toString()) })
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