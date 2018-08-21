package com.rentian.newoa.common.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rentian.newoa.R;
import com.rentian.newoa.modules.todolist.adapter.PoplistAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.zyyoona7.popup.BasePopup;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2018/8/14.
 */

public class ComplexPopup extends BasePopup<ComplexPopup> {
    private static final String TAG = "ComplexPopup";

    private RecyclerView mRecyclerView;
    private PoplistAdapter mComplexAdapter;
    private Context mContext;

    public static ComplexPopup create(Context context) {
        return new ComplexPopup(context);
    }

    protected ComplexPopup(Context context) {
        mContext = context;
        setContext(context);
    }


    @Override
    protected void initAttributes() {
        setContentView(R.layout.pop_recycleview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);

    }
    private ArrayList<DataList> mGongsi = new ArrayList<>();
    private PoplistAdapter mPoplistAdapter;
    @Override
    protected void initViews(View view, ComplexPopup basePopup) {
        mRecyclerView = findViewById(R.id.recycler_view);


    }

    public void setAbc(ArrayList<DataList> mGongsi ) {
        mPoplistAdapter = new PoplistAdapter(mContext, mGongsi, 1, null);
        mRecyclerView.setAdapter(mPoplistAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                mContext, LinearLayoutManager.VERTICAL, 1, R.color.task_text2));
    }

}
