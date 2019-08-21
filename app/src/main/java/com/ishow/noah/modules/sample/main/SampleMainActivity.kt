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

package com.ishow.noah.modules.sample.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.ishow.common.extensions.showFragment
import com.ishow.noah.R
import com.ishow.noah.databinding.ActivitySampleMainBinding
import com.ishow.noah.modules.base.mvvm.AppBindActivity
import com.ishow.noah.modules.sample.entries.Sample

/**
 * 测试Demo
 */
class SampleMainActivity : AppBindActivity<ActivitySampleMainBinding>() {
    private lateinit var viewModel: SampleMainViewModel
    private val mListFragment = SampleListFragment.newInstance()
    private var mLastFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindContentView(R.layout.activity_sample_main)

        bindViewModel(SampleMainViewModel::class.java) {
            viewModel = it
            dataBinding.vm = it
            showFragment(mListFragment)
        }

        val fragmentManager = supportFragmentManager
        fragmentManager.addOnBackStackChangedListener {
            if (fragmentManager.backStackEntryCount == 0) {
                viewModel.updateTitle(getString(R.string.sample_main))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("yhy", "requestCode = $requestCode")
        Log.i("yhy", "resultCode = $resultCode")
        Log.i("yhy", "data = $data")
    }

    fun showDetail(sample: Sample) {
        val fragment = sample.action.newInstance() as? Fragment ?: return

        viewModel.updateTitle(sample.name)
        mLastFragment = fragment
        showFragment(fragment, mListFragment)
    }
}
