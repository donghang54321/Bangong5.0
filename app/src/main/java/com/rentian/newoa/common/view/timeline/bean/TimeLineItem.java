package com.rentian.newoa.common.view.timeline.bean;

import android.graphics.drawable.Drawable;

/**
 * 时间轴Bean
 */
public class TimeLineItem {

    private String title;

    private String detail;

    private String time;

    private Drawable icon;

    public TimeLineItem() {
    }

    public TimeLineItem(String title, String detail, String time, Drawable icon) {
        this.title = title;
        this.detail = detail;
        this.time = time;
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "TimeLineItem{" +
                "title='" + title + '\'' +
                ", detail='" + detail + '\'' +
                ", time='" + time + '\'' +
                ", icon=" + icon +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}