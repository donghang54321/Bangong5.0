package com.rentian.newoa.modules.todolist.imodules;

import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TestData;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by rentianjituan on 2018/3/5.
 */

public interface ItemTouchHelperAdapter {
    //数据交换
    void onItemMove(int fromPosition, int toPosition);

    //数据删除
    void onItemDissmiss(int position);

    //获得全选状态
    void updateData(int fromPosition, int toPosition);

    //获得data
    ArrayList<DataList> getData();

    //同步数据
    void updateShuju(Map<String, String> hashMap);

    //更改背景色
    void setBg(int position);

}
