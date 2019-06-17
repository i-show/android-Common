/*
 * Copyright (C) 2017. The yuhaiyang Android Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ishow.noah.modules.egg

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_egg.*

/**
 * Created by yuhaiyang on 2017/5/27.
 * 彩蛋
 */

class EggActivity : AppBaseActivity() {
    private var mAdapter: EggAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egg)
        mAdapter!!.data = EggFactory.product(this)
    }

    override fun initViews() {
        super.initViews()

        mAdapter = EggAdapter(context)
        list.layoutManager = GridLayoutManager(context, 2)
        list.adapter = mAdapter
    }
}
