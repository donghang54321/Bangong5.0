package com.rentian.newoa.modules.meeting.bean;

import com.google.gson.annotations.Expose;

public class Msg {
    @Expose
    public long id;
    @Expose
    public String subject;
    @Expose
    public String content;
    @Expose
    public String msg;
    @Expose
    public String time;
    @Expose
    public String name;
    @Expose
    public String mark;
    @Expose
    public int version;
    @Expose
    public int status;
}
