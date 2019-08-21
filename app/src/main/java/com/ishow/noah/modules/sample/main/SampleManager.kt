package com.ishow.noah.modules.sample.main

import com.ishow.noah.modules.sample.detail.dashline.SampleDashLineFragment
import com.ishow.noah.modules.sample.detail.dialog.normal.SampleBaseDialogFragment
import com.ishow.noah.modules.sample.detail.dialog.select.SampleDataPickerDialogFragment
import com.ishow.noah.modules.sample.detail.edittextpro.SampleEditTextProFragment
import com.ishow.noah.modules.sample.detail.pickview.SamplePickerFragment
import com.ishow.noah.modules.sample.entries.Sample
import com.ishow.noah.modules.sample.detail.glide.SampleGlideCornerFragment
import com.ishow.noah.modules.sample.detail.permission.SamplePermissionFragment
import com.ishow.noah.modules.sample.detail.photo.select.SampleSelectPhotoFragment
import com.ishow.noah.modules.sample.detail.statusview.SampleStatusViewFragment
import com.ishow.noah.modules.sample.detail.webview.loading.SampleLoadingWebViewFragment

import java.util.ArrayList

/**
 * Created by yuhaiyang on 2017/10/12.
 * 管理器
 */

object SampleManager {


    val samples: MutableList<Sample>
        get() {
            val list = ArrayList<Sample>()
            list.add(Sample.instance("PickerView", SamplePickerFragment::class.java))
            list.add(Sample.instance("GlideCorner", SampleGlideCornerFragment::class.java))
            list.add(Sample.instance("StatusView", SampleStatusViewFragment::class.java))
            list.add(Sample.instance("BaseDialog", SampleBaseDialogFragment::class.java))
            list.add(Sample.instance("PickerData", SampleDataPickerDialogFragment::class.java))
            list.add(Sample.instance("EditTextPro", SampleEditTextProFragment::class.java))
            list.add(Sample.instance("LoadingWebView", SampleLoadingWebViewFragment::class.java))
            list.add(Sample.instance("DashLine", SampleDashLineFragment::class.java))
            list.add(Sample.instance("Permission", SamplePermissionFragment::class.java))
            list.add(Sample.instance("SelectPhoto", SampleSelectPhotoFragment::class.java))
            return list
        }
}
