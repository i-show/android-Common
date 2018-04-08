package com.ishow.noah.modules.sample.pulltorefresh;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ishow.common.widget.recyclerview.itemdecoration.LinearSpacingItemDecoration;
import com.ishow.noah.R;
import com.ishow.noah.modules.base.PullToRefreshActivity;
import com.ishow.noah.modules.sample.entries.Job;
import com.ishow.pulltorefresh.IPullToRefreshUtils;
import com.ishow.pulltorefresh.PullToRefreshView;
import com.ishow.pulltorefresh.classic.ClassicHeader;
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

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.addItemDecoration(new LinearSpacingItemDecoration(this, R.dimen.gap_grade_1));
        list.setAdapter(footer);

        ClassicHeader header = new ClassicHeader(this);
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pulltorefresh);
        mPullToRefreshView.setHeader(header);
        mPullToRefreshView.setFooter(footer);
        mPullToRefreshView.setOnPullToRefreshListener(this);

        mPullToRefreshView.setRefreshEnable(false);
        mPullToRefreshView.setLoadMoreEnable(false);
    }

    @Override
    protected void loadData(View v, int pagerNumber, int pagerSize) {
        mPresenter.getList(this, pagerNumber, true);
    }

    @Override
    protected IPullToRefreshUtils getPullToRefreshUtils(View v) {
        return mAdapter;
    }

    @Override
    public void updateView(List<Job> list, int count) {
        mAdapter.resolveData(mPullToRefreshView, list, count);
    }
}
