package com.rentian.newoa.modules.login.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.baoyz.treasure.Treasure;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.adapter.CardAdapter;
import com.rentian.newoa.common.bean.SimplePreferences;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.banner.BannerLayout;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.view.Main2Activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import jameson.io.library.util.ScreenUtil;


public class LoginActivity extends Base2Activity implements OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SimplePreferences preferences = Treasure.get(getApplicationContext()
                , SimplePreferences.class);
        super.onCreate(savedInstanceState);
        if (preferences.getIsLogin()) {
            okGoLogin(preferences.getUsername(), preferences.getPassword());
            return;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        setContentView(R.layout.activity_login);//
        getPicURL();
        setStatusBarDarkMode();
//        init();

    }

    private BannerLayout mRecyclerView;
    private List<String> mList = new ArrayList<>();

    private void init() {

        mRecyclerView = findViewById(R.id.recycler);
        ViewGroup.LayoutParams para1;
        para1 = mRecyclerView.getLayoutParams();
        para1.height = (int) (ScreenUtil.getScreenHeight(this) * 0.4);
        mRecyclerView.setLayoutParams(para1);
        mRecyclerView.setAdapter(new CardAdapter(mList));

        // mRecyclerView绑定scale效果

//        initBlurBackground();
    }

    //登录方法
    private void getPicURL() {

        OkGo.<String>get(Const.RTOA.URL_LIST_URL)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("pic", response.body());
                        mList.clear();
                        Type type = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        ArrayList<String> data = CommonUtil.gson.fromJson(response.body(), type);
                        mList.addAll(data);
                        if (mList.size() > 0) {
                            init();
                        }
                    }
                });

    }

    //登录方法
    private void okGoLogin(String u, final String p) {

        OkGo.<String>post(Const.RTOA.URL_LOGIN)
                .tag(this)
                .params("u", u)
                .params("p", p)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("login", response.body());

                        Type type = new TypeToken<TaskData>() {
                        }.getType();
                        TaskData data = CommonUtil.gson.fromJson(response.body(), type);
                        if (data.code == 0) {
                            MyApp.getInstance().setMyUid(data.data.pushid);
                            MyApp.getInstance().uid = data.data.uid;
                            MyApp.getInstance().myName = data.data.name;
                            MyApp.getInstance().qiye = data.data.cid;
                            MyApp.getInstance().myPost = data.data.post;
                            MyApp.getInstance().myPost = data.data.dept;
                            MyApp.getInstance().loginMsg = response.body();

                            startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                            overridePendingTransition(R.anim.enter, R.anim.exit);
                            finish();
//                            }
                        } else {
                            ToastUtill.showMessage(data.msg);
                            setContentView(R.layout.activity_login);//
                            getPicURL();
                            setStatusBarDarkMode();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {

    }

    public void onLogin(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        i.putExtra("type", 2);
        startActivity(i);
        finish();
    }

    public void onRegister(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        i.putExtra("type", 0);
        startActivity(i);
        finish();
    }
}
