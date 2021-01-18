package com.ishow.noah.modules.main.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.announcement.IAnnouncementData
import com.ishow.common.widget.load.LoadSir
import com.ishow.common.widget.load.Loader
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.ui.widget.load.AppEmptyLoad
import com.ishow.noah.ui.widget.load.AppLoadingStatus


class Test(override val title: String) : IAnnouncementData {

}


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

        binding.bu1.setOnClickListener {
           val result =  TestAAA.missingNumber(intArrayOf(9,6,4,2,3,5,7,0,1))
            Log.i("yhy" ,"result = $result")
        }

    }

    override fun initViews(view: View) {
        super.initViews(view)
        PrintView.init(binding.printView)
        PrintView.reset()
    }


    @SuppressLint("SetTextI18n")
    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.userName.observe(this, { Log.i("yhy", "userName = $it") })
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