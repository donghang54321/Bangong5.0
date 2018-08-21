package com.rentian.newoa.modules.todolist.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2018/4/20.
 */

public class DataList {


    @Expose
    public String mingcheng;

    @Expose
    public String typeName;

    @Expose
    public String name;

    @Expose
    public String url;

    @Expose
    public String color;

    @Expose
    public String dept;

    @Expose
    public String post;

    @Expose
    public int typeId;


    @Expose
    public int index;

    @Expose
    public int isTrend;

    @Expose
    public int isLike;

    @Expose
    public int isComment;

    @Expose
    public int isFile;

    @Expose
    public String title;

    @Expose
    public String tag;

    @Expose
    public String endTime;

    @Expose
    public String img;

    @Expose
    public String icon;

    @Expose
    public String content;

    @Expose
    public String b;

    @Expose
    public int rank;

    @Expose
    public int qiye;

    @Expose
    public int id;

    @Expose
    public int wc;

    @Expose
    public int beyond;

    public int titleType ;

    @Expose
    public ArrayList<DataList> list;

}
