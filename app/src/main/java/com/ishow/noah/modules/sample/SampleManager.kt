package com.ishow.noah.modules.sample

import com.ishow.noah.modules.sample.detail.dashline.SampleDashLineFragment
import com.ishow.noah.modules.sample.detail.dialog.normal.SampleBaseDialogFragment
import com.ishow.noah.modules.sample.detail.dialog.select.SampleDataPickerDialogFragment
import com.ishow.noah.modules.sample.detail.edittextpro.SampleEditTextProFragment
import com.ishow.noah.modules.sample.detail.glide.SampleGlideCornerFragment
import com.ishow.noah.modules.sample.detail.log.SampleLogFragment
import com.ishow.noah.modules.sample.detail.permission.SamplePermissionFragment
import com.ishow.noah.modules.sample.detail.photo.select.SampleSelectPhotoFragment
import com.ishow.noah.modules.sample.detail.pickview.SamplePickerFragment
import com.ishow.noah.modules.sample.detail.recycle.adapter.SampleBindAdapterFragment
import com.ishow.noah.modules.sample.detail.utils.json.SampleJsonFragment
import com.ishow.noah.modules.sample.detail.utils.reflection.SampleReflectionFragment
import com.ishow.noah.modules.sample.detail.views.constraint.SampleConstraintLayoutFragment
import com.ishow.noah.modules.sample.detail.dialog.loading.SampleLoadingDialogFragment
import com.ishow.noah.modules.sample.detail.views.loading.SampleLoadingWebViewFragment
import com.ishow.noah.modules.sample.detail.views.pull2refresh.SamplePull2RefreshFragment
import com.ishow.noah.modules.sample.detail.views.statusview.SampleStatusViewFragment
import com.ishow.noah.modules.sample.entries.Sample
import java.util.*

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
            // Dialog
            list.add(Sample.instance("BaseDialog", SampleBaseDialogFragment::class.java))
            list.add(Sample.instance("LoadingDialog", SampleLoadingDialogFragment::class.java))
            list.add(Sample.instance("PickerData", SampleDataPickerDialogFragment::class.java))

            list.add(Sample.instance("EditTextPro", SampleEditTextProFragment::class.java))
            list.add(Sample.instance("LoadingWebView", SampleLoadingWebViewFragment::class.java))
            list.add(Sample.instance("DashLine", SampleDashLineFragment::class.java))
            list.add(Sample.instance("Permission", SamplePermissionFragment::class.java))
            list.add(Sample.instance("SelectPhoto", SampleSelectPhotoFragment::class.java))
            list.add(Sample.instance("Log", SampleLogFragment::class.java))
            list.add(Sample.instance("反射", SampleReflectionFragment::class.java))
            list.add(Sample.instance("Pull2Refresh", SamplePull2RefreshFragment::class.java))
            list.add(Sample.instance("Json", SampleJsonFragment::class.java))
            list.add(Sample.instance("ConstraintLayout", SampleConstraintLayoutFragment::class.java))
            list.add(Sample.instance("BindAdapter", SampleBindAdapterFragment::class.java))
            return list
        }
}
