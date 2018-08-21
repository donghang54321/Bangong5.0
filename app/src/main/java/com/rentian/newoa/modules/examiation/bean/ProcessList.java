package com.rentian.newoa.modules.examiation.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/21.
 */
public class ProcessList {
    @Expose
    public String doc;

    @Expose
    public int x;

    @Expose
    public int y;

    @Expose
    public ArrayList<ProcessInfo> info;
}
