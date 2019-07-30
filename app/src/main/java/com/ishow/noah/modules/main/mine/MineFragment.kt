package com.ishow.noah.modules.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.loadUrl
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bindContentView(container, R.layout.fragement_mine)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




    fun update(userContainer: UserContainer?) {
        avatar.loadUrl(userContainer?.user?.avatar)
        name.text = userContainer?.user?.nickName
    }

    fun onViewClick(v: View?) {
        when (v?.id) {
            R.id.settings -> {
                AppRouter.with(context)
                    .target(SettingsActivity::class.java)
                    .start()
            }

            R.id.name -> {
                AppRouter.with(context)
                    .target(ModifyUserActivity::class.java)
                    .start()
            }
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
