package com.rentian.newoa.modules.meeting.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.modules.meeting.bean.Member;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/26.
 */
public class SpeakerAdapter extends BaseAdapter {
    private ArrayList<Member> onlineSpeaker;
    private Context context;

    public SpeakerAdapter(ArrayList<Member> onlineSpeaker, Context context) {
        this.onlineSpeaker = onlineSpeaker;
        this.context = context;
    }

    @Override
    public int getCount() {
        return onlineSpeaker.size();
    }

    @Override
    public Object getItem(int position) {
        return onlineSpeaker.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item_meeting_member, null);
        if (onlineSpeaker.size() > 0) {
            convertView.setVisibility(View.VISIBLE);
            TextView name = (TextView) convertView.findViewById(R.id.member_name);
            name.setTextColor(Color.WHITE);
            try {
                name.setText(onlineSpeaker.get(position).name);
            }catch (Exception e){
                convertView.setVisibility(View.GONE);
            }


        } else {
            convertView.setVisibility(View.GONE);

        }
        return convertView;
    }
}
