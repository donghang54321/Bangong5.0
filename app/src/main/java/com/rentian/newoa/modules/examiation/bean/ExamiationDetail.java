package com.rentian.newoa.modules.examiation.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/21.
 */
public class ExamiationDetail {

    @Expose
    public ArrayList<Node> node;

    @Expose
    public String form;//表单名字

    @Expose
    public String taskid;

    @Expose
    public String name;

    @Expose
    public String dept;

    @Expose
    public ArrayList<ProcessList> list;




}
