package com.rentian.newoa.modules.communication.bean;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AddressBook {
    @Expose
    public String deptname;

    @Expose
    public List<EmployeeInfoByNet> list;
}