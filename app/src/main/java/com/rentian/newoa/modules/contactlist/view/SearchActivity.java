package com.rentian.newoa.modules.contactlist.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.modules.contactlist.adapter.SearchAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends Base2Activity
        implements SetActionBarListener {
    private AppBarLayout appBarLayout;
    private TextView tvQueren;
    private EditText editText;
    private RelativeLayout rlSearch;
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private ArrayList<DataList> userlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(" ");
        initView();
        setStatusBarDarkMode();
        editTextSearchListener();
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                Log.e("Offset",verticalOffset+"");
//                Log.e("Offset2",(1f + ((verticalOffset + 108f) / 222f))+"");
                //// TODO: 2018/6/29
                //84 搜索框高度  verticalOffset/330百分比
                rlSearch.setTranslationY(verticalOffset - (74 * verticalOffset / 330));
                float scaleX = 1f + ((verticalOffset + 108f) / 222f);
                if (verticalOffset != 0) {
                    if (-verticalOffset > 108) {
                        if (scaleX * 0.8f > 0.7f) {
                            rlSearch.setScaleX(scaleX * 0.8f);
                            rlSearch.setScaleY(0.8f);

                        } else {
                            rlSearch.setScaleX(0.7f);
                            rlSearch.setBackgroundColor(Color.parseColor("#00ffffff"));
                            rlSearch.setScaleY(0.8f);
                        }
                    } else {
                        rlSearch.setBackgroundColor(Color.parseColor("#ffffffff"));
                        rlSearch.setScaleX(1.0f);
                        rlSearch.setScaleY(1.0f);
                    }
                }
            }
        });
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", "");
        doHttpAsync(Const.RTOA.URL_LIST_SEARCH, hashMap);
    }

    private void initView() {
        tvQueren=findViewById(R.id.tv_queren);
        mRecyclerView = findViewById(R.id.id_recyclerview);
        rlSearch = findViewById(R.id.rl_search);
        appBarLayout = findViewById(R.id.app_bar);
        editText = findViewById(R.id.et);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new SearchAdapter(this, userlist,this));
        tvQueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                        userlist.clear();
                        TaskData appData=response.body();
                        userlist.addAll(appData.data.user);
                        mAdapter.notifyDataSetChanged();
                    }
                });

    }
    private ArrayList<DataList> userlist2 = new ArrayList<>();
    private void search() {
        String data = editText.getText().toString().trim();
        userlist2.clear();

        for (int i = 0; i < userlist.size(); i++) {
            DataList dataList = userlist.get(i);

            String ageStr = dataList.tag;
            if( ageStr.contains(data)){
                userlist2.add(dataList);
            }
        }
        mAdapter.setDataLists(userlist2);
    }
    private void editTextSearchListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0){
                    mAdapter.setDataLists(userlist);
                }else {
                    search();
                }

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        MyApp.getInstance().idlist.clear();
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            MyApp.getInstance().idlist.clear();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }


    @Override
    public void setActionBar(int i,String url) {
        if (i==0){
            tvQueren.setVisibility(View.GONE);
        }else {
            Intent intent = new Intent();
            // 获取用户计算后的结果
            intent.putExtra("url", url); //将计算的值回传回去
            //通过intent对象返回结果，必须要调用一个setResult方法，
            //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
            setResult(2, intent);
            finish(); //结束当前的activity的生命周期
            tvQueren.setVisibility(View.VISIBLE);
            tvQueren.setText("确认" + "(" + i + ")");
        }
    }

}
