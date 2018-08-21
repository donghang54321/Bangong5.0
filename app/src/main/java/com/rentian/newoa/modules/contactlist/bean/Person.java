package com.rentian.newoa.modules.contactlist.bean;

import java.io.Serializable;

/**
 * Created by rentianjituan on 2018/7/2.
 */

public class Person implements Serializable {
    private String name;
    private String email;

    public Person(String n, String e) { name = n; email = e; }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return name; }
}