package com.rentian.newoa.modules.contactlist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.imodules.ItemTouchHelperAdapter;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by rentianjituan on 2018/7/2.
 */

public class SearchAdapter extends RecyclerView.Adapter
        <SearchAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private SetActionBarListener setActionBarListener;

    public void setDataLists(ArrayList<DataList> dataLists) {
        this.dataLists = dataLists;
        notifyDataSetChanged();
    }

    private ArrayList<DataList> dataLists;

    public SearchAdapter(Context mContext, ArrayList<DataList> applist
            , SetActionBarListener setActionBarListener) {
        this.mContext = mContext;
        this.dataLists = applist;
        this.setActionBarListener = setActionBarListener;
    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SearchAdapter.MyViewHolder holder = new SearchAdapter.MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_renyuan, parent,
                false));
        return holder;
    }

    private int num;

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, final int position) {

        holder.tvName.setText(dataLists.get(position).name);
        holder.tvContent.setText(dataLists.get(position).post
                + "(" + dataLists.get(position).dept + ")");
        if (dataLists.get(position).img == null) {
            holder.icon.setImageResource(R.drawable.default_avatar);
        } else {
            Glide.with(mContext)
                    .load(Const.RTOA.URL_BASE + dataLists.get(position).img)
                    .into(holder.icon);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.checkBox.isChecked()) {
                    dataLists.get(position).titleType = 0;
                    MyApp.getInstance().idlist.remove(dataLists.get(position).id+"");
                    num--;
                } else {
                    dataLists.get(position).titleType = 1;
                    MyApp.getInstance().idlist.add(dataLists.get(position).id+"");
                    num++;
                }
                setActionBarListener.setActionBar(num,dataLists.get(position).img);
            }
        });
        if (dataLists.get(position).titleType == 0) {
            holder.checkBox.setChecked(false);
        } else {
            holder.checkBox.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        /**
         * 在这里进行给原数组数据的移动
         */
        Collections.swap(dataLists, fromPosition, toPosition);
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
        CircleImageView icon;
        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.name);
            tvContent = view.findViewById(R.id.content);
            icon = view.findViewById(R.id.app_icon);
            checkBox = view.findViewById(R.id.checkbox);
        }
    }
}