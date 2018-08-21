package com.rentian.newoa.modules.note.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.DateUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.view.CircleImageView;
import com.rentian.newoa.common.view.FillListView;
import com.rentian.newoa.modules.note.adapter.ExamineAdapter;
import com.rentian.newoa.modules.note.bean.Quanxian;
import com.rentian.newoa.modules.note.bean.ReportData;
import com.rentian.newoa.modules.note.bean.ResReport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsReportActivity extends AppCompatActivity implements
        TextView.OnEditorActionListener {
    @BindView(R.id.daily_report_details)
    TextView tv1;
    @BindView(R.id.question_report_details)
    TextView tv2;
    @BindView(R.id.reporter_name)
    TextView name2;
    @BindView(R.id.tv_tuijian)
    TextView tvTuijian;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.report_time)
    TextView time;
    @BindView(R.id.report_details_list)
    FillListView lv;
    @BindView(R.id.rl_like)
    RelativeLayout rlLike;
    @BindView(R.id.et_reply)
    EditText etReply;
    @BindView(R.id.iv_like)
    ImageView ivDz;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.report_avatar_iv)
    CircleImageView ivTouxiang;
    private InputMethodManager imm;
    private ArrayList<ResReport> noticeList = new ArrayList<>();
    private ExamineAdapter examineAdapter;
    private String myName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 19) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        setContentView(R.layout.activity_details_report);
        myName=getUsername(this);
        ButterKnife.bind(this);

//        if ("1892".equals(MyApp.getInstance().getMyUid())){
//            tvTuijian.setVisibility(View.VISIBLE);
//        }

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        etReply.setOnEditorActionListener(this);
        doHttpAsync(getIntent().getIntExtra("id", 0) + "");
        examineAdapter = new ExamineAdapter(this, noticeList);
        lv.setAdapter(examineAdapter);
        ivDz.setVisibility(View.VISIBLE);
        llBottom.setVisibility(View.VISIBLE);
//        async();
    }


    private void doHttpAsync(String id) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("id", id);
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_REPROT_DETAIL).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("reportdetail", info.getRetDetail());

                            Type type = new TypeToken<ReportData>() {
                            }.getType();
                            ReportData data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            tv1.setText(data.c1);
                            tv2.setText(data.c2);
                            name2.setText(data.name);
//                            name.setText(data.name.substring(data.name.length() - 1));
                            time.setText(DateUtil.formatTimeInMillis(data.time,"MM月dd日HH:mm"));
                            if (data.list.size() > 0) {
                                rlLike.setVisibility(View.VISIBLE);
                                String zan = "";
                                for (int i = 0; i < data.list.size(); i++) {

                                    if (i == data.list.size() - 1) {
                                        zan += (data.list.get(i) + "觉得很赞");
                                    } else {
                                        zan += (data.list.get(i) + ",");
                                    }
                                }
                                tvLike.setText(zan);
                            }

                            noticeList.addAll(data.list1);
                            if (noticeList.size() > 0) {
                                examineAdapter.notifyDataSetChanged();
                            }

                        } else {
                            Log.e("reportdetail", info.getRetDetail());
                        }
                    }
                }
        );

    }

    public void onDz(final View view) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1,2.5f,1,2.5f,
                Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        //3秒完成动画
        scaleAnimation.setDuration(500);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ivDz.setImageResource(R.drawable.like_blue);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中

        AlphaAnimation alphaAnimation=new AlphaAnimation(0f,1f);
        alphaAnimation.setDuration(500);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        view.startAnimation(animationSet);
        doDz(getIntent().getIntExtra("id", 0) + "");
    }

    private void doDz(String id) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("gid", id);
        hashMap.put("uid", MyApp.getInstance().getMyUid());
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_REPROT_DZ).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("reportdedz", info.getRetDetail());

                            Type type = new TypeToken<ReportData>() {
                            }.getType();
                            ReportData data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            if (data.msg.equals("1")) {
                                ToastUtill.showMessage("点赞成功");
                                if (tvLike.getText().toString().trim().length()==0){
                                    rlLike.setVisibility(View.VISIBLE);
                                    tvLike.setText(myName+"觉得很赞");
                                }else {
                                    tvLike.setText(myName + "," + tvLike.getText());
                                }
                            } else {
                                ToastUtill.showMessage(data.msg);
                            }
                        } else {
                            Log.e("reportdz", info.getRetDetail());
                        }
                    }
                }
        );

    }

    private void doPl(String id, final String notice) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("gid", id);
        hashMap.put("uid", MyApp.getInstance().getMyUid());
        hashMap.put("content", notice);
        OkHttpUtil.getDefault().doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_REPROT_PL).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("reportdedz", info.getRetDetail());

                            Type type = new TypeToken<ReportData>() {
                            }.getType();
                            ReportData data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            if (data.msg.equals("1")) {
                                etReply.setText("");
                                ToastUtill.showMessage("评论成功");
                                ResReport resReport=new ResReport();
                                resReport.name=myName;
                                resReport.time= Calendar.getInstance().getTimeInMillis();
                                resReport.content=notice;
                                resReport.uid=MyApp.getInstance().getMyUid();
                                Collections.reverse(noticeList);
                                noticeList.add(resReport);
                                Collections.reverse(noticeList);
                                examineAdapter.notifyDataSetChanged();

                            } else {
                                ToastUtill.showMessage(data.msg);
                            }
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                                        0);
                            }
                        } else {
                            Log.e("reportdz", info.getRetDetail());
                        }
                    }
                }
        );

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            doPl(getIntent().getIntExtra("id", 0) + "", etReply.getText().toString());

        } else if (isEnterClick(event)) {
            doPl(getIntent().getIntExtra("id", 0) + "", etReply.getText().toString());

        }
        return isEnterClick(event);
    }

    private boolean isEnterClick(KeyEvent event) {
        return event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }
    private String getUsername(Context context) {

        SharedPreferences sp = context.getSharedPreferences("user_info",
                Activity.MODE_PRIVATE);

        return sp.getString("username", "");
    }
    Quanxian quanxian;
//    private void async() {
//        OkHttpUtil.getDefault(this).doGetAsync(
//                HttpInfo.Builder().setUrl(Const.RTOA.URL_REPORT_QX).build(),
//                new Callback() {
//                    @Override
//                    public void onFailure(HttpInfo info) throws IOException {
//                        String result = info.getRetDetail();
//
//                        Log.e("异步请求失败：" , result);
//                    }
//
//                    @Override
//                    public void onSuccess(HttpInfo info) throws IOException {
//                        String result = info.getRetDetail();
//                        Type type = new TypeToken<Quanxian>() {
//                        }.getType();
//                        quanxian = CommonUtil.gson.fromJson(info.getRetDetail(), type);
//                        for (int i=0;i<quanxian.dz.size();i++){
//                            if (MyApp.deptid.equals(quanxian.dz.get(i)+""))
//                                ivDz.setVisibility(View.VISIBLE);
//
//                        }
//                        for (int i=0;i<quanxian.notice.size();i++){
//                            if (MyApp.uid.equals(quanxian.notice.get(i)+""))
//                                llBottom.setVisibility(View.VISIBLE);
//                        }
//
//                        Log.e("异步请求成功：" , result);
//                        //GSon解析
//                    }
//                });
//    }

    public void onBack(View view) {
        finish();
    }

//    private void doTuijian() {
//        Map<String, String> hashMap = new HashMap<>();
//        hashMap.put("uid", MyApp.uid);
//        hashMap.put("rid", getIntent().getIntExtra("rid", 0) + "");
//        OkHttpUtil.getDefault().doPostAsync(
//                HttpInfo.Builder().setUrl(Const.URL_REPORTLIST_TJ).addParams(hashMap).build(),
//                new CallbackOk() {
//                    @Override
//                    public void onResponse(HttpInfo info) throws IOException {
//                        if (info.isSuccessful()) {
//                            Log.e("tuijian", info.getRetDetail());
//
//                            Type type = new TypeToken<ReportData>() {
//                            }.getType();
//                            ReportData data = CommonUtil.gson.fromJson(info.getRetDetail(), type);
//                            if (data.msg.equals("1")){
//                                ToastUtill.showMessage("推荐日志成功");
//                            }
//                        } else {
//                            Log.e("tuijian", info.getRetDetail());
//                        }
//                    }
//                }
//        );
//
//    }

    private AlertDialog.Builder dialog;

//    private void creatDialog() {
//        dialog = new AlertDialog.Builder(this);  //先得到构造器
//        dialog.setTitle("提示"); //设置标题
//        dialog.setMessage("确定要推荐这篇日志吗？"); //设置内容
//        dialog.setPositiveButton("推荐", new DialogInterface.OnClickListener() { //设置确定按钮
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss(); //关闭dialog
//                doTuijian();
//            }
//        });
//        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.setCancelable(false);
//        //参数都设置完成了，创建并显示出来
//        dialog.create().show();
//
//    }

    public void onTuijian(View view) {
//        creatDialog();
    }
}
