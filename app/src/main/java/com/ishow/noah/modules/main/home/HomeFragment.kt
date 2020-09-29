package com.ishow.noah.modules.main.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.ishow.common.extensions.*
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.common.widget.load.LoadSir
import com.ishow.common.widget.load.Loader
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.sample.SampleLockScreenActivity
import com.ishow.noah.modules.sample.entries.Sample
import com.ishow.noah.ui.widget.load.AppEmptyLoad
import kotlinx.android.synthetic.main.f_home.*


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position.withSeekBar(seekBar)

        val loader = LoadSir.new(test1)
            .empty(AppEmptyLoad())
            .build()
            .show(Loader.Type.Empty)

        //loader.show(Loader.Type.Empty)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                position.onProgressChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        loading.setOnClickListener { onViewClick() }
    }

    override fun onViewClick() {
        super.onViewClick()
        Log.i("yhy", "onViewClick -------" + System.currentTimeMillis())
    }

    override fun onResume() {
        super.onResume()
        Thread {
            Looper.prepare()
            Log.i("yhy", "is MathThread = " + isMainThread())
            Toast.makeText(requireContext(), "AAAAAA", Toast.LENGTH_SHORT).show()
            Looper.loop()
        }.start()
    }

    override fun initViewModel(vm: HomeViewModel) {
        super.initViewModel(vm)
        vm.test2.observe(this, { PrintView.print(it.toString()) })
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        AppRouter.with(context)
            .target(SampleLockScreenActivity::class.java)
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