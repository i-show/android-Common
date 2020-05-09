package com.ishow.noah.modules.main.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ishow.noah.R
import com.ishow.noah.modules.base.AppBaseFragment

/**
 * Created by yuhaiyang on 2017/4/21.
 * Home Fragment
 */

class HomeFragment : AppBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onRightClick(v: View) {
        super.onRightClick(v)
        val intent = Intent(Intent.ACTION_VIEW)
        // intent.setPackage("com.qiyi.video")
        // intent.data = Uri.parse("https://www.iqiyi.com/v_19rqvnet8k.html")
        // intent.setPackage("com.tencent.qqlive")
        intent.data = Uri.parse("http://v.youku.com/v_show/id_XNDMxNDkwMTg2MA==.html?tpa=dW5pb25faWQ9MzAwMDA4XzEwMDAwMl8wMl8wMQ&refer=esfhz_operation.xuka.xj_00003036_000000_FNZfau_19010900")
        startActivity(intent)

    }

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
