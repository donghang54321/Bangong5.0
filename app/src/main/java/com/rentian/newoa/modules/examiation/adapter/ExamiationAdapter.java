package com.rentian.newoa.modules.examiation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.modules.examiation.bean.Examiation;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/19.
 */
public class ExamiationAdapter extends BaseAdapter {
    private ArrayList<Examiation> data;

    private Context context;

    public ExamiationAdapter(Context context, ArrayList<Examiation> data) {
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
        convertView= LayoutInflater.from(context).inflate(R.layout.item_examination_list,null);
        TextView tvType= (TextView) convertView.findViewById(R.id.examination_subject);
        TextView tvTime= (TextView) convertView.findViewById(R.id.examination_date);
        TextView tvName= (TextView) convertView.findViewById(R.id.examination_publisher);

        tvName.setText("申请人："+data.get(position).name);
        tvTime.setText("时间："+data.get(position).time1);
        tvType.setText(data.get(position).type);
        return convertView;
    }
}
