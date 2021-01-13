/**
 * Copyright (C) 2016 The yuhaiyang Android Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.BottomBar
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import com.ishow.noah.modules.main.home.HomeFragment
import com.ishow.noah.modules.main.mine.MineFragment
import com.ishow.noah.modules.main.tab2.Tab2Fragment
import com.ishow.noah.modules.main.tab3.Tab3Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppBaseActivity(), BottomBar.OnBottomBarListener {

    private var tab1Fragment: HomeFragment? = null
    private var tab2Fragment: Tab2Fragment? = null
    private var tab3Fragment: Tab3Fragment? = null
    private var tab4Fragment: MineFragment? = null
    private val fragmentList = mutableListOf<Fragment>()
    private var lastTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun initNecessaryData() {
        super.initNecessaryData()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val type = intent.getIntExtra(KEY_TYPE, TAB_FIRST)
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



