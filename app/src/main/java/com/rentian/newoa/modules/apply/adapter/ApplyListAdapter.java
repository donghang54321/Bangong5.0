package com.rentian.newoa.modules.apply.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.modules.apply.bean.ApplyList;
import com.rentian.newoa.modules.apply.bean.ApplyListContext;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/12.
 */
public class ApplyListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ApplyList> data;
    private ArrayList<ApplyListContext> list = new ArrayList<>();

    public ApplyListAdapter(Context context, ArrayList<ApplyList> data) {
        this.context = context;
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            ApplyListContext context1 = new ApplyListContext();
            context1.name = data.get(i).name;
            list.add(context1);
            for (int j = 0; j < data.get(i).list.size(); j++) {
                ApplyListContext context2 = new ApplyListContext();
                context2.name = data.get(i).list.get(j).name;
                context2.id = data.get(i).list.get(j).id;
                list.add(context2);
            }
        }
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_apply_list_text, null);
        TextView tv = (TextView) convertView.findViewById(R.id.content);
        if (list.get(position).id == 0) {
            convertView.findViewById(R.id.title).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.title)).setText(list.get(position).name);
            convertView.findViewById(R.id.content).setVisibility(View.GONE);
            return convertView;
        }
        ((TextView) convertView.findViewById(R.id.content)).setText(list.get(position).name);

        return convertView;
    }
}
