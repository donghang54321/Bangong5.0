package com.rentian.newoa.modules.apply.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.apply.adapter.ApplyListAdapter;
import com.rentian.newoa.modules.apply.bean.ApplyList;
import com.rentian.newoa.modules.examiation.view.ExaminationListActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 申请列表页
 */
public class ApplyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lv;
    private ArrayList<ApplyList> data = new ArrayList<>();
    private ApplyListAdapter adapter;
    private WhorlView loadView;
    private LinearLayout loadLayout;
    private View viewRight;
    private AlertDialog dialogRight;
    private PopupWindow mPopupwinow;
    private TextView tvRight;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_apply_list);
        lv = (ListView) findViewById(R.id.lv_apply_list);
        tvRight = (TextView) findViewById(R.id.tv_examina);
        viewRight = LayoutInflater.from(this).inflate(R.layout.apply_menu_two, null);
        dialogRight = new AlertDialog.Builder(this).setView(viewRight).create();
        dialogRight.getWindow().setGravity(Gravity.TOP);
        dialogRight.getWindow().setGravity(Gravity.RIGHT);
        lv.setOnItemClickListener(this);
        loadView = (WhorlView) findViewById(R.id.loading_view);
        loadLayout = (LinearLayout) findViewById(R.id.loading_layout);
        loadView.start();
        postAsync();

    }


//    private void update() {
//        UpdateTextTask updateTextTask = new UpdateTextTask(this);
//        updateTextTask.execute();
//    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getItemId(position) != 0) {
            Intent intent = new Intent(ApplyListActivity.this, ApplyActivity.class);
            intent.putExtra("id", adapter.getItemId(position));
            startActivity(intent);
        }
    }


    private void postAsync() {
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_APPLY_LIST_TEST)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("ApplyListActivity", info.getRetDetail());
                            Type type = new TypeToken<List<ApplyList>>() {
                            }.getType();


                            data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            adapter = new ApplyListAdapter(ApplyListActivity.this, data);
                            lv.setAdapter(adapter);
                            setViewGone(loadLayout);
                        } else {
                            Log.e("ApplyListActivity", info.getRetDetail());
                        }
                    }
                }
        );

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

    public void onExamina(View view) {
        if (mPopupwinow == null) {
            //新建一个popwindow
            mPopupwinow = new PopupWindow(viewRight,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);
            //设置popwindow的背景颜色
            mPopupwinow.setBackgroundDrawable(new ColorDrawable(
                    0x00000000));
        }
        //设置popwindow的位置  tv:为微信右上角+号view，居于+号下方
        mPopupwinow.showAsDropDown(tvRight, 0, 0);
    }

    public void menuClick(View v) {
        Intent intent = new Intent(this, ExaminationListActivity.class);
        intent.putExtra("type", 2);
        startActivity(intent);
        mPopupwinow.dismiss();
    }

    public void menuClick2(View v) {
        Intent intent = new Intent(this, ExaminationListActivity.class);
        intent.putExtra("type", 1);
        startActivity(intent);
        mPopupwinow.dismiss();
    }
}
