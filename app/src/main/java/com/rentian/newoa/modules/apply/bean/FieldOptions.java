package com.rentian.newoa.modules.apply.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by rentianjituan on 2016/4/12.
 */
public class FieldOptions {
    @Expose
    public String description;

    @Expose
    public List<Option> options;

    @Expose
    public String size;
}
