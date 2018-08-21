package com.rentian.newoa.modules.apply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.modules.examiation.bean.ApplyData;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/7/11.
 */
public class MyApplyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ApplyData> data;

    public MyApplyAdapter(Context context, ArrayList<ApplyData> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ApplyData getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_apply_list,null);
        TextView tvTitel= (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvTime= (TextView) convertView.findViewById(R.id.tv_details);
        TextView tvStatus= (TextView) convertView.findViewById(R.id.tv_status);
        tvTitel.setText(data.get(position).name);
        tvTime.setText(data.get(position).time1);
        tvStatus.setText(data.get(position).status);
        return convertView;
    }
}
