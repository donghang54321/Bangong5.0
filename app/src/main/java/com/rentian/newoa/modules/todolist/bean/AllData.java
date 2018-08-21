package com.rentian.newoa.modules.todolist.bean;

import com.google.gson.annotations.Expose;
import com.rentian.newoa.modules.task.bean.TaskDetail;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2018/4/20.
 */

public class AllData {
    @Expose
    public ArrayList<DataList> company;

    @Expose
    public ArrayList<DataList> app;

    @Expose
    public ArrayList<DataList> icon;

    @Expose
    public ArrayList<DataList> user;

    @Expose
    public ArrayList<DataList> todo;

    @Expose
    public TaskDetail detail;

    @Expose
    public int id;

    @Expose
    public int beyond;

    @Expose
    public int r;

    @Expose
    public String cn;

    @Expose
    public String sessionid;

    @Expose
    public String week;

    @Expose
    public String pushid;

    @Expose
    public String name;

    @Expose
    public String email;

    @Expose
    public String dept;

    @Expose
    public String post;

    @Expose
    public String avatar;

    @Expose
    public String phone;

    @Expose
    public int cid;

    @Expose
    public int uid;

}
