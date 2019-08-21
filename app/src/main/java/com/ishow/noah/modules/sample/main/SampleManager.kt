package com.ishow.noah.modules.sample.main

import com.ishow.noah.modules.sample.detail.pickview.SamplePickerFragment
import com.ishow.noah.modules.sample.entries.Sample
import com.ishow.noah.modules.sample.detail.glide.SampleGlideCornerFragment

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
            return list
        }

    /*
    private static List<Sample> getWidgetSamples() {
        List<Sample> list = new ArrayList<>();
        list.add(Sample.Companion.instance(R.string.sample_pick_view, SamplePickerFragment.class));
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
