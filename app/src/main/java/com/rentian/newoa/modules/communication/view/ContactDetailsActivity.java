package com.rentian.newoa.modules.communication.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.view.RippleBackground;
import com.rentian.newoa.modules.communication.bean.EmployeeInfoByNet;
import com.rentian.newoa.modules.login.view.ForgetPasswordActivity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class ContactDetailsActivity extends AppCompatActivity {

    private RippleBackground imageTitle;
    private TextView tv_name, tv_dept, tv_tel, tv_lastName;
    private TextView phonenum, officenum, eMail, department, address;

    private String phone, tel, email, dept, addr, name, img, postion;
    private int uid;
    private ImageView ivTouxiang;
    private Button btUpdate, btCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_contact_details);
        getBundleInfo();
        init();
        setView();

    }

    private void getBundleInfo() {
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");
        tel = bundle.getString("tel");
        email = bundle.getString("email");
        dept = bundle.getString("dept");
        addr = bundle.getString("addr");
        name = bundle.getString("name");
        img = bundle.getString("img");
        uid = bundle.getInt("uid");
        postion = bundle.getString("position");
    }

    private void init() {
        final RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.contact);
        rippleBackground.startRippleAnimation();
        ivTouxiang = findViewById(R.id.contact_iv);
        btCancel = findViewById(R.id.bt_cancel);
        phonenum = (TextView) findViewById(R.id.contact_details_phonenum);
        officenum = (TextView) findViewById(R.id.contact_details_officenum);
        eMail = (TextView) findViewById(R.id.contact_details_email);
        department = (TextView) findViewById(R.id.contact_details_department);
        address = (TextView) findViewById(R.id.contact_details_address);
        imageTitle = (RippleBackground) findViewById(R.id.contact);
        tv_name = (TextView) findViewById(R.id.contact_name);
        tv_dept = (TextView) findViewById(R.id.contact_bumen);
        tv_lastName = (TextView) findViewById(R.id.tv_report_last_text);
        btUpdate = findViewById(R.id.bt_update);
        if (getIntent().getBooleanExtra("isMe", false)) {
            btUpdate.setVisibility(View.VISIBLE);
            btCancel.setVisibility(View.VISIBLE);
        }
        if (!imageTitle.isRippleAnimationRunning())
            imageTitle.startRippleAnimation();
    }

    private void setView() {
        department.setText(dept);
        tv_name.setText(name);
//        tv_dept.setText(dept + "-" + postion);
        phonenum.setText(phone);
        eMail.setText(email);
        address.setText(addr);
        officenum.setText(tel);
//        if (img.length() > 0) {
//            Glide.with(this).load(Const.RTOA.URL_BASE + img).into(ivTouxiang);
//        } else {
//            tv_lastName.setText(name.substring(name.length() - 1));
//            int lottery = uid % 7;
//            ivTouxiang.setImageResource(tx_color[lottery]);
//
//        }
    }

    public void onBack(View view) {
        finish();
    }

    public void onCancel(View view) {
        Intent i = new Intent();
        setResult(4, i);
        finish();
    }

    public void callPhone(View v) {
        if (phone != null) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    }

    public void callTel(View v) {
        if (tel != null) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent);
        }
    }

    public void sendSms(View v) {
        if (phone != null) {
            Uri smsToUri = Uri.parse("smsto:" + phone);
            Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
            mIntent.putExtra("sms_body", "");
            startActivity(mIntent);
        }
    }

    public void chat(View v) {
        Intent intent = new Intent(
                this, ForgetPasswordActivity.class);
        intent.putExtra("personDetail", 1);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageTitle.stopRippleAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!imageTitle.isRippleAnimationRunning())
            imageTitle.startRippleAnimation();
//        if (getIntent().getBooleanExtra("isMe",false)
//                ||MyApp.getInstance().getMyUid().equals(""+uid)) {
        String id = "" + uid;
        if (id.equals("0")){
            id=MyApp.getInstance().getMyUid();
        }
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("uid", id);
        doHttpAsync(Const.RTOA.URL_USER_INFO, hashMap);
//        }
    }

    private void doHttpAsync(String url, final Map<String, String> hashMap) {

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
                            tv_name.setText(detail.username);
                            phonenum.setText(detail.cellphone);
                            eMail.setText(detail.email);
                            officenum.setText(detail.tel);
                            tv_dept.setText(detail.lmzw);
                            address.setText(detail.zw);
                            department.setText(detail.qyname);
                        }
                    }
                }

        );

    }
}
