package com.rentian.newoa.modules.examiation.bean;

import com.google.gson.annotations.Expose;

/**
 * Created by rentianjituan on 2016/4/21.
 */
public class Node {
    @Expose
    public String cid;

    @Expose
    public String field_type;

    @Expose
    public String label;

    @Expose
    public boolean required;

    @Expose
    public String file;

    @Expose
    public FieldOptions field_options;

    @Expose
    public int comid;

    @Expose
    public int templateid;

    @Expose
    public int tfb;

}
