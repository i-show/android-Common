package com.ishow.noah.modules.sample.main

import com.ishow.noah.R
import com.ishow.noah.modules.sample.SampleStatusViewActivity
import com.ishow.noah.modules.sample.dialog.normal.SampleBaseDialogActivity
import com.ishow.noah.modules.sample.dialog.select.SampleSelectDialogAndPickerDialog
import com.ishow.noah.modules.sample.edittextpro.SampleEditTextProActivity
import com.ishow.noah.modules.sample.entries.Sample
import com.ishow.noah.modules.sample.glide.SampleGlideCornerFragment
import com.ishow.noah.modules.sample.http.SampleHttpActivity
import com.ishow.noah.modules.sample.imageloader.SampleImageLoaderActivity
import com.ishow.noah.modules.sample.permission.SamplePermissionActivity
import com.ishow.noah.modules.sample.photo.select.SampleSelectPhotoActivity
import com.ishow.noah.modules.sample.pickview.SamplePickerActivity
import com.ishow.noah.modules.sample.pulltorefresh.SamplePullToRefreshActivity
import com.ishow.noah.modules.sample.recycle.animation.SampleAnimationRecycleViewActivity
import com.ishow.noah.modules.sample.webview.loading.SampleLoadingWebViewActivity
import com.ishow.noah.modules.sample.widget.dashline.SampleDashLineActivity

import java.util.ArrayList

/**
 * Created by yuhaiyang on 2017/10/12.
 * 管理器
 */

object SampleManager {


    val samples: MutableList<Sample>
        get() {
            val list = ArrayList<Sample>()
            list.add(Sample.instance("GlideCorner", SampleGlideCornerFragment::class.java))
            return list
        }

    /*
    private static List<Sample> getWidgetSamples() {
        List<Sample> list = new ArrayList<>();
        list.add(Sample.Companion.instance(R.string.sample_pick_view, SamplePickerActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_select_photo, SampleSelectPhotoActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_permission, SamplePermissionActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_edittextpro, SampleEditTextProActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_http, SampleHttpActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_imageloader, SampleImageLoaderActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_ani_recycle, SampleAnimationRecycleViewActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_picker_and_select, SampleSelectDialogAndPickerDialog.class));
        list.add(Sample.Companion.instance(R.string.sample_loading_webview, SampleLoadingWebViewActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_pull_to_refresh, SamplePullToRefreshActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_base_dialog, SampleBaseDialogActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_status_view, SampleStatusViewActivity.class));
        list.add(Sample.Companion.instance(R.string.sample_dash_line, SampleDashLineActivity.class));
        return list;
    }
    */
}
