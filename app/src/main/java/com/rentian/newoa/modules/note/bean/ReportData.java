package com.rentian.newoa.modules.note.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2017/6/7.
 */

public class ReportData {

    @Expose
    public String c1;

    @Expose
    public String c2;

    @Expose
    public String dept;

    @Expose
    public String content;

    @Expose
    public String name;

    @Expose
    public long time;

    @Expose
    public int x;

    @Expose
    public int id;

    @Expose
    public int uid;

    public boolean isSelected ;

    @Expose
    public ArrayList<ResReport> list1;

    @Expose
    public ArrayList<String> list;

    @Expose
    public String msg;
}
