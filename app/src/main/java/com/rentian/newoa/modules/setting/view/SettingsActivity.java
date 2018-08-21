package com.rentian.newoa.modules.setting.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.treasure.Treasure;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.bean.SimplePreferences;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.modules.setting.adapter.GridAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.view.Main2Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends Base2Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setupActionBar();
        setStatusBarDarkMode();
        initView();
        initData();
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", "" + MyApp.getInstance().qiye);
        doHttpAsync(Const.RTOA.URL_PERSON_SETTING, hashMap);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("设置");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private RecyclerView mRecyclerView;
    private TextView tvPhone;
    private GridAdapter mGridAdapter;
    private TextView tvName, tvTuichu;
    private CircleImageView touxiang;

    private void initView() {

        mRecyclerView = findViewById(R.id.rv_gird);
        tvName = findViewById(R.id.tv_name);
        tvTuichu = findViewById(R.id.tv_tuichu);
        tvTuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimplePreferences preferences = Treasure.get(SettingsActivity.this
                        , SimplePreferences.class);
                preferences.setIsLogin(false);
                Intent intent = new Intent();
                // 获取用户计算后的结果
                //通过intent对象返回结果，必须要调用一个setResult方法，
                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                setResult(4, intent);
                finish(); //结束当前的activity的生命周期
            }
        });
        touxiang = findViewById(R.id.iv_touxiang);
//        mRecyclerView.addItemDecoration(new RecycleViewDivider(
//                getContext(), LinearLayoutManager.VERTICAL, 1, R.color.task_text2));


        RelativeLayout relativeLayout1 = findViewById(R.id.item1);
        TextView textView1 = (TextView) relativeLayout1.getChildAt(0);
        textView1.setText("账号信息");
        relativeLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,AccountActivity.class));
            }
        });

        RelativeLayout relativeLayout2 = findViewById(R.id.item2);
        TextView textView2 = (TextView) relativeLayout2.getChildAt(0);
        textView2.setText("设置");

        RelativeLayout relativeLayout3 = findViewById(R.id.item3);
        TextView textView3 = (TextView) relativeLayout3.getChildAt(0);
        textView3.setText("联系客服");
        tvPhone = (TextView) relativeLayout3.getChildAt(2);

        RelativeLayout relativeLayout4 = findViewById(R.id.item4);
        TextView textView4 = (TextView) relativeLayout4.getChildAt(0);
        textView4.setText("在线反馈");

        RelativeLayout relativeLayout5 = findViewById(R.id.item5);
        TextView textView5 = (TextView) relativeLayout5.getChildAt(0);
        textView5.setText("推荐给朋友");

        RelativeLayout relativeLayout6 = findViewById(R.id.item6);
        TextView textView6 = (TextView) relativeLayout6.getChildAt(0);
        textView6.setText("检查更新");
    }

    public void initData() {
        tvName.setText(MyApp.getInstance().myName);
        Glide.with(this)
                .load(Const.RTOA.URL_BASE + loginData.data.avatar)
                .into(touxiang);
    }

    private ArrayList<DataList> list = new ArrayList<>();

    public void doHttpAsync(String url, Map<String, String> hashMap) {
//        animation_view_click.setVisibility(View.VISIBLE);
//        startAnima();
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            list.clear();
                            TaskData taskData = response.body();
                            tvPhone.setText(taskData.data.phone);
                            list.addAll(taskData.data.icon);
                            if (list.size() > 0) {
                                if (mGridAdapter == null) {
                                    mRecyclerView.setLayoutManager(new GridLayoutManager(SettingsActivity.this, list.size()));
                                    mGridAdapter = new GridAdapter(SettingsActivity.this, list, 1);
                                    mRecyclerView.setAdapter(mGridAdapter);
                                } else {
                                    mGridAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });

    }
}
