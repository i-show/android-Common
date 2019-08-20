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
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ishow.common.extensions.showFragment
import com.ishow.common.utils.ToastUtils
import com.ishow.common.widget.BottomBar
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import com.ishow.noah.modules.main.home.HomeFragment
import com.ishow.noah.modules.main.mine.MineFragment
import com.ishow.noah.modules.main.tab2.Tab2Fragment
import com.ishow.noah.modules.main.tab3.Tab3Fragment
import com.ishow.noah.modules.sample.glide.SampleGlideCornerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppBaseActivity(), BottomBar.OnBottomBarListener {

    private var mBeforeFragment: Fragment? = null
    private var mTab1Fragment: HomeFragment? = null
    private var mTab2Fragment: Tab2Fragment? = null
    private var mTab3Fragment: Tab3Fragment? = null
    private var mTab4Fragment: MineFragment? = null

    private var mLastTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = intent
        val type = intent.getIntExtra(KEY_TYPE, TAB_FIRST)
        bottomBar.setSelectedId(type, true)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val type = intent.getIntExtra(KEY_TYPE, TAB_FIRST)
        bottomBar.setSelectedId(type, true)
    }

    override fun initViews() {
        super.initViews()
        bottomBar.setOnSelectedChangedListener(this)
    }


    override fun onBackPressed() {
        val nowTime = System.currentTimeMillis()
        if (nowTime - mLastTime < 2000) {
            super.onBackPressed()
        } else {
            mLastTime = nowTime
            ToastUtils.show(this, R.string.click_again_to_exit)
        }
    }


    override fun onSelectedChanged(parent: ViewGroup, @IdRes selectId: Int, index: Int) {
        selectFragment(selectId)
    }

    override fun onClickChild(v: View, isSameView: Boolean) {}


    fun selectFragment(selectId: Int) {
        when (selectId) {
            R.id.tab_1 -> {
                if (mTab1Fragment == null) {
                    mTab1Fragment = HomeFragment.newInstance()
                }
                showFragment(mTab1Fragment, mBeforeFragment, FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                mBeforeFragment = mTab1Fragment
            }
            R.id.tab_2 -> {
                if (mTab2Fragment == null) {
                    mTab2Fragment = Tab2Fragment.newInstance()
                }

                showFragment(mTab2Fragment, mBeforeFragment, FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                mBeforeFragment = mTab2Fragment
            }
            R.id.tab_3 -> {
                if (mTab3Fragment == null) {
                    mTab3Fragment = Tab3Fragment.newInstance()
                }
                showFragment(mTab3Fragment, mBeforeFragment, FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                mBeforeFragment = mTab3Fragment
            }
            R.id.tab_4 -> {
                if (mTab4Fragment == null) {
                    mTab4Fragment = MineFragment.newInstance()
                }
                showFragment(mTab4Fragment, mBeforeFragment, FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                mBeforeFragment = mTab4Fragment
            }
        }
    }

    companion object {
        const val TAB_FIRST = R.id.tab_1
    }
}



