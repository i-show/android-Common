package com.ishow.noahark.modules.sample.main;

import com.ishow.noahark.R;
import com.ishow.noahark.modules.sample.dialog.select.SampleSelectDialogAndPickerDialog;
import com.ishow.noahark.modules.sample.edittextpro.SampleEditTextProActivity;
import com.ishow.noahark.modules.sample.entries.Sample;
import com.ishow.noahark.modules.sample.http.SampleHttpActivity;
import com.ishow.noahark.modules.sample.imageloader.SampleImageLoaderActivity;
import com.ishow.noahark.modules.sample.permission.SamplePermissionActivity;
import com.ishow.noahark.modules.sample.photo.select.SampleSelectPhotoActivity;
import com.ishow.noahark.modules.sample.pickview.SamplePickerActivity;
import com.ishow.noahark.modules.sample.pulltorefresh.SamplePullToRefreshActivity;
import com.ishow.noahark.modules.sample.recycle.animation.SampleAnimationRecycleViewActivity;
import com.ishow.noahark.modules.sample.webview.loading.SampleLoadingWebViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaiyang on 2017/10/12.
 * 管理器
 */

public class SampleManager {


    public static List<Sample> getSampleCate() {
        List<Sample> list = new ArrayList<>();
        list.add(new Sample(R.string.sample_widget_list, SampleMainActivity.class));
        return list;
    }

    public static List<Sample> getSamples() {
        return getSamples(Sample.Cate.Widget);
    }


    @SuppressWarnings("WeakerAccess")
    public static List<Sample> getSamples(@Sample.Cate int cate) {
        switch (cate) {
            case Sample.Cate.Widget:
                return getWidgetSamples();
            default:
                return new ArrayList<>();
        }
    }

    private static List<Sample> getWidgetSamples() {
        List<Sample> list = new ArrayList<>();
        list.add(Sample.newInstance(R.string.sample_pick_view, SamplePickerActivity.class));
        list.add(Sample.newInstance(R.string.sample_select_photo, SampleSelectPhotoActivity.class));
        list.add(Sample.newInstance(R.string.sample_permission, SamplePermissionActivity.class));
        list.add(Sample.newInstance(R.string.sample_edittextpro, SampleEditTextProActivity.class));
        list.add(Sample.newInstance(R.string.sample_http, SampleHttpActivity.class));
        list.add(Sample.newInstance(R.string.sample_imageloader, SampleImageLoaderActivity.class));
        list.add(Sample.newInstance(R.string.sample_ani_recycle, SampleAnimationRecycleViewActivity.class));
        list.add(Sample.newInstance(R.string.sample_picker_and_select, SampleSelectDialogAndPickerDialog.class));
        list.add(Sample.newInstance(R.string.sample_loading_webview, SampleLoadingWebViewActivity.class));
        list.add(Sample.newInstance(R.string.sample_pull_to_refresh, SamplePullToRefreshActivity.class));
        return list;
    }
}
