package com.rentian.newoa.modules.communication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.rentian.newoa.R;
import com.rentian.newoa.modules.communication.bean.EmployeeInfo;

import java.util.ArrayList;
import java.util.Collections;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {

    private Context mContext;
    private ArrayList<EmployeeInfo> datas;


    public ContactAdapter(Context context, ArrayList<EmployeeInfo> mDatas) {
        this.mContext = context;
        this.datas = mDatas;
        if (datas == null) {
            datas = new ArrayList<>();
        }
        Collections.sort(datas);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_list, parent,
                    false);
            holder.name = (TextView) convertView
                    .findViewById(R.id.contact_name);
            holder.contactPosition = (TextView) convertView
                    .findViewById(R.id.contact_position);
            holder.tvLetter = (TextView) convertView
                    .findViewById(R.id.contact_catalog);
            holder.touxiang =  convertView
                    .findViewById(R.id.contact_avatar_iv);
            holder.touxiangTv = (TextView) convertView
                    .findViewById(R.id.contact_avatar_tv);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(datas.get(position).getName());
        holder.contactPosition.setText(datas.get(position).getPosition());
        //如果是第0个那么一定显示#号
        if (position == 0) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText("" + datas.get(position).getFirstChar());
        } else {
            //如果和上一个item的首字母不同，则认为是新分类的开始
            char a = datas.get(position).getFirstChar();
            char b = datas.get(position - 1).getFirstChar();
            if (a != b) {
                holder.tvLetter.setVisibility(View.VISIBLE);
                holder.tvLetter.setText("" + datas.get(position).getFirstChar());
            } else {
                holder.tvLetter.setVisibility(View.GONE);
            }
        }
//        if (datas.get(position).getImg().length() > 0) {
//            holder.touxiangTv.setText("");
//            String imageUrl = Const.RTOA.URL_BASE + datas.get(position).getImg();
//            Glide.with(mContext)
//                    .load(imageUrl)
//                    .into(holder.touxiang);
//        } else {
//            int lottery = datas.get(position).getUid() % 7;
//            Glide.with(mContext)
//                    .load(colors[lottery])
//                    .into(holder.touxiang);
//            String name = datas.get(position).getName();
//            holder.touxiangTv.setText(name.substring(name.length() - 1));
//        }
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = datas.get(i).getFirstChar();
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        EmployeeInfo item = datas.get(position);
        return item.getFirstChar();
    }

    public static class Holder {
        public TextView name;
        public TextView contactPosition;
        public TextView tvLetter;
        public ImageView touxiang;
        public TextView touxiangTv;
    }
}
