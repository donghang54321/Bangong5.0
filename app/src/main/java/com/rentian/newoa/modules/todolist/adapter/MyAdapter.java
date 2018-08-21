package com.rentian.newoa.modules.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.ExtendedEditText;
import com.rentian.newoa.common.view.RefreshLayout;
import com.rentian.newoa.common.view.SwipeMenuLayout2;
import com.rentian.newoa.modules.task.iview.SetZhuyemian;
import com.rentian.newoa.modules.task.view.TaskDetailActivity;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.imodules.ItemTouchHelperAdapter;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rentianjituan on 2018/3/5.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {
    //数据
    private ArrayList<DataList> mData;
    private LayoutInflater mLayoutInflater;
    public boolean isEditer;
    private SetActionBarListener mSetActionBarListener;
    private SetZhuyemian setZhuyemian;

    public MyAdapter(Context context, ArrayList<DataList> data
            , SetZhuyemian setZhuyemian) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
//        mSetActionBarListener = setActionBarListener;
        this.setZhuyemian = setZhuyemian;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        notifyItemMoved(fromPosition, toPosition);
        Collections.swap(mData, fromPosition, toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("id", mData.get(position).id + "");
        hashMap.put("complete", "1");
        hashMap.put("huadong", "1");
//        mData.remove(position);
//        notifyItemRemoved(position);
        submitTask(Const.RTOA.URL_ZHUTASK_SUBMIT, hashMap, position);

    }

    @Override
    public void updateData(int p1, int p2) {

        notifyItemRangeChanged(p1, p2);
    }

    @Override
    public ArrayList<DataList> getData() {
        return mData;
    }

    @Override
    public void updateShuju(Map<String, String> hashMap) {
        doHttpAsync(Const.RTOA.URL_TODOLIST_TUODONG, hashMap);
    }

    @Override
    public void setBg(int position) {
//        if (rlMap.size() > 0)
//            for (Integer x : rlMap.keySet()) {
//                if (position!=x)
//                (rlMap.get(x)).setBackgroundColor(
//                        mLayoutInflater.getContext().
//                                getResources().getColor(R.color.tab_top_text_1));
//            }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).titleType;
    }

    public void setEditer(boolean isEditer) {
        this.isEditer = isEditer;
    }

//    private Map<Integer, RelativeLayout> rlMap = new HashMap<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new MyAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_title1, parent, false));
        } else if (viewType == 1 || viewType == 2) {
            return new NormalItem(mLayoutInflater.inflate(R.layout.item_task1, parent, false));
        } else {
            return new MyAdapter.NormalItem(mLayoutInflater.inflate(R.layout.item_task1, parent, false));

        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder.getItemViewType() == 1 ||
                holder.getItemViewType() == 2) {
            ((NormalItem) holder).viewColor.setBackgroundColor
                    (Color.parseColor(mData.get(position).color));
//            rlMap.put(position, ((NormalItem) holder).relativeLayout);
            ((NormalItem) holder).tvEndtime.setText(mData.get(position).endTime);
            ((NormalItem) holder).itemView.setText(mData.get(position).title
                    + "这个任务大家伙都会一起的见客户电话号还得跟电话号");
            ((NormalItem) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(
                            mLayoutInflater.getContext(), TaskDetailActivity.class);
                    if (mData.get(position).img!=null)
                    intent.putExtra("img",mData.get(position).img );
                    intent.putExtra("id",mData.get(position).id );
                    mLayoutInflater.getContext().startActivity(intent);
                }

            });

            if (mData.get(position).wc != 1) {
                if (mData.get(position).beyond==0){
                    ((NormalItem) holder).tvEndtime.setTextColor(
                            mLayoutInflater.getContext().getResources().
                            getColor(R.color.task_text1));
                }else {
                    ((NormalItem) holder).tvEndtime.setTextColor(
                            mLayoutInflater.getContext().getResources().
                            getColor(R.color.head_image_2));
                }
                ((NormalItem) holder).btRank1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("rank", "1");
                        hashMap.put("id", mData.get(position).id + "");
                        doHttpAsync(Const.RTOA.URL_SET_RANK, hashMap);
                        ((NormalItem) holder).swipeMenu.smoothClose();
                        ((NormalItem) holder).viewColor.setBackgroundColor
                                (ContextCompat.getColor
                                        (mLayoutInflater.getContext(), R.color.rank1));
                        mData.get(position).color="#cfcec7";
                    }
                });
                ((NormalItem) holder).btRank2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("rank", "2");
                        hashMap.put("id", mData.get(position).id + "");
                        doHttpAsync(Const.RTOA.URL_SET_RANK, hashMap);
                        ((NormalItem) holder).swipeMenu.smoothClose();
                        ((NormalItem) holder).viewColor.setBackgroundColor
                                (ContextCompat.getColor
                                        (mLayoutInflater.getContext(), R.color.rank2));
                        mData.get(position).color="#f4ca38";
                    }
                });
                ((NormalItem) holder).btRank3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("rank", "3");
                        hashMap.put("id", mData.get(position).id + "");
                        doHttpAsync(Const.RTOA.URL_SET_RANK, hashMap);
                        ((NormalItem) holder).swipeMenu.smoothClose();
                        ((NormalItem) holder).viewColor.setBackgroundColor
                                (ContextCompat.getColor
                                        (mLayoutInflater.getContext(), R.color.rank3));
                        mData.get(position).color="#e69b3a";
                    }
                });
                ((NormalItem) holder).btRank4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> hashMap = new HashMap<>();
                        hashMap.put("rank", "4");
                        hashMap.put("id", mData.get(position).id + "");
                        doHttpAsync(Const.RTOA.URL_SET_RANK, hashMap);
                        ((NormalItem) holder).swipeMenu.smoothClose();
                        ((NormalItem) holder).viewColor.setBackgroundColor
                                (ContextCompat.getColor
                                        (mLayoutInflater.getContext(), R.color.rank4));
                        mData.get(position).color="#bc4545";
                    }
                });
            }


            if (mData.get(position).wc == 1) {
                ((NormalItem) holder).checkBox.setChecked(true);
            } else if (mData.get(position).wc == 0) {
                ((NormalItem) holder).checkBox.setChecked(false);
            }
            ((NormalItem) holder).checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((NormalItem) holder).checkBox.isChecked()) {
                        if (mData.get(position).wc == 0) {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", mData.get(position).id + "");
                            hashMap.put("complete", "1");
                            hashMap.put("huadong", "0");
                            submitTask(Const.RTOA.URL_ZHUTASK_SUBMIT, hashMap, position);
                        }
//                        editText1.setText(buttonView.getText()+"选中");
                    } else {
                        if (mData.get(position).wc == 1) {
                            Map<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("id", mData.get(position).id + "");
                            hashMap.put("complete", "0");
                            hashMap.put("huadong", "0");
                            submitTask(Const.RTOA.URL_ZHUTASK_SUBMIT, hashMap, position);
                        }
//                        editText1.setText(buttonView.getText()+"取消选中");
                    }
                }
            });

            if (mData.get(position).img == null) {
                ((NormalItem) holder).touxiang.setImageResource(R.drawable.default_avatar);
            } else {
                Glide.with(mLayoutInflater.getContext())
                        .load(Const.RTOA.URL_BASE + mData.get(position).img)
                        .into(((NormalItem) holder).touxiang);
            }

            if (mData.get(position).tag == null) {
                ((NormalItem) holder).llTag.setVisibility(View.GONE);
            } else {
                ((NormalItem) holder).llTag.setVisibility(View.VISIBLE);
                ((NormalItem) holder).tvTag.setText(mData.get(position).tag);
            }

            if (mData.get(position).isTrend == 0) {
                ((NormalItem) holder).llDongtai.setVisibility(View.GONE);
            } else {
                ((NormalItem) holder).llDongtai.setVisibility(View.VISIBLE);
                ((NormalItem) holder).tvDongtai.setText(mData.get(position).isTrend + "");
            }

            if (mData.get(position).isLike == 0) {
                ((NormalItem) holder).llZan.setVisibility(View.GONE);
            } else {
                ((NormalItem) holder).llZan.setVisibility(View.VISIBLE);
                ((NormalItem) holder).tvZan.setText(mData.get(position).isLike + "");
            }

            if (mData.get(position).isComment == 0) {
                ((NormalItem) holder).llPinglun.setVisibility(View.GONE);
            } else {
                ((NormalItem) holder).llPinglun.setVisibility(View.VISIBLE);
                ((NormalItem) holder).tvPinglun.setText(mData.get(position).isComment + "");
            }

            if (mData.get(position).isFile == 0) {
                ((NormalItem) holder).llFile.setVisibility(View.GONE);
            } else {
                ((NormalItem) holder).llFile.setVisibility(View.VISIBLE);
                ((NormalItem) holder).tvFile.setText(mData.get(position).isFile + "");
            }
        } else if (holder.getItemViewType() == 0) {
            ((NormalItem) holder).itemView.setText(mData.get(position).typeName);

        }
        ((NormalItem) holder).swipeMenu
                .setIos(true)
                .setLeftSwipe(true)
                .setHuadong(false)
                .setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private ArrayList<RelativeLayout> rl = new ArrayList<>();

    public static class NormalItem extends RecyclerView.ViewHolder {
        public TextView itemView, tv, tvEndtime,
                tvDongtai, tvZan, tvFile, tvPinglun, tvTag;
        public ImageView iv;
        public SwipeMenuLayout2 swipeMenu;
        public CheckBox checkBox;
        //        public RadioButton radioButton;
        public RelativeLayout relativeLayout;
        public LinearLayout llDongtai, llZan, llFile, llPinglun, llTag;
        public CircleImageView touxiang;
        public ImageView viewColor;
        public ImageButton btRank1, btRank2, btRank3, btRank4;

        public NormalItem(View view) {
            super(view);
            viewColor = view.findViewById(R.id.view_color);
            itemView = view.findViewById(R.id.item);
            swipeMenu = view.findViewById(R.id.swipeMenu);
            tvEndtime = view.findViewById(R.id.endTime);
            tv = view.findViewById(R.id.tv_text);
            iv = view.findViewById(R.id.iv_img);
            checkBox = view.findViewById(R.id.checkbox);
            relativeLayout = view.findViewById(R.id.rl);
            touxiang = view.findViewById(R.id.touxiang);
            llDongtai = view.findViewById(R.id.ll_dongtai);
            llZan = view.findViewById(R.id.ll_like);
            llFile = view.findViewById(R.id.ll_file);
            llPinglun = view.findViewById(R.id.ll_pinglun);
            llTag = view.findViewById(R.id.ll_tag);

            tvDongtai = view.findViewById(R.id.tv_dongtai);
            tvZan = view.findViewById(R.id.tv_like);
            tvFile = view.findViewById(R.id.tv_file);
            tvPinglun = view.findViewById(R.id.tv_pinglun);
            tvTag = view.findViewById(R.id.tv_tag);

            btRank1 = (ImageButton) swipeMenu.getChildAt(4);
            btRank2 = (ImageButton) swipeMenu.getChildAt(3);
            btRank3 = (ImageButton) swipeMenu.getChildAt(2);
            btRank4 = (ImageButton) swipeMenu.getChildAt(1);


        }

    }

    private void doHttpAsync(String url, Map<String, String> hashMap) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        //// TODO: 2018/4/26 拖拽完成后

                    }
                });
    }

    private void submitTask(String url, final Map<String, String> hashMap, final int fromPosition) {

        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
//                            if ("0".equals(hashMap.get("huadong"))) {
                            mData.remove(fromPosition);
                            notifyItemRemoved(fromPosition);
//                            }
                            setZhuyemian.setWancheng(0);


                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }

                    }
                });

    }
}