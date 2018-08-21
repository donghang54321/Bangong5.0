package com.rentian.newoa.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.view.lunboRecyclerView.CardAdapterHelper;
import com.rentian.newoa.modules.todolist.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ToastUtils;

/**
 * Created by rentianjituan on 2018/8/2.
 */

public class CardAdapter  extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<String> mList = new ArrayList<>();
    public CardAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(holder.mImageView.getContext())
                .load(Const.RTOA.URL_BASE + mList.get(position))
                .into(holder.mImageView);
//        holder.mImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ToastUtils.show(holder.mImageView.getContext(), "" + position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

}
