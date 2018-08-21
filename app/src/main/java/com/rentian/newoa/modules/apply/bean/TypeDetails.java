package com.rentian.newoa.modules.apply.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by rentianjituan on 2016/4/12.
 */
public class TypeDetails {

    @Expose
    public FieldOptions field_options;

    @Expose
    public String field_type;

    @Expose
    public String label;

    @Expose
    public boolean required;

    @Expose
    public int templateid;

    @Expose
    public int comid;
}
