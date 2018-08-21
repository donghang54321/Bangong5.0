package com.rentian.newoa.modules.examiation.view.iview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.rentian.newoa.common.view.FillListView;
import com.rentian.newoa.common.view.timeline.adapter.TimeLineAdapter;
import com.rentian.newoa.common.view.timeline.bean.TimeLineItem;

import java.util.ArrayList;

/**
 * 时间轴控件封装
 */
public class TimeLineListView extends FrameLayout {

    private Context mContext;

    private FillListView listView;

    private TimeLineAdapter adapter;
    private ArrayList<TimeLineItem> mData;

    public TimeLineListView(Context context) {
        this(context, null);
    }

    public TimeLineListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeLineListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,//
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0, 0);
        listView = new FillListView(mContext);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setBackgroundColor(0xffffff);
        listView.setLayoutParams(layoutParams);
        addView(listView);
    }

    public void setAdapter(ArrayList<TimeLineItem> data) {
        this.mData = data;
        if (adapter == null) {
            adapter = new TimeLineAdapter(mContext, mData);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}
