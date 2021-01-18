package com.ishow.noah.modules.sample.detail.utils.info

import android.annotation.SuppressLint
import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import com.ishow.common.extensions.clipboardManager
import com.ishow.common.extensions.toast
import com.ishow.noah.R
import com.ishow.noah.databinding.FBaseInfoBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-03-18.
 */
class BaseInfoFragment : AppBindFragment<FBaseInfoBinding, BaseInfoViewModel>() {

    override fun getLayout(): Int = R.layout.f_base_info

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.model.text = "model：${Build.MODEL}"
        binding. model.setOnLongClickListener { copy(Build.MODEL) }

        binding.manufacturer.text = "manufacturer：${Build.MANUFACTURER}"
        binding.manufacturer.setOnLongClickListener { copy(Build.MANUFACTURER) }

        binding.version.text = "version：${Build.VERSION.RELEASE}"
        binding.version.setOnLongClickListener { copy(Build.VERSION.RELEASE) }

        binding.display.text = "display：${Build.DISPLAY}"
        binding.display.setOnLongClickListener { copy(Build.DISPLAY) }
    }

    private fun copy(str: String) :Boolean{
        val manager = context?.clipboardManager
        val data = ClipData.newPlainText("BaseInfo", str)
        manager?.setPrimaryClip(data)
        Log.i(TAG, "BaseInfo: $str ")
        toast("已经复制到剪切板")
        return true
    }

    companion object {
        private const val TAG = "BaseInfoFragment"
    }
}