package com.ishow.noah.modules.sample.detail.androidx.coordinatorLayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ishow.noah.R
import com.ishow.noah.databinding.FSampleCoordinatorLayoutBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import kotlinx.android.synthetic.main.f_sample_coordinator_layout.*

/**
 * Created by yuhaiyang on 2020-06-29.
 */
class SampleCoordinatorLayoutFragment : AppBindFragment<FSampleCoordinatorLayoutBinding, SampleCoordinatorLayoutViewModel>() {

    override fun getLayout(): Int = R.layout.f_sample_coordinator_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appBar.addOnOffsetChangedListener(TopEffectListener(R.id.toolbar))
        val activity = activity as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setHomeButtonEnabled(true)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}