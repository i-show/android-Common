package com.ishow.noah.modules.main.mine

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import com.ishow.common.extensions.open
import com.ishow.noah.R
import com.ishow.noah.databinding.FMineBinding
import com.ishow.noah.modules.base.mvvm.view.AppBindFragment
import com.ishow.noah.modules.settings.SettingsActivity
import kotlinx.android.synthetic.main.f_mine.*

/**
 * Created by yuhaiyang on 2020-05-11.
 */
class MineFragment : AppBindFragment<FMineBinding, MineViewModel>() {

    override fun getLayout(): Int = R.layout.f_mine


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settings.setOnClickListener { open(SettingsActivity::class.java) }
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