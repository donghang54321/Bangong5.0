package com.rentian.newoa.modules.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rentian.newoa.R;
import com.rentian.newoa.modules.contactlist.view.SearchActivity;
import com.rentian.newoa.modules.login.view.RegisterActivity;
import com.rentian.newoa.modules.task.iview.SetZhuyemian;
import com.rentian.newoa.modules.team.view.AddteamActivity;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;
import com.rentian.newoa.modules.todolist.view.Main2Activity;

import java.util.ArrayList;

/**
 * Created by rentianjituan on 2018/6/15.
 */

public class PoplistAdapter extends RecyclerView.Adapter<PoplistAdapter.MyViewHolder> {
    private ArrayList<DataList> mData;
    private LayoutInflater mLayoutInflater;

    private int mCid;
    private SetActionBarListener mSetActionBarListener;

    public PoplistAdapter(Context context, ArrayList<DataList> data
            , int cid, SetActionBarListener setActionBarListener) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
        mCid = cid;
        mSetActionBarListener = setActionBarListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).titleType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = null;
        if (viewType == 0) {
            holder = new MyViewHolder(mLayoutInflater.inflate
                    (R.layout.item_recycleciew_pop, parent,
                            false));
        } else if (viewType == 1) {
            holder = new MyViewHolder(mLayoutInflater.inflate
                    (R.layout.item_addteam, parent,
                            false));
        } else if (viewType == 2) {
            holder = new MyViewHolder(mLayoutInflater.inflate
                    (R.layout.bg_line, parent,
                            false));
        }else if (viewType == 3) {
            holder = new MyViewHolder(mLayoutInflater.inflate
                    (R.layout.item_search_team, parent,
                            false));
        }else if (viewType == 4) {
            holder = new MyViewHolder(mLayoutInflater.inflate
                    (R.layout.item_address, parent,
                            false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (holder.getItemViewType() == 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCid = mData.get(position).qiye;
                    mSetActionBarListener.setActionBar(position, "1");
                    notifyDataSetChanged();
                }
            });
            holder.tv.setText(mData.get(position).mingcheng);
            if (mData.get(position).qiye == mCid) {
                holder.iv.setVisibility(View.VISIBLE);
            } else {
                holder.iv.setVisibility(View.GONE);
            }
        } else if (holder.getItemViewType() == 1) {
            if (mData.get(position).typeId == 1) {
                holder.tvContent.setText("创建新团队");
                holder.iv.setImageResource(R.drawable.plus);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mLayoutInflater.getContext()
                                , RegisterActivity.class);
                        i.putExtra("type", 6);
                        ((Main2Activity) mLayoutInflater.getContext())
                                .startActivityForResult(i, Main2Activity.REQUESTCODE);
                    }
                });
            } else if (mData.get(position).typeId == 2) {
                holder.tvContent.setText("加入一个团队");
                holder.iv.setImageResource(R.drawable.tuandui);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(mLayoutInflater.getContext()
                                , AddteamActivity.class);
                        mLayoutInflater.getContext()
                                .startActivity(i);
                    }
                });
            }
        } else if (holder.getItemViewType() == 2) {

        }else if (holder.getItemViewType() == 3) {
            holder.tv.setText(mData.get(position).name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSetActionBarListener.setActionBar(mData.get(position).id,"");
                }
            });
        }else if (holder.getItemViewType() == 4) {
            holder.tv.setText(mData.get(position).name);
            holder.tvContent.setText(mData.get(position).content);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSetActionBarListener.setActionBar(position,mData.get(position).name);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv, tvContent;
        ImageView iv;

        public MyViewHolder(View view) {
            super(view);
            tvContent = view.findViewById(R.id.tv_creatteam);
            tv = view.findViewById(R.id.tv_gc_name);
            iv = view.findViewById(R.id.iv_check);
        }
    }
}

