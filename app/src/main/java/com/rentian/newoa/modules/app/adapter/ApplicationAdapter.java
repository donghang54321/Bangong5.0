package com.rentian.newoa.modules.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.modules.todolist.adapter.MyAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.imodules.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by rentianjituan on 2018/6/28.
 */

public class ApplicationAdapter extends RecyclerView.Adapter
        <ApplicationAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private ArrayList<DataList> mApplist;

    public ApplicationAdapter(Context mContext, ArrayList<DataList> applist) {
        this.mContext = mContext;
        this.mApplist = applist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_app, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvName.setText(mApplist.get(position).name);
        holder.tvContent.setText(mApplist.get(position).content);
        Glide.with(mContext)
                .load(Const.RTOA.URL_BASE + mApplist.get(position).icon)
                .into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return mApplist.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        /**
         * 在这里进行给原数组数据的移动
         */
        Collections.swap(mApplist, fromPosition, toPosition);
        /**
         * 通知数据移动
         */
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onItemDissmiss(int position) {

    }

    @Override
    public void updateData(int fromPosition, int toPosition) {

    }

    @Override
    public ArrayList<DataList> getData() {
        return null;
    }

    @Override
    public void updateShuju(Map<String, String> hashMap) {

    }

    @Override
    public void setBg(int position) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvContent;
        ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.name);
            tvContent = view.findViewById(R.id.content);
            icon = view.findViewById(R.id.app_icon);

        }
    }
}
