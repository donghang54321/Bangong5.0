package com.rentian.newoa.modules.meeting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rentian.newoa.R;
import com.rentian.newoa.modules.meeting.bean.Member;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/6/8.
 */
public class RestMeetingMemberAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Member> data;

    public RestMeetingMemberAdapter(Context context, ArrayList<Member> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate( R.layout.meeting_member_item, null);

        TextView mNikeName = (TextView) convertView.findViewById(R.id.meeting_contact_item_nick_tv);
        mNikeName.setText(data.get(position).name);


        return convertView;
    }
}
