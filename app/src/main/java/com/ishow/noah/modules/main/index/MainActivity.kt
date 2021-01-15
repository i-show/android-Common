package com.ishow.noah.modules.main.index

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.BottomBar
import com.ishow.noah.R
import com.ishow.noah.databinding.AMainBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindActivity
import com.ishow.noah.modules.main.home.HomeFragment
import com.ishow.noah.modules.main.mine.MineFragment
import com.ishow.noah.modules.main.tab2.Tab2Fragment
import com.ishow.noah.modules.main.tab3.Tab3Fragment
import kotlinx.android.synthetic.main.a_main.*

/**
 * Created by yuhaiyang on 2021-01-15.
 */
class MainActivity : AppBindActivity<AMainBinding, MainViewModel>(), BottomBar.OnBottomBarListener  {

    private var tab1Fragment: HomeFragment? = null
    private var tab2Fragment: Tab2Fragment? = null
    private var tab3Fragment: Tab3Fragment? = null
    private var tab4Fragment: MineFragment? = null
    private val fragmentList = mutableListOf<Fragment>()
    private var lastTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.a_main)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val type = intent.getIntExtra(KEY_TYPE, MainActivity.TAB_FIRST)
        bottomBar.setSelectedId(type, true)
    }

    override fun initViews() {
        super.initViews()
        tab1Fragment = HomeFragment.newInstance()
        fragmentList.add(tab1Fragment!!)
        tab2Fragment = Tab2Fragment.newInstance()
        fragmentList.add(tab2Fragment!!)
        tab3Fragment = Tab3Fragment.newInstance()
        fragmentList.add(tab3Fragment!!)
        tab4Fragment = MineFragment.newInstance()
        fragmentList.add(tab4Fragment!!)

        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = fragmentList.size

            override fun createFragment(position: Int): Fragment {
                return fragmentList[position]
            }
        }

        bottomBar.setOnSelectedChangedListener(this)
    }

    override fun onBackPressed() {
        val nowTime = System.currentTimeMillis()
        if (nowTime - lastTime < 2000) {
            super.onBackPressed()
        } else {
            lastTime = nowTime
            ToastUtils.show(this, R.string.click_again_to_exit)
        }
    }

    override fun onSelectedChanged(parent: ViewGroup, @IdRes selectId: Int, index: Int) {
        viewPager.setCurrentItem(index, false)
    }

    companion object {
        const val TAB_FIRST = R.id.tab_1
    }
}