package com.ishow.noah.modules.main.tab2

import android.os.Bundle
import com.ishow.noah.R
import com.ishow.noah.databinding.FTab2Binding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class Tab2Fragment : AppBindFragment<FTab2Binding, Tab2ViewModel>() {

    override fun getLayout(): Int = R.layout.f_tab2


    companion object {

        fun newInstance(): Tab2Fragment {

            val args = Bundle()

            val fragment = Tab2Fragment()
            fragment.arguments = args
            return fragment
        }
    }
}