package com.rentian.newoa.modules.apply.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/12.
 */
public class ApplyForm {
    @Expose
    public ArrayList<TypeDetails> list;

    @Expose
    public String name;
}
