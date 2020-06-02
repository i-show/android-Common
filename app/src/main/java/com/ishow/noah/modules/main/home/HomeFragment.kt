package com.ishow.noah.modules.main.home

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.utils.textwatcher.PhoneNumberTextWatcher
import com.ishow.common.widget.PrintView
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    private var job: Job? = null

    override fun getLayout(): Int = R.layout.f_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrintView.init(printView)
        PrintView.reset()

        val ani = ValueAnimator.ofFloat(1F, 1.5F)
        ani.duration = 800
        ani.addUpdateListener {
            val value = it.animatedValue as Float
            test2.scaleX = value
            test2.scaleY = value
        }
        ani.repeatCount = ValueAnimator.INFINITE
        phone.addTextChangedListener(PhoneNumberTextWatcher())
        test2.setOnClickListener {
            ani.start()
        }




        test3.setOnClickListener {
            job?.cancel()
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

    fun timing(times: Int, block: (time: Int) -> Unit) = GlobalScope.launch {
        var currentTime = times
        repeat(times) {
            block(currentTime)
            currentTime -= 1
            delay(1000)
        }
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