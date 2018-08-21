package com.rentian.newoa.modules.apply.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.view.RefreshLayout;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.apply.adapter.MyApplyAdapter;
import com.rentian.newoa.modules.examiation.adapter.ExamiationAdapter;
import com.rentian.newoa.modules.examiation.bean.ApplyData;
import com.rentian.newoa.modules.examiation.bean.Examiation;
import com.rentian.newoa.modules.examiation.view.ExaminationAcitity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class ApplyHistoryActivity extends AppCompatActivity {

    private ListView lv;
    private RefreshLayout myRefreshListView;
    private WhorlView loadView;
    private LinearLayout loadLayout;
    private int pn = 1;
    private ArrayList<Examiation> data=new ArrayList<>();
    private ArrayList<ApplyData> data2=new ArrayList<>();
    private ExamiationAdapter adapter;
    private MyApplyAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_apply_history);
        findViews();
        registerListeners();
        loadView.start();
        postAsync(getIntent().getIntExtra("type", 1));


    }

    private void findViews() {
        myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);
        loadView = (WhorlView) findViewById(R.id.loading_view);
        loadLayout = (LinearLayout) findViewById(R.id.loading_layout);
        lv = (ListView) findViewById(R.id.lv_ofe_notificatin);

    }

    private void registerListeners() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(ApplyHistoryActivity.this, ExaminationAcitity.class);
                if (getIntent().getIntExtra("type", 1) == 1) {
                    intent.putExtra("taskId", data.get(arg2).eventid+"");
                    intent.putExtra("type",1);
                } else {
                    intent.putExtra("taskId", data2.get(arg2).id+"");
                    intent.putExtra("type",1);
                }
                startActivity(intent);

            }
        });
        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

                postAsync(getIntent().getIntExtra("type", 1));

            }
        });
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                myRefreshListView.setRefreshing(false);
            }
        });
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
    private void postAsync(final int t) {
        String url=null;
        if (t==1){
            url=Const.RTOA.URL_EXAMINATIONHISTORY_LIST;
        }else {
            url = Const.RTOA.URL_MYAPPLYHISTORY_LIST;
        }
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(url)
                        .addParam("uid",MyApp.getInstance().getMyUid())
                        .addParam("uname",MyApp.getInstance().myName)
                        .addParam("pno",pn+"")
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("ApplyListActivity", info.getRetDetail());
                            if (t==1) {
                                Type type = new TypeToken<ArrayList<Examiation>>() {
                                }.getType();
                                ArrayList<Examiation> list =
                                        CommonUtil.gson.fromJson(info.getRetDetail(), type);
                                data.addAll(list);
                                if (adapter==null){
                                    adapter = new ExamiationAdapter(ApplyHistoryActivity.this, data);
                                    lv.setAdapter(adapter);
                                    setViewGone(loadLayout);
                                }
                                else {
                                    myRefreshListView.setLoading(false);
                                    adapter.notifyDataSetChanged();
                                }

                            }else {
                                Type type = new TypeToken<ArrayList<ApplyData>>() {
                                }.getType();
                                ArrayList<ApplyData> list=
                                CommonUtil.gson.fromJson(info.getRetDetail(), type);
                                data2.addAll(list);
                                if (adapter2==null){
                                    adapter2 = new MyApplyAdapter(ApplyHistoryActivity.this, data2);
                                    lv.setAdapter(adapter2);
                                    setViewGone(loadLayout);
                                }
                                else {
                                    myRefreshListView.setLoading(false);
                                    adapter2.notifyDataSetChanged();
                                }
                            }
                            pn++;
                        } else {
                            Log.e("ApplyHistoryActivity", info.getRetDetail());
                        }
                    }
                }
        );

    }



    public void onBack(View view) {
        finish();
    }
}
