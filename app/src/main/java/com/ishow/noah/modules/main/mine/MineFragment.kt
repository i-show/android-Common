package com.ishow.noah.modules.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.loadUrl
import com.ishow.common.extensions.open
import com.ishow.common.extensions.toJson
import com.ishow.common.utils.image.loader.ImageLoader
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.R
import com.ishow.noah.databinding.FragementMineBinding
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.modify.ModifyUserActivity
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.base.mvvm.AppBindFragment
import com.ishow.noah.modules.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragement_mine.*

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class MineFragment : AppBindFragment<FragementMineBinding>() {

    override fun getLayout(): Int = R.layout.fragement_mine

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.fragment = this
        dataBinding.vm = bindViewModel(MineViewModel::class.java)
    }

    fun update(userContainer: UserContainer?) {
        avatar.loadUrl(userContainer?.user?.avatar)
        name.text = userContainer?.user?.nickName
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
        when (v?.id) {
            R.id.settings -> open(SettingsActivity::class.java)
            R.id.userContainer -> open(ModifyUserActivity::class.java)
        }
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
