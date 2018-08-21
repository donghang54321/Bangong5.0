package com.rentian.newoa.modules.login.view;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.rentian.newoa.modules.communication.bean.EmployeeInfoByNet;
import com.rentian.newoa.modules.meeting.bean.Msg;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    private int time = 0;
    private TextView tvTitle;
    private Button button;
    private EditText etPhone, etCode, etPassword, etName,etPoiston;
    private TextInputLayout tllPhone, tllCode, tllPassword, tllName,tllPoiston;

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
        setContentView(R.layout.activity_forget_password);
        etPhone = findViewById(R.id.et_pnumber);
        etPoiston= findViewById(R.id.et_poistio);
        etCode = findViewById(R.id.et_code);
        etPassword = findViewById(R.id.et_newpassword);
        etName = findViewById(R.id.et_name);
        tllPhone = findViewById(R.id.tll_pnumber);
        tllCode = findViewById(R.id.tll_code);
        tllPassword = findViewById(R.id.tll_newpassword);
        tllName = findViewById(R.id.til_name);
        tllPoiston= findViewById(R.id.tll_poistion);
        tvTitle = findViewById(R.id.title);
        if (getIntent().getIntExtra("personDetail", 0) == 1) {
            tvTitle.setText("修改信息");
            tllPassword.setHint("企业名称");
            etPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            tllPhone.setHint("固定电话");
            etPhone.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            tllCode.setHint("电子邮箱");
            etCode.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            tllName.setHint("手机号码");
            etName.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("uid", MyApp.getInstance().getMyUid());
            getUInfo(Const.RTOA.URL_USER_INFO, hashMap);

        }else {
            tllPoiston.setVisibility(View.GONE);
        }
    }

    private void getUInfo(String url, final Map<String, String> hashMap) {

        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder().setUrl(url).addParams(hashMap)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        Log.e("info", info.getRetDetail());
                        if (info.isSuccessful()) {

                            Type type = new TypeToken<EmployeeInfoByNet>() {
                            }.getType();

                            EmployeeInfoByNet detail = CommonUtil.gson
                                    .fromJson(info.getRetDetail(), type);
                            etName.setText(detail.cellphone);
                            etCode.setText(detail.email);
                            etPhone.setText(detail.tel);
                            etPoiston.setText(detail.zw);
                            etPassword.setText(detail.qyname);
                        }
                    }
                }

        );

    }

    private void doHttpAsync(String url, final Map<String, String> hashMap) {

        OkHttpUtil.getDefault(this).doPostAsync(
                HttpInfo.Builder().setUrl(url).addParams(hashMap)
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        Log.e("密码", info.getRetDetail());
                        if (info.isSuccessful()) {

                            Type type = new TypeToken<Msg>() {
                            }.getType();

                            Msg msg = CommonUtil.gson.fromJson(info.getRetDetail(), type);

                            if (msg.msg.equals("1")) {
                                ToastUtill.showMessage("修改成功");
                                finish();
                            } else {
                                ToastUtill.showMessage(msg.msg);
                            }

                        }
                    }
                }

        );

    }

    public void onSubmit(View view) {
        if (getIntent().getIntExtra("personDetail", 0) == 1) {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("phone", etName.getText().toString().trim());
            hashMap.put("tel", etPhone.getText().toString().trim());
            hashMap.put("email", etCode.getText().toString().trim());
            hashMap.put("qyname", etPassword.getText().toString().trim());
            hashMap.put("zw", etPoiston.getText().toString().trim());
            hashMap.put("uid", MyApp.getInstance().getMyUid());
            doHttpAsync(Const.RTOA.URL_UPDATE_INFO, hashMap);

        } else {
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("sp", etName.getText().toString().trim());
            hashMap.put("p0", etPhone.getText().toString().trim());
            hashMap.put("p1", etCode.getText().toString().trim());
            hashMap.put("p2", etPassword.getText().toString().trim());
            doHttpAsync(Const.RTOA.URL_FORGET_PASSWORD, hashMap);
        }
    }

    public void onBack(View view) {
        finish();
    }
}
