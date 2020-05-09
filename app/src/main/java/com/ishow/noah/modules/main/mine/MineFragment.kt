package com.ishow.noah.modules.main.mine

import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.loadUrl
import com.ishow.common.extensions.open
import com.ishow.noah.R
import com.ishow.noah.databinding.FragmentMineBinding
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.modules.account.modify.ModifyUserActivity
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class MineFragment : AppBindFragment<FragmentMineBinding, MineViewModel>() {

    override fun getLayout(): Int = R.layout.fragment_mine

    fun update(userContainer: UserContainer?) {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) dataBinding.vm?.resume()
    }

    override fun onResume() {
        super.onResume()
        dataBinding.vm?.resume()
    }

    fun onViewClick(v: View?) {

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
