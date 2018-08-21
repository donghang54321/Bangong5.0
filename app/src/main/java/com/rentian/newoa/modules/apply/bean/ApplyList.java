package com.rentian.newoa.modules.apply.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/12.
 */
public class ApplyList {
    @Expose
    public ArrayList<ApplyListContext> list;

    @Expose
    public String name;

}
