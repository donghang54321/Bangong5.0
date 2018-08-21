package com.rentian.newoa.modules.examiation.view;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.apply.adapter.MyApplyAdapter;
import com.rentian.newoa.modules.apply.view.ApplyHistoryActivity;
import com.rentian.newoa.modules.examiation.adapter.ExamiationAdapter;
import com.rentian.newoa.modules.examiation.bean.ApplyData;
import com.rentian.newoa.modules.examiation.bean.Examiation;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by rentianjituan on 2016/4/19.
 */
public class ExaminationListActivity extends AppCompatActivity {
    private ListView lv;
    private WhorlView loadView;
    private LinearLayout loadLayout;
    private ArrayList<Examiation> data;
    private ArrayList<ApplyData> data2;
    private ExamiationAdapter adapter;
    private MyApplyAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_examination_list);
        lv = (ListView) findViewById(R.id.lv_examination);
        loadView = (WhorlView) findViewById(R.id.loading_view);
        loadLayout = (LinearLayout) findViewById(R.id.loading_layout);
        loadView.start();
        initListener();
        TextView title= (TextView) findViewById(R.id.title);
        if (getIntent().getIntExtra("type", 1)==2)
        title.setText("我的申请");
    }

    private void initListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExaminationListActivity.this, ExaminationAcitity.class);
                if (getIntent().getIntExtra("type", 1) == 1) {
                    intent.putExtra("taskId", data.get(position).taskid);
                    intent.putExtra("type",0);
                } else {
                    intent.putExtra("taskId", data2.get(position).id+"");
                    intent.putExtra("type",1);
                }
                startActivity(intent);
            }
        });
    }

//    private void update() {
//        UpdateTextTask updateTextTask = new UpdateTextTask(this);
//        updateTextTask.execute(getIntent().getIntExtra("type", 1));
//    }

    private void postAsync(final int t) {
        String url=null;
        if (t==1){
            url=Const.RTOA.URL_EXAMINATION_LIST;
        }else {
            url = Const.RTOA.URL_MYAPPLY_LIST;
        }
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(url)
                        .addParam("uid",MyApp.getInstance().getMyUid())
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("ApplyListActivity", info.getRetDetail());
                            if (t==1) {
                                Type type = new TypeToken<ArrayList<Examiation>>() {
                                }.getType();

                                data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                                adapter = new ExamiationAdapter(ExaminationListActivity.this, data);
                                lv.setAdapter(adapter);
                                setViewGone(loadLayout);
                            }else {
                                Type type = new TypeToken<ArrayList<ApplyData>>() {
                                }.getType();

                                data2 = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                                adapter2 = new MyApplyAdapter(ExaminationListActivity.this, data2);
                                lv.setAdapter(adapter2);
                                setViewGone(loadLayout);
                            }
                        } else {
                            Log.e("ApplyListActivity", info.getRetDetail());
                        }
                    }
                }
        );

    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            //如果仅仅是用来判断网络连接
            //则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    public void onBack(View view) {
        finish();
    }
    public void onHistory(View view) {
        Intent intent = new Intent(ExaminationListActivity.this,
                ApplyHistoryActivity.class);
        intent.putExtra("type",getIntent().getIntExtra("type", 1));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(this))
            postAsync(getIntent().getIntExtra("type", 1));
        else ToastUtill.showMessage("没有网络");
    }
}
