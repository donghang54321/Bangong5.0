package com.rentian.newoa.modules.meeting.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/7/21.
 */
public class RoomData {

    @Expose
    public ArrayList<Member> speaker;

    @Expose
    public ArrayList<Message> message;
}
