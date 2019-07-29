package com.ishow.noah.modules.main.mine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.common.extensions.loadUrl
import com.ishow.common.utils.image.loader.ImageLoader
import com.ishow.common.utils.router.AppRouter
import com.ishow.noah.R
import com.ishow.noah.entries.UserContainer
import com.ishow.noah.manager.UserManager
import com.ishow.noah.modules.account.modify.ModifyUserActivity
import com.ishow.noah.modules.base.AppBaseFragment
import com.ishow.noah.modules.settings.SettingsActivity
import kotlinx.android.synthetic.main.fragement_mine.*

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class MineFragment : AppBaseFragment(), MineContract.View, View.OnClickListener {


    private var mRootView: View? = null

    private lateinit var mPresenter: MineContract.Presenter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView != null) {
            return mRootView
        }

        mRootView = inflater.inflate(R.layout.fragement_mine, container, false)

        mPresenter = MinePresenter(this)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings.setOnClickListener(this)
        name.setOnClickListener(this)
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden)mPresenter.onResume()
    }


    override fun update(userContainer: UserContainer?) {
        avatar.loadUrl(userContainer?.user?.avatar)
        name.text = userContainer?.user?.nickName
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.settings -> {
                AppRouter.with(context)
                    .target(SettingsActivity::class.java)
                    .start()
            }

            R.id.name ->{
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
