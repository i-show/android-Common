package com.ishow.noah.modules.main.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.ishow.common.extensions.*
import com.ishow.common.utils.router.AppRouter
import com.ishow.common.widget.PrintView
import com.ishow.common.widget.dialog.BaseDialog
import com.ishow.noah.R
import com.ishow.noah.databinding.FHomeBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.f_home.*
import kotlinx.coroutines.Job
import java.io.File


/**
 * Created by yuhaiyang on 2020-05-11.
 */
class HomeFragment : AppBindFragment<FHomeBinding, HomeViewModel>() {
    override fun getLayout(): Int = R.layout.f_home

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position.withSeekBar(seekBar)
        position.onClick {
            val privacy = "《隐私协议》"
            val container = "请您务必审慎阅读、充分阅读与理解隐私协议与政策各条款，为了向您提供服务，我们需要收集您的设备信息、操作日志等个人信息。您可以在设置中查看、变更、删除个人信息并管理您的授权。\n您可查看${privacy}了解详细信息，如您同意，请点击同意接受我们的服务"
            val result = container.asSpan()
                .spanClick(privacy, true) { toast("点击了内容")}
                .spanColor(privacy, Color.RED)

            val dialog = BaseDialog.Builder(context)
                .setMessageGravity(Gravity.START)
                .setMessage(result)
                .setPositiveButton("")
                .create()
                .show()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                position.onProgressChanged(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

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