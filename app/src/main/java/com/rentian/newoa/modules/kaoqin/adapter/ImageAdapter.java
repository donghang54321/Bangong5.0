package com.rentian.newoa.modules.kaoqin.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.LQRPhotoSelectUtils;
import com.rentian.newoa.modules.kaoqin.bean.PicData;
import com.rentian.newoa.modules.kaoqin.view.DakaActivity;
import com.rentian.newoa.modules.todolist.bean.DataList;

import java.util.ArrayList;

import kr.co.namee.permissiongen.PermissionGen;

/**
 * Created by rentianjituan on 2018/8/20.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private ArrayList<PicData> mData;
    private LayoutInflater mLayoutInflater;

    public ImageAdapter(Context context, ArrayList<PicData> data
            ) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;

    }


    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageAdapter.MyViewHolder holder = new ImageAdapter.MyViewHolder(mLayoutInflater.inflate
                (R.layout.item_iv_xiangji, parent,
                        false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.MyViewHolder holder, final int position) {
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData.get(position).img.equals("1")){
                    PermissionGen.with((DakaActivity) mLayoutInflater.getContext())
                            .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA
                            ).request();
                }
            }
        });
        if (mData.get(position).img.equals("1")){
//            Glide.with(mLayoutInflater.getContext())
//                    .load(R.drawable.xiangji)
//                    .into(holder.iv);
            holder.iv.setImageResource(R.drawable.xiangji);
        }else {
            holder.iv.setImageBitmap(mData.get(position).bt);
        }
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