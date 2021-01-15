package com.ishow.noah.modules.main.mine

import android.os.Bundle
import android.view.View
import com.ishow.noah.R
import com.ishow.noah.databinding.FMineBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class MineFragment : AppBindFragment<FMineBinding, MineViewModel>() {

    override fun getLayout(): Int = R.layout.f_mine


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): MineFragment {

            val args = Bundle()

            val fragment = MineFragment()
            fragment.arguments = args
            return fragment
        }
    }
}