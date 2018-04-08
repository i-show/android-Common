package com.ishow.noah.modules.sample.entries;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by yuhaiyang on 2017/6/29.
 * 工作
 */

public class Job {
    @JSONField(name = "FactoryInfoID")
    public String factoryInfoId;
    @JSONField(name = "FactoryID")
    public String factoryId;
    @JSONField(name = "FactoryName")
    public String factoryName;
    @JSONField(name = "PicURL")
    public String imagePath;
    @JSONField(name = "Title")
    public String title;
    @JSONField(name = "SalaryTo")
    public int salaryTo;
    @JSONField(name = "SalaryFrom")
    public int salaryFrom;
    @JSONField(name = "ProfitAmount")
    public String profitAmount;
    @JSONField(name = "IsHotFactory")
    public int isHotFactory;
    public String applyCount = "183";
    public String profitTime = "23:11:15";

    @JSONField(name = "Content")
    public String content;
    @JSONField(name = "WorkContent")
    public String workContent;
    @JSONField(name = "FactoryContent")
    public String factoryContent;
    @JSONField(name = "Feature")
    public String feature;
    @JSONField(name = "ShortDate")
    public String signUpDate;
    @JSONField(name = "Subsidy")
    public float subsidy;


    public String getTitle() {
        return title;
    }
}
