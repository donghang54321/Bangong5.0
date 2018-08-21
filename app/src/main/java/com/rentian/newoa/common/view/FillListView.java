package com.rentian.newoa.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class FillListView extends ListView {

    public FillListView(Context context) {
        super(context);
    }

    public FillListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
