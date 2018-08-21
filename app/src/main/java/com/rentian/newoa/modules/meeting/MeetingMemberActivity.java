package com.rentian.newoa.modules.meeting;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.view.RefreshLayout;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.meeting.adapter.RestMeetingMemberAdapter;
import com.rentian.newoa.modules.meeting.bean.Member;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by rentianjituan on 2016/6/8.
 */
public class MeetingMemberActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private RefreshLayout myRefreshListView;
    private RestMeetingMemberAdapter adapter;
    private ArrayList<Member> data = new ArrayList<>();
    private ArrayList<Member> data2 = new ArrayList<>();
    private WhorlView loadView;
    private LinearLayout loadLayout;
    private TextView tittle;
    private View view;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_stock_detai_list);
        tittle = (TextView) findViewById(R.id.title);
        tittle.setText("参会成员");
        ImageView ivDownArrow = (ImageView) findViewById(R.id.iv_down_arrow);
        ivDownArrow.setVisibility(View.VISIBLE);
        tittle.setOnClickListener(this);
        view = LayoutInflater.from(this).inflate(R.layout.menu_report, null);
        TextView title_month = (TextView) view.findViewById(R.id.title1);
        TextView title_week = (TextView) view.findViewById(R.id.title2);
        title_month.setText("在线成员");
        title_week.setText("离线成员");
        dialog = new AlertDialog.Builder(this).setView(view).create();
        dialog.getWindow().setGravity(Gravity.TOP);

        lv = (ListView) findViewById(R.id.lv);
        loadView = (WhorlView) findViewById(R.id.loading_view);
        loadLayout = (LinearLayout) findViewById(R.id.loading_layout);

        myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (tittle.getText().toString().equals("离线成员")) {
                    updateOff();
                } else {
                    update();
                }
            }
        });

        // 加载监听器
        loadView.start();
        update();

    }

    private void update() {
        UpdateTextTask updateTextTask = new UpdateTextTask();
        updateTextTask.execute(0);
    }

    private void updateOff() {
        UpdateTextTask updateTextTask = new UpdateTextTask();
        updateTextTask.execute(1);
    }


    private boolean isOnce = true;//adapter
    private boolean once = true;//dialog

    @Override
    public void onClick(View v) {
        if (v == tittle) {
            if (once) {
                WindowManager m = getWindowManager();
                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
                dialog.show();
                WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
                if (d.getWidth() * 0.5 < 300) {
                    p.width = (int) (d.getWidth() * 0.6);
                } else {
                    p.width = (int) (d.getWidth() * 0.5);
                }
                dialog.getWindow().setAttributes(p);
                once = false;
//                Log.e("view.getWidth()", d.getWidth() * 0.5 + "");
            } else {
                dialog.show();
            }
        }
    }

    public void titleClick1(View view) {
        tittle.setText("离线成员");
        isOnce = true;
        adapter = null;
        updateOff();
        dialog.dismiss();

    }

    public void titleClick2(View view) {
        tittle.setText("在线成员");
        isOnce = true;
        adapter = null;
        update();
        dialog.dismiss();

    }

    class UpdateTextTask extends AsyncTask<Integer, Integer, Integer> {
        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */

        @Override
        protected Integer doInBackground(Integer... params) {
            if (params[0] == 0) {
                String request = HttpURLConnHelper.requestByPOST(Const.RTOA.URL_MEETING_ROOM_ON,
                        "roomid=" + MyApp.getInstance().getMyRoomId());

                Type type = new TypeToken<ArrayList<Member>>() {
                }.getType();
                Log.e("json", request);
                ArrayList<Member> list = CommonUtil.gson.fromJson(request, type);
                data.clear();
                data.addAll(list);
                return 0;
            } else {
                String request = HttpURLConnHelper.requestByGET(Const.RTOA.URL_TEST_OFFLINE
                        +"?roomid="+MyApp.getMyRoomId()
                );
                Log.e("离线人", request);
                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();

                ArrayList<String> data = CommonUtil.gson.fromJson(request, type);
                data2.clear();
                for (int i=0;i<data.size();i++){
                    Member member=new Member();
                    member.name=data.get(i);
                    data2.add(member);
                }

                return 1;
            }

        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == 0) {
                if (isOnce) {
                    setViewGone(loadLayout);
                    isOnce = false;
                    if (adapter == null) {
                        adapter = new RestMeetingMemberAdapter(MeetingMemberActivity.this, data);
                        lv.setAdapter(adapter);
                    }
                } else {
                    myRefreshListView.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (isOnce) {
                    setViewGone(loadLayout);
                    isOnce = false;
                    if (adapter == null) {
                        adapter = new RestMeetingMemberAdapter(MeetingMemberActivity.this, data2);
                        lv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    myRefreshListView.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void setViewGone(final View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 0f, 1f, 0f,//
                loadView.getWidth() / 2,//
                loadView.getHeight() / 2);
        scaleAnimation.setDuration(1000);
        view.startAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                loadView.stop();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void onBack(View v) {
        finish();
    }
}
