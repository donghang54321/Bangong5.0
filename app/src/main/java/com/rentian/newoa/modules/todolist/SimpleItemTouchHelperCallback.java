package com.rentian.newoa.modules.todolist;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.modules.todolist.adapter.MyAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TestData;
import com.rentian.newoa.modules.todolist.imodules.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by rentianjituan on 2018/3/5.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    private boolean isExpand;

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.RIGHT;

//        Log.e("isExpand", isExpand + "");
//        Log.e("getScrollXView", viewHolder.itemView.getScrollX() + "");
//        Log.e("getScrollX", ((MyAdapter.NormalItem) viewHolder).swipeMenu.getScrollX() + "");
//        Log.e("isHuadong", ((MyAdapter.NormalItem) viewHolder).swipeMenu.isHuadong() + "");
//        Log.e("isZhankai", MyApp.getInstance().isZhankai() + "");
        if (((MyAdapter.NormalItem) viewHolder).swipeMenu.getScrollX() == 0)
            ((MyAdapter.NormalItem) viewHolder).swipeMenu.setSwipeEnable(true);

        if (((MyAdapter.NormalItem) viewHolder).swipeMenu.getScrollX() != 0
                || ((MyAdapter.NormalItem) viewHolder).swipeMenu.getIsExpand()
                || ((MyAdapter.NormalItem) viewHolder).swipeMenu.isHuadong()
                || MyApp.getInstance().isZhankai()) {
            isExpand = MyApp.getInstance().isZhankai();

            dragFlags = 0;
        }
        if (((MyAdapter.NormalItem) viewHolder).swipeMenu.getScrollX() == 0) {
            MyApp.getInstance().setZhankai(false);
        }
        //TODO 设置能否拖拽
        if (viewHolder.getItemViewType() == 0) {

            return 0;
        } else if (viewHolder.getItemViewType() == 2) {
            swipeFlags = 0;
        }

        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean isLongPressDragEnabled() {

        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    private int postion1, postion2, wc, oldindex, oldid, oldtype, oldwc;
    private boolean loop = true;
    private ArrayList<DataList> oldData = new ArrayList<>();

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (target.getAdapterPosition() == 0 || viewHolder.getAdapterPosition() == -1) {
            return false;
        }

        if (loop) {

            oldData.clear();
            oldData.addAll(mAdapter.getData());
            postion1 = viewHolder.getAdapterPosition();
            oldindex = mAdapter.getData().get(postion1).index;
            oldid = mAdapter.getData().get(postion1).id;
            oldtype = mAdapter.getData().get(postion1).typeId;
            oldwc = mAdapter.getData().get(postion1).wc;
            loop = false;
        }
        postion2 = target.getAdapterPosition();
        if ((mAdapter.getData().get(target.getAdapterPosition()).typeId == -1 && oldwc == 0)
                || mAdapter.getData().get(target.getAdapterPosition()).wc == 1) {
//            ToastUtill.showMessage("更换");
            wc = 1;
            //// TODO: 2018/4/27
        } else if (mAdapter.getData().get(target.getAdapterPosition() - 1).typeId != -1
                && oldwc == 1) {
            wc = 2;
        } else {
            wc = 0;
        }
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if (direction == ItemTouchHelper.RIGHT)
            mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());


    }

    //限制ImageView长度所能增加的最大值
    private double ICON_MAX_SIZE = 50;
    //ImageView的初始长宽
    private int fixWidth = 75;

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        //重置改变，防止由于复用而导致的显示问题
        viewHolder.itemView.setScrollX(0);
//        ((MyAdapter.NormalItem) viewHolder).tv.setText("左滑删除");
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((MyAdapter.NormalItem) viewHolder).iv.getLayoutParams();
//        params.width = 75;
//        params.height = 75;
//        ((MyAdapter.NormalItem) viewHolder).iv.setLayoutParams(params);
        ((MyAdapter.NormalItem) viewHolder).iv.setVisibility(View.VISIBLE);
        ((MyAdapter.NormalItem) viewHolder).iv.setImageResource(R.
                mipmap.bt_ic_done_white_24);
        boolean zhankai = ((MyAdapter.NormalItem) viewHolder).swipeMenu.getIsExpand();
        ((MyAdapter.NormalItem) viewHolder).swipeMenu.setIos(true)
                .setLeftSwipe(true)
                .setHuadong(false)
                .setSwipeEnable(true);
        ((MyAdapter.NormalItem) viewHolder).swipeMenu.setIsExpand(zhankai);
        MyApp.getInstance().setZhankai(zhankai);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.itemView.setElevation(0f);
        }

        viewHolder.itemView.setBackgroundColor(
                viewHolder.itemView.getResources().getColor(R.color.tab_top_text_1));
        Log.e("getScrollX1", isExpand + "");
//        ((MyAdapter.NormalItem) viewHolder).radioButton.setChecked(false);
        Log.e("Posi", ((MyAdapter.NormalItem) viewHolder).swipeMenu.isSwipeEnable() + "1");
        if (is4) {
            is4 = false;
            if (viewHolder.getAdapterPosition() != -1)
                mAdapter.onItemDissmiss(viewHolder.getAdapterPosition());
        }
        if (postion1 != postion2) {
//            Map<String, String> hashMap = new HashMap<String, String>();
//            hashMap.put("id", "" + oldData.get(postion1).id);
//            hashMap.put("srcIndex", "" + oldData.get(postion1).index);
//            hashMap.put("srcSort", "" + oldData.get(postion1).typeId);
//            hashMap.put("desIndex", (mAdapter.getData()
//                    .get(viewHolder.getAdapterPosition() - 1).index + 1) + "");
//            hashMap.put("desSort", (mAdapter.getData()
//                    .get(viewHolder.getAdapterPosition() - 1).typeId) + "");
//            hashMap.put("wc", wc + "");
//
//            mAdapter.updateShuju(hashMap);
            if (wc == 0) {
                mAdapter.getData().get(viewHolder.getAdapterPosition()).typeId =
                        mAdapter.getData()
                                .get(viewHolder.getAdapterPosition() - 1).typeId;
            } else if (wc == 1) {
                mAdapter.getData().get(viewHolder.getAdapterPosition()).wc = 1;
                mAdapter.getData().get(viewHolder.getAdapterPosition()).titleType = 2;
            } else if (wc == 2) {
                mAdapter.getData().get(viewHolder.getAdapterPosition()).wc = 0;
                mAdapter.getData().get(viewHolder.getAdapterPosition()).titleType = 1;

            }
            Log.e("Posi", (mAdapter.getData()
                    .get(viewHolder.getAdapterPosition() - 1).typeId) + "");
//            Log.e("Posi", oldData.get(postion1).index+"&"
//                    +(mAdapter.getData().get(viewHolder.getAdapterPosition()-1).index+1));
            int p = 0;
            int wc1 = 0;
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                p++;

                if (mAdapter.getData().get(i).titleType == 0) {

                    mAdapter.getData().get(i).index = 0;
                    p = 0;
                    if (mAdapter.getData().get(i).typeId == -1) {
                        wc1 = 1;
                    }
                } else {
                    mAdapter.getData().get(i).wc = wc1;
                    mAdapter.getData().get(i).index = p;
                    mAdapter.getData().get(i).titleType = 1;
                }
                if (mAdapter.getData().get(i).wc == 1) {
                    mAdapter.getData().get(i).titleType = 2;
                }

            }
            Map<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("id", "" + oldid);
            hashMap.put("srcIndex", "" + oldindex);
            hashMap.put("srcSort", "" + oldtype);
            hashMap.put("desIndex", (mAdapter.getData()
                    .get(viewHolder.getAdapterPosition() - 1).index + 1) + "");
            hashMap.put("desSort", (mAdapter.getData()
                    .get(viewHolder.getAdapterPosition() - 1).typeId) + "");
            hashMap.put("wc", wc + "");

            mAdapter.updateShuju(hashMap);
            mAdapter.updateData(postion1, postion2);

        }
        if (viewHolder.getAdapterPosition() != -1) {
            if (mAdapter.getData().get(viewHolder.getAdapterPosition()).titleType == 2) {
                ((MyAdapter.NormalItem) viewHolder).checkBox.setChecked(true);
            } else {
                ((MyAdapter.NormalItem) viewHolder).checkBox.setChecked(false);
            }
            ((MyAdapter.NormalItem) viewHolder).viewColor.setBackgroundColor
                    (Color.parseColor(mAdapter.getData().
                            get(viewHolder.getAdapterPosition()).color));
        }
//        if (postion1!=postion2&&oldData.get(postion1).quyu!=oldData.get(postion2).quyu
//                ){
//            ToastUtill.showMessage("更换");
//            loop=true;
//
//        }
        loop = true;
        postion1 = postion2 = oldindex = 0;
    }

    ItemTouchHelper mtouchHelper;

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        mtouchHelper = touchHelper;
    }

    boolean is4;

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //仅对侧滑状态下的效果做出改变
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            //如果dX小于等于删除方块的宽度，那么我们把该方块滑出来
            if (Math.abs(dX) <= recyclerView.getWidth() / 4) {
                viewHolder.itemView.scrollTo(-(int) dX, 0);

                Log.e("500", getSlideLimitation(viewHolder) + "");
                if (isCurrentlyActive) {
                    is4 = false;
                    viewHolder.itemView.setBackgroundColor(
                            viewHolder.itemView.getResources().getColor(R.color.tab_top_text_1)
                    );
                }
            } else if (Math.abs(dX) > recyclerView.getWidth() / 4
                    && Math.abs(dX) <= recyclerView.getWidth() / 2) {
                viewHolder.itemView.scrollTo(-(int) dX, 0);


                if (isCurrentlyActive) {

                    is4 = true;
                    viewHolder.itemView.setBackgroundColor(
                            viewHolder.itemView.getResources().getColor(R.color.material_red)
                    );
                }
            }
            //如果dX还未达到能删除的距离，此时慢慢增加“眼睛”的大小，增加的最大值为ICON_MAX_SIZE
            else if (Math.abs(dX) > recyclerView.getWidth() / 2) {

//                double distance = (recyclerView.getWidth() / 2 - getSlideLimitation(viewHolder));
//                double factor = ICON_MAX_SIZE / distance;
//                double diff = (Math.abs(dX) - getSlideLimitation(viewHolder)) * factor;
//                if (diff >= ICON_MAX_SIZE)
//                    diff = ICON_MAX_SIZE;
                viewHolder.itemView.scrollTo(-(int) dX, 0);
                ((MyAdapter.NormalItem) viewHolder).tv.setText("");   //把文字去掉
                ((MyAdapter.NormalItem) viewHolder).iv.setVisibility(View.VISIBLE);  //显示眼睛
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ((MyAdapter.NormalItem) viewHolder).iv.getLayoutParams();
//                params.width = (int) (fixWidth + diff);
//                params.height = (int) (fixWidth + diff);
//                ((MyAdapter.NormalItem) viewHolder).iv.setLayoutParams(params);
                ((MyAdapter.NormalItem) viewHolder).iv.
                        setImageResource(R.mipmap.bt_ic_schedule_white_24);
                if (isCurrentlyActive) {
                    is4 = false;
                    viewHolder.itemView.setBackgroundColor(
                            viewHolder.itemView.getResources().getColor(R.color.material_red)
                    );
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    if (isCurrentlyActive) {
                        if (viewHolder.itemView.getElevation()!=0){
                            viewHolder.itemView.setElevation(10.0f);
                        }
                        if (dY == 0) {
//                            mAdapter.setBg(viewHolder.getAdapterPosition());
                            ((Vibrator) viewHolder.itemView.getContext().
                                    getSystemService(VIBRATOR_SERVICE)).vibrate(20);
                        }
                    }
                }
            }
            //拖拽状态下不做改变，需要调用父类的方法
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }


    }


    /**
     * 获取删除方块的宽度
     */
    public int getSlideLimitation(RecyclerView.ViewHolder viewHolder) {
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
        return viewGroup.getChildAt(1).getLayoutParams().width;
    }
}
