package com.rentian.newoa.modules.meeting.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rentian.newoa.R;
import com.rentian.newoa.modules.meeting.bean.Message;
import com.rentian.newoa.modules.meeting.view.CCPTextView;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/7/21.
 */
public class RoomChatAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Message> message;
    private String myName;

    public RoomChatAdapter(Context context, ArrayList<Message> message) {
        this.context = context;
        this.message = message;
        SharedPreferences sp = context.getSharedPreferences("user_info",
                Activity.MODE_PRIVATE);
        myName=sp.getString("username", "");
    }

    @Override
    public int getCount() {
        return message.size();
    }

    @Override
    public Object getItem(int position) {
        return message.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (message.size()>0) {
            if (myName.equals(message.get(position).name)) {
                convertView = LayoutInflater.from(context).inflate(R.layout.ytx_chatting_item_to, null);
                CCPTextView tvContent = (CCPTextView) convertView.findViewById(R.id.chatting_content_itv);
                tvContent.setText(message.get(position).content);

            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.room_chatting_from, null);
                TextView tvName = (TextView) convertView.findViewById(R.id.tv_name_from);
                CCPTextView tvContent = (CCPTextView) convertView.findViewById(R.id.chatting_content_itv);
                tvName.setText(message.get(position).name);
                tvContent.setText(message.get(position).content);
            }
        }else {
            convertView = LayoutInflater.from(context).inflate(R.layout.ytx_chatting_item_to, null);
        }
        return convertView;
    }
}
