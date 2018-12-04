package com.ishow.noah.modules.sample.entries;

import com.ishow.noah.ui.widget.announcement.IAnnouncementData;

public class TestA implements IAnnouncementData {
    public String titlee;

    public TestA(String title) {
        this.titlee = title;
    }

    @Override
    public String getTitle() {
        return titlee;
    }
}
