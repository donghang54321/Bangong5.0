package com.rentian.newoa.modules.apply.bean;

import android.view.View;

/**
 * 存放申请页面中View和其类型
 */
public class ApplyView {

    private String type;

    private View view;

    public ApplyView(String type, View view) {
        this.type = type;
        this.view = view;
    }

    @Override
    public String toString() {
        return "ApplyView{" +
                "type='" + type + '\'' +
                ", view=" + view +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
