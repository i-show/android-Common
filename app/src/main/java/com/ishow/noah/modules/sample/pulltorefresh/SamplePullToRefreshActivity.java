package com.ishow.noah.modules.sample.pulltorefresh;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ishow.common.widget.recyclerview.itemdecoration.SpacingDecoration;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.PullToRefreshActivity;
import com.ishow.noah.modules.sample.entries.Job;
import com.ishow.pulltorefresh.IPullToRefreshUtils;
import com.ishow.pulltorefresh.PullToRefreshView;
import com.ishow.pulltorefresh.headers.classic.ClassicHeader;
import com.ishow.pulltorefresh.recycleview.LoadMoreAdapter;

import java.util.List;

/**
 * Created by yuhaiyang on 2017/9/21.
 */

public class SamplePullToRefreshActivity extends PullToRefreshActivity implements SamplePullToRefreshContract.View {
    private SamplePullToRefreshContract.Presenter mPresenter;
    private SamplePullToRefreshAdapter mAdapter;

    private PullToRefreshView mPullToRefreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_pulltorefresh);
        mPresenter = new SamplePullToRefreshPresenter(this);
        mPresenter.getList(this, 0, true);
    }

    @Override
    protected void initViews() {
        super.initViews();

        mAdapter = new SamplePullToRefreshAdapter(this);
        LoadMoreAdapter footer = new LoadMoreAdapter(this, mAdapter);

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new SpacingDecoration(this, R.dimen.gap_grade_1));
        list.setAdapter(footer);

        ClassicHeader header = new ClassicHeader(this);
        mPullToRefreshView = findViewById(R.id.pulltorefresh);
        mPullToRefreshView.setHeader(header);
        mPullToRefreshView.setFooter(footer);
        mPullToRefreshView.setOnPullToRefreshListener(this);

        mPullToRefreshView.setRefreshEnable(false);
        mPullToRefreshView.setLoadMoreEnable(false);
    }

    @Override
    protected void loadData(@NonNull View v, int pagerNumber, int pagerSize) {
        mPresenter.getList(this, pagerNumber, true);
    }

    @Override
    protected IPullToRefreshUtils getPullToRefreshUtils(@NonNull View v) {
        return mAdapter;
    }

    @Override
    public void updateView(List<Job> list, int count) {
        mAdapter.resolveData(mPullToRefreshView, list, count);
    }
}
