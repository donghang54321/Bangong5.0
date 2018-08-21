package com.rentian.newoa.modules.note.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.NotifyMessageManager;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.meeting.bean.Msg;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SendReportActivity extends AppCompatActivity
         {
    private EditText et_work_of_today, et_problem_feedback, et_tomorrow_plan;
    private TextView tv_work_of_today, tv_problem_feedback, tv_tomorrow_plan;
    private ImageView work_of_today_line, problem_feedback_line, tomorrow_plan_line;
    public TextView tv_calender;
    private View calender_back;
    private MyApp myApp;
    private Date calendar = new Date();
    private String dailyDate;
    private boolean isDayPickerShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_report_send);
        myApp = MyApp.getInstance();
        init();
        setEtListener(et_problem_feedback, tv_problem_feedback, problem_feedback_line);
        setEtListener(et_work_of_today, tv_work_of_today, work_of_today_line);
    }


    private void init() {
        et_problem_feedback = (EditText) findViewById(R.id.et_problem_feedback);
        et_work_of_today = (EditText) findViewById(R.id.et_work_of_today);
        tv_problem_feedback = (TextView) findViewById(R.id.problem_feedback_num);
        tv_work_of_today = (TextView) findViewById(R.id.work_of_today_num);
        work_of_today_line = (ImageView) findViewById(R.id.work_of_today_line);
        problem_feedback_line = (ImageView) findViewById(R.id.problem_feedback_line);
    }


    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest.replaceAll("\u0001\n", "");
    }

    private void setEtListener(EditText et, final TextView tv, final ImageView line) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = replaceBlank(s.toString());

                int num = str.length();
                tv.setText(num + "");
                if (num >= 50) {
                    tv.setTextColor(Color.BLACK);
                    tv.setTextSize(18.0f);
                } else if (num < 50) {
                    tv.setTextColor(Color.RED);
                    tv.setTextSize(14.2f);
                }
                float temp = num / 50f;
                if (temp > 1) {
                    temp = 1f;
                }
                line.setScaleX(1f - temp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void showSendExecuting() {
        tv_calender.setClickable(false);
        et_work_of_today.setEnabled(false);
        et_problem_feedback.setEnabled(false);
        et_tomorrow_plan.setEnabled(false);
    }

    private void showSendPost() {
        tv_calender.setClickable(true);
        et_work_of_today.setEnabled(true);
        et_problem_feedback.setEnabled(true);
        et_tomorrow_plan.setEnabled(true);
    }

    private void showSendResult(int flag, int score) {
        switch (flag) {
            case 0:
                break;
            case 1:
                CommonUtil.showToast(SendReportActivity.this, "发送失败");
                break;
            case 2:
                CommonUtil.showToast(SendReportActivity.this, "正在发送中，请稍等片刻");
                break;
            case 3:
                int workOfTodayNum = et_work_of_today.getText().toString().trim().length();
                int problemFeedBackNum = et_problem_feedback.getText().toString().trim().length();
                int TomorrowPlanNum = et_tomorrow_plan.getText().toString().trim().length();
                StringBuffer sb = new StringBuffer();
                int countLine = 0;
                boolean w1 = false, w2 = false, w3 = false;
                if (workOfTodayNum < 50) {
                    countLine++;
                    w1 = true;
                }
                if (problemFeedBackNum < 50) {
                    countLine++;
                    w2 = true;
                }
                if (TomorrowPlanNum < 50) {
                    countLine++;
                    w3 = true;
                }
                for (int i = 0; i < countLine; i++) {
                    if (i != 0) {
                        sb.append("\n");
                    }
                    if (w1) {
                        sb.append("今日工作字数不足50，还差" + (50 - workOfTodayNum) + "个字");
                        w1 = false;
                        continue;
                    }
                    if (w2) {
                        sb.append("问题反馈字数不足50，还差" + (50 - problemFeedBackNum) + "个字");
                        w2 = false;
                        continue;
                    }
                    if (w3) {
                        sb.append("明日计划字数不足50，还差" + (50 - TomorrowPlanNum) + "个字");
                        w3 = false;
                        continue;
                    }
                }
                CommonUtil.showToast(SendReportActivity.this, sb.toString());
                break;
            case 4:
                if (score > 0)
                    CommonUtil.showToast(SendReportActivity.this, "发送成功,获得" + score + "积分");
                else {
                    CommonUtil.showToast(SendReportActivity.this, "发送成功");
                }

                finish();
                break;
            default:
                break;
        }

    }

    public void onBack(View view) {
        finish();
    }

    public void onSend(View view) {
        doHttpAsync(et_work_of_today.getText().toString(),
                et_problem_feedback.getText().toString());
//        mPresenter.sendDaily(getDailyInfo());
    }

    private void doHttpAsync(String today_word, String tomorrow_plan) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("c1", today_word);
        hashMap.put("c2", tomorrow_plan);
        hashMap.put("uid",myApp.getMyUid());
        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_REPROT_SEND).addParams(hashMap).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("json", info.getRetDetail());

                            Type type = new TypeToken<Msg>() {
                            }.getType();
                            Msg msg = CommonUtil.gson.fromJson(info.getRetDetail(), type);
                            if (msg.msg.equals("1")){
                                CommonUtil.showToast(SendReportActivity.this, "发送成功");
                                finish();
                                NotifyMessageManager.getInstance().sendNotifyMessage("1");

                            }else {
                                CommonUtil.showToast(SendReportActivity.this, msg.msg);
                            }

                        } else {
                            Log.e("json", info.getRetDetail());
                        }
                    }
                }
        );

    }



}
