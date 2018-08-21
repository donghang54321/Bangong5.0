package com.rentian.newoa.modules.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rentian.newoa.R;
import com.rentian.newoa.common.utils.DateUtil;
import com.rentian.newoa.common.view.CircleImageView;
import com.rentian.newoa.modules.note.bean.ResReport;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/3/7.
 */
public class ExamineAdapter extends BaseAdapter {
    private ArrayList<ResReport> data;
    private Context context;

    public ExamineAdapter(Context context, ArrayList<ResReport> data) {
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
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_report_details, null);
            holder.approverName =  convertView.findViewById(R.id.reporter_name);
            holder.publishDate =  convertView.findViewById(R.id.report_time);
            holder.dailyContent =  convertView.findViewById(R.id.tv_report_details);
            holder.ivTouxiang =  convertView.findViewById(R.id.report_avatar_iv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
//            int lottery = Integer.parseInt(data.get(position).uid) % 7;
//            holder.tvDmLast.setText(data.get(position).name
//                    .substring(data.get(position).name.length() - 1));
//            Glide.with(context)
//                    .load(colors[lottery])
//                    .into(holder.ivTouxiang);
        holder.approverName.setText(data.get(position).name);
        holder.publishDate.setText(DateUtil.formatTimeInMillis
                (data.get(position).time,"HH:mm"));
        holder.dailyContent.setText(data.get(position).content);

        return convertView;
    }

    private static class Holder {
         TextView approverName;
         TextView publishDate;
         TextView dailyContent;
         CircleImageView ivTouxiang;
         TextView tvDmLast;
    }
}