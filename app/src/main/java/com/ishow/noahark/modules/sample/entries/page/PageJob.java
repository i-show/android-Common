package com.ishow.noahark.modules.sample.entries.page;


import com.ishow.noahark.modules.sample.entries.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaiyang on 2017/9/19.
 * 分页
 */

public class PageJob {
    public int totalRows;
    public List<Job> results;

    public List<Job> getResults() {
        if (results == null) {
            return new ArrayList<>();
        }
        return results;
    }

    public int getTotalRows() {
        return totalRows;
    }
}
