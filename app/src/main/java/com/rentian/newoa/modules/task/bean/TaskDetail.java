package com.rentian.newoa.modules.task.bean;

import android.widget.EditText;

import com.google.gson.annotations.Expose;
import com.rentian.newoa.modules.todolist.bean.DataList;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2018/5/15.
 */

public class TaskDetail {


    @Expose
    public String color;

    @Expose
    public int id;

    @Expose
    public int pid;

    @Expose
    public int layoutType;

    @Expose
    public int wenjian;

    @Expose
    public int receiver;

    @Expose
    public int amount_like;

    @Expose
    public int amount_focus;

    @Expose
    public int creator;

    @Expose
    public int rank;


    @Expose
    public int parent;

    @Expose
    public int isdz;

    @Expose
    public int complete;

    @Expose
    public int isgz;

    @Expose
    public int my;

    @Expose
    public int paixu;

    @Expose
    public int beyond;

    @Expose
    public int wancheng;//0未完成 1完成

    @Expose
    public String title;

    @Expose
    public String week;

    @Expose
    public String name;

    @Expose
    public String img;

    @Expose
    public String content;

    @Expose
    public String neirong;

    @Expose
    public String mingcheng;

    @Expose
    public String leixing;

    @Expose
    public String daxiao;

    @Expose
    public String time;

    @Expose
    public String titleNum;

    @Expose
    public String c1;//标题时间

    @Expose
    public String c2;//赞

    @Expose
    public ArrayList<TaskDetail> sub;

    @Expose
    public ArrayList<TaskDetail> file;

    @Expose
    public ArrayList<TaskDetail> comment;

    @Expose
    public ArrayList<TaskDetail> trend;


}
