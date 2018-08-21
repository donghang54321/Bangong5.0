package com.rentian.newoa.common.view.timeline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.common.view.timeline.bean.TimeLineItem;
import com.rentian.newoa.common.view.timeline.bean.TimeLineItemType;
import com.rentian.newoa.common.view.timeline.view.TimeLineView;

import java.util.ArrayList;

/**
 * 时间轴适配器
 */
public class TimeLineAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TimeLineItem> mDatas;

    public TimeLineAdapter(Context context, ArrayList<TimeLineItem> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_timeline, parent, false);
        int type = getType(position);
        TimeLineView timeLineView = (TimeLineView) convertView.findViewById(R.id.timeLineView);
        if (type == TimeLineItemType.ATOM) {
            timeLineView.setBeginLine(null);
            timeLineView.setEndLine(null);
        } else if (type == TimeLineItemType.START) {
            timeLineView.setBeginLine(null);
        } else if (type == TimeLineItemType.END) {
            timeLineView.setEndLine(null);
        }
        if (null == mDatas.get(position).getIcon()) {
            timeLineView.setTimeLineMarker(mContext.getResources().getDrawable(R.drawable.timeline_marker));
            timeLineView.setTimeLineMarkerSize(timeLineView.getTimeLineMarkerSize() * 2 / 3);
        } else {
            timeLineView.setTimeLineMarker(mDatas.get(position).getIcon());
        }
        TextView title = (TextView) convertView.findViewById(R.id.time_line_title);
        title.setText(mDatas.get(position).getTitle());
        TextView detail = (TextView) convertView.findViewById(R.id.time_line_detail);
        detail.setText(mDatas.get(position).getDetail());
        TextView time = (TextView) convertView.findViewById(R.id.time_line_time);
        if (mDatas.get(position).getTime() == null ||
                "".equals(mDatas.get(position).getTime())) {
            time.setVisibility(View.GONE);
        } else {
            time.setText(mDatas.get(position).getTime());
        }
        return convertView;
    }

    private int getType(int position) {
        int size = mDatas.size() - 1;
        if (size == 0) {
            return TimeLineItemType.ATOM;
        } else if (position == 0) {
            return TimeLineItemType.START;
        } else if (position == size) {
            return TimeLineItemType.END;
        } else {
            return TimeLineItemType.NORMAL;
        }
    }
}
