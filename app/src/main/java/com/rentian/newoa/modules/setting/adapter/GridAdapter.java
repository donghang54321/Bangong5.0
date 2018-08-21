package com.rentian.newoa.modules.setting.adapter;

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
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2018/7/26.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.MyViewHolder> {
    private ArrayList<DataList> mData;
    private LayoutInflater mLayoutInflater;
    private int mCid;

    public GridAdapter(Context context, ArrayList<DataList> data
            , int cid) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        mCid = cid;

    }


    @Override
    public GridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GridAdapter.MyViewHolder holder = new GridAdapter.MyViewHolder(mLayoutInflater.inflate
                (R.layout.item_icon, parent,
                        false));
        return holder;
    }

    @Override
    public void onBindViewHolder(GridAdapter.MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Glide.with(mLayoutInflater.getContext())
                .load(Const.RTOA.URL_BASE + mData.get(position).img)
                .into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            iv = view.findViewById(R.id.iv_icon);
        }
    }
}
