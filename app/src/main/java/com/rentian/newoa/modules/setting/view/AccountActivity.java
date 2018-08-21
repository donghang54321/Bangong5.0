package com.rentian.newoa.modules.setting.view;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends Base2Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupActionBar();
        setStatusBarDarkMode();
        initView();
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("time", "1");
        doHttpAsync(Const.RTOA.URL_USER_INFO, hashMap);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("账号信息");
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

    private TextView tvPhone;
    private TextView tvName,tvEmail;
    private CircleImageView touxiang;

    private void initView() {


        RelativeLayout relativeLayout1 = findViewById(R.id.item1);
        TextView textView1 = (TextView) relativeLayout1.getChildAt(0);
        textView1.setText("头像");
        touxiang = (CircleImageView) relativeLayout1.getChildAt(3);

        RelativeLayout relativeLayout2 = findViewById(R.id.item2);
        TextView textView2 = (TextView) relativeLayout2.getChildAt(0);
        textView2.setText("姓名");
        tvName = (TextView) relativeLayout2.getChildAt(2);

        RelativeLayout rlPassword = findViewById(R.id.item_password);
        TextView tvPassword = (TextView) rlPassword.getChildAt(0);
        tvPassword.setText("修改密码");


        RelativeLayout relativeLayout3 = findViewById(R.id.item3);
        TextView textView3 = (TextView) relativeLayout3.getChildAt(0);
        textView3.setText("手机");
        tvPhone = (TextView) relativeLayout3.getChildAt(2);

        RelativeLayout relativeLayout4 = findViewById(R.id.item4);
        TextView textView4 = (TextView) relativeLayout4.getChildAt(0);
        textView4.setText("邮箱");
        tvEmail = (TextView) relativeLayout4.getChildAt(2);

        RelativeLayout relativeLayout5 = findViewById(R.id.item5);
        TextView textView5 = (TextView) relativeLayout5.getChildAt(0);
        textView5.setText("微信");

        RelativeLayout relativeLayout6 = findViewById(R.id.item6);
        TextView textView6 = (TextView) relativeLayout6.getChildAt(0);
        textView6.setText("钉钉");
    }



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
                            TaskData taskData = response.body();
                            tvName.setText(taskData.data.name);
                            tvPhone.setText(taskData.data.phone);
                            tvEmail.setText(taskData.data.email);
                            Glide.with(AccountActivity.this)
                                    .load(Const.RTOA.URL_BASE + taskData.data.avatar)
                                    .into(touxiang);
//                            tvPhone.setText(taskData.data.phone);
                        }
                    }
                });

    }
}
