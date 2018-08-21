package com.rentian.newoa.modules.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rentian.newoa.R;
import com.rentian.newoa.common.view.CircleImageView;
import com.rentian.newoa.modules.note.bean.AllReport;
import com.rentian.newoa.common.utils.DateUtil;
import java.util.List;


public class AllReportAdapter extends BaseAdapter {
    private Context context;
    private List<AllReport> allReports;


    public AllReportAdapter(Context context, List<AllReport> allReports) {
        this.context = context;
        this.allReports = allReports;
    }

    @Override
    public int getCount() {

        return allReports.size();
    }

    @Override
    public Object getItem(int position) {

        return allReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_report, null,
                    false);
            holder.employeeNname = (TextView) convertView
                    .findViewById(R.id.report_name);
            holder.publishDate = (TextView) convertView
                    .findViewById(R.id.report_time);
            holder.dailyContent = (TextView) convertView
                    .findViewById(R.id.report_message);
            holder.employeePost = (TextView) convertView
                    .findViewById(R.id.report_position);
            holder.tvDmLast = (TextView) convertView
                    .findViewById(R.id.tv_report_last_text);
            holder.ivTouxiang = (CircleImageView) convertView
                    .findViewById(R.id.report_avatar_iv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
//        if (allReports.get(position).img.length() > 0) {
//            holder.tvDmLast.setText("");
//            String imageUrl = Const.RTOA.URL_BASE + allReports.get(position).img;
//            Glide.with(context)
//                    .load(imageUrl)
//                    .into(holder.ivTouxiang);
//        } else {
//            int lottery = allReports.get(position).uid % 7;
//            holder.tvDmLast.setText(allReports.get(position).name
//                    .substring(allReports.get(position).name.length() - 1));
//            Glide.with(context)
//                    .load(colors[lottery])
//                    .into(holder.ivTouxiang);
//        }
        holder.employeeNname.setText(allReports.get(position).name);
        holder.publishDate.setText(DateUtil.formatTimeInMillis(allReports.get(position).time));
        holder.dailyContent.setText(allReports.get(position).c1+"\n"
                +allReports.get(position).c2);
        holder.employeePost.setText(allReports.get(position).post);


        return convertView;
    }

    public static class Holder {
        public TextView employeeNname;
        public TextView publishDate;
        public TextView dailyContent;
        public TextView employeePost;
        public CircleImageView ivTouxiang;
        public TextView tvDmLast;
    }
}
