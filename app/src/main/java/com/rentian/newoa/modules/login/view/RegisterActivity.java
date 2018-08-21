package com.rentian.newoa.modules.login.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.baoyz.treasure.Treasure;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.bean.SimplePreferences;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.Base64BitmapUtil;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.DisplayUtil;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.view.Main2Activity;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends Base2Activity implements View.OnClickListener {
    /**
     * type
     * 0注册第一步 1(手机)注册第二步 2登录 3忘记密码
     * 4(邮箱)注册第二步  5(手机)忘记密码第二步
     * 6创建新团队
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setStatusBarDarkMode();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        type = getIntent().getIntExtra("type", 0);
        initView();

    }

    private TextView tvContent, tvTitle1, tvTitle2, tvpassword, tvTitle3, tvTitle4;
    private ImageView ivTitle, ivEt;
    private EditText et1, et2, et3, et4;
    private RelativeLayout rl, rl2, rl3, rl4;
    private SuperTextView bt;

    private void initView() {
        ivTitle = findViewById(R.id.ivTitle);
        tvContent = findViewById(R.id.tv_content);
        tvTitle1 = findViewById(R.id.tv_title1);
        tvTitle2 = findViewById(R.id.tv_title2);
        tvTitle3 = findViewById(R.id.tv_title3);
        tvTitle4 = findViewById(R.id.tv_title4);
        ivEt = findViewById(R.id.iv_et);
        tvpassword = findViewById(R.id.tv_forgetpassword);
        et1 = findViewById(R.id.et);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        rl = findViewById(R.id.rl);
        rl2 = findViewById(R.id.rl2);
        rl3 = findViewById(R.id.rl3);
        rl4 = findViewById(R.id.rl4);
        bt = findViewById(R.id.bt);
        ivEt.setOnClickListener(this);
        bt.setOnClickListener(this);
        tvpassword.setOnClickListener(this);
        if (type == 0) {
            rl2.setVisibility(View.GONE);
            rl3.setVisibility(View.GONE);
            rl4.setVisibility(View.GONE);
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
            tvTitle2.setVisibility(View.GONE);
            ivTitle.setImageResource(R.drawable.register);
            ivEt.setImageResource(R.drawable.chahao);
            ivEt.setTag("del");
            tvContent.setText("注册表示您已阅读并同意 服务条款");
            tvTitle1.setText("输入邮箱/手机号");
            bt.setCenterString("下一步");
        } else if (type == 1) {
            ivTitle.setImageResource(R.drawable.register);
            ivEt.setImageResource(R.drawable.yanbi);
            ivEt.setTag("yanbi");
            tvContent.setText("请为账号" + getIntent().getStringExtra("zhanghao")
                    + "设置密码");
            et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            tvTitle1.setText("输入密码");
            tvTitle2.setText("输入密码");
            tvTitle3.setText("输入验证码");
            tvTitle4.setText("输入姓名");
            bt.setCenterString("完成注册");
        } else if (type == 2) {
            tvpassword.setVisibility(View.VISIBLE);
            rl3.setVisibility(View.GONE);
            rl4.setVisibility(View.GONE);
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
            ivEt.setVisibility(View.GONE);
            ivTitle.setImageResource(R.drawable.tv_login);
            ivEt.setImageResource(R.drawable.yanbi);
            ivEt.setTag("yanbi");
            tvContent.setText("");
            tvTitle1.setText("输入账号");
            tvTitle2.setText("输入密码");
            et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            bt.setCenterString("登录")
                    .setShapeSelectorNormalColor(R.color.bt_0);
            et2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (et1.getText().toString().trim().length()
                            > 0 && s.toString().trim().length() > 0) {
                        bt.setShapeSelectorNormalColor(R.color.bt_1);
                    } else {
                        bt.setShapeSelectorNormalColor(R.color.bt_0);
                    }
                }
            });

        } else if (type == 3) {
            rl2.setVisibility(View.GONE);
            tvTitle2.setVisibility(View.GONE);
            rl3.setVisibility(View.GONE);
            rl4.setVisibility(View.GONE);
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
            ivTitle.setImageResource(R.drawable.tv_forgetpassword);
            ivEt.setImageResource(R.drawable.chahao);
            ivEt.setTag("del");
            tvTitle1.setText("输入邮箱/手机号");
            bt.setCenterString("下一步");
        } else if (type == 4) {
            tvTitle3.setVisibility(View.GONE);
            rl3.setVisibility(View.GONE);
            ivTitle.setImageResource(R.drawable.register);
            ivEt.setImageResource(R.drawable.yanbi);
            ivEt.setTag("yanbi");
            et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            tvContent.setText("请为账号" + getIntent().getStringExtra("zhanghao")
                    + "设置密码");
            tvTitle1.setText("输入密码");
            tvTitle2.setText("输入密码");
            tvTitle3.setText("输入验证码");
            tvTitle4.setText("输入姓名");
            bt.setCenterString("完成注册");
        } else if (type == 5) {
            tvTitle4.setVisibility(View.GONE);
            rl4.setVisibility(View.GONE);
            et4.setText("1");
            ivTitle.setImageResource(R.drawable.setnewp);
            ivEt.setImageResource(R.drawable.yanbi);
            ivEt.setTag("yanbi");
            et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            tvContent.setText("请输入手机收到的验证码和新密码");
            tvTitle1.setText("输入新密码");
            tvTitle2.setText("确认新密码");
            tvTitle3.setText("输入验证码");
            tvTitle4.setText("输入姓名");
            bt.setCenterString("重设密码");

        } else if (type == 6) {
            rl2.setVisibility(View.GONE);
            rl3.setVisibility(View.GONE);
            rl4.setVisibility(View.GONE);
            tvTitle3.setVisibility(View.GONE);
            tvTitle4.setVisibility(View.GONE);
            tvTitle2.setVisibility(View.GONE);
            ivTitle.setImageResource(R.drawable.creattem);
            ivEt.setImageResource(R.drawable.chahao);
            ivEt.setTag("del");
            tvContent.setText("请输入团队名称，即刻开始新的协作");
            tvTitle1.setText("输入团队名称");
            bt.setCenterString("创建");

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v == ivEt) {
            if (ivEt.getTag().equals("del")) {
                et1.setText("");
            } else if (ivEt.getTag().equals("yanbi")) {
                ivEt.setTag("yanzheng");
                ivEt.setImageResource(R.drawable.yanzheng);
                et1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                et2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else if (ivEt.getTag().equals("yanzheng")) {
                ivEt.setTag("yanbi");
                ivEt.setImageResource(R.drawable.yanbi);
                et1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                et2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        } else if (v == bt) {
            if (bt.getCenterTextView().getText().equals("下一步")) {
                String url = null;
                if (type == 0) {
                    url = Const.RTOA.URL_REGISTER_1;
                } else if (type == 3) {
                    url = Const.RTOA.URL_FORGET_1;
                }
                if (et1.getText().toString().trim().length() > 0) {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("account", et1.getText().toString());
                    doHttpAsync(url, hashMap);
                } else {
                    ToastUtill.showMessage("所填内容不能为空");
                }

            } else if (bt.getCenterTextView().getText().equals("登录")) {
                if (et1.getText().toString().trim().length() > 0
                        && et2.getText().toString().trim().length() > 0) {
                    okGoLogin(et1.getText().toString(), et2.getText().toString());
                } else {
                    ToastUtill.showMessage("所填内容不能为空");
                }
            } else if (bt.getCenterTextView().getText().equals("重设密码")) {
                if (et1.getText().toString().trim().length() > 0
                        && et2.getText().toString().trim().length() > 0
                        && et3.getText().toString().trim().length() > 0
                        && et4.getText().toString().trim().length() > 0) {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("code", et3.getText().toString());
                    hashMap.put("account", getIntent().getStringExtra("zhanghao"));
                    hashMap.put("p", et1.getText().toString());
                    hashMap.put("rp", et2.getText().toString());
                    hashMap.put("sessionid", getIntent().getStringExtra("sessionid"));
                    doHttpAsync(Const.RTOA.URL_FORGET_2, hashMap);
                } else {
                    ToastUtill.showMessage("所填内容不能为空");
                }
            } else if (bt.getCenterTextView().getText().equals("完成注册")) {
                if (type == 4) {
                    et3.setText("1");
                }
                if (et1.getText().toString().trim().length() > 0
                        && et2.getText().toString().trim().length() > 0
                        && et3.getText().toString().trim().length() > 0
                        && et4.getText().toString().trim().length() > 0) {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    if (type == 1) {
                        hashMap.put("code", et3.getText().toString());
                    }
                    hashMap.put("account", getIntent().getStringExtra("zhanghao"));
                    hashMap.put("name", et4.getText().toString());
                    hashMap.put("p", et1.getText().toString());
                    hashMap.put("rp", et2.getText().toString());
                    hashMap.put("sessionid", getIntent().getStringExtra("sessionid"));
                    hashMap.put("avatar", drawBitmap(et4.getText().toString().substring(0, 1)));
                    doHttpAsync(Const.RTOA.URL_REGISTER_2, hashMap);
                } else {
                    ToastUtill.showMessage("所填内容不能为空");
                }
            } else if (bt.getCenterTextView().getText().equals("创建")) {
                if (et1.getText().toString().trim().length() > 0) {
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("name", et1.getText().toString());
                    doHttpAsync(Const.RTOA.URL_GREATTEAM, hashMap);
                } else {
                    ToastUtill.showMessage("所填内容不能为空");
                }
            }
        } else if (v == tvpassword) {
            Intent i = new Intent(this, RegisterActivity.class);
            i.putExtra("type", 3);
            startActivity(i);
            finish();
        }
    }

    //登录方法
    private void okGoLogin(final String u, final String p) {

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
                            String account = null;
                            if (getIntent().getStringExtra("zhanghao") == null) {
                                account = u;
                            } else {
                                account = getIntent().getStringExtra("zhanghao");
                            }
                            SimplePreferences preferences = Treasure.get(getApplicationContext()
                                    , SimplePreferences.class);
                            preferences.setUsername(account);
                            preferences.setPassword(p);
                            preferences.setIsLogin(true);
                            startActivity(new Intent(RegisterActivity.this
                                    , Main2Activity.class));
                            finish();
                        } else {
                            ToastUtill.showMessage(data.msg);
                        }
                    }
                });

    }

    private int[] colors = {R.color.head_back_0, R.color.head_back_1, R.color.head_back_2,
            R.color.head_back_3, R.color.head_back_4, R.color.head_back_5, R.color.head_back_6};

    /**
     * 根据图片和文字生成图片
     */
    private String drawBitmap(String text) {
        if (et4.getText().toString().trim().length() > 0) {
            Random random = new Random();
            int height = DisplayUtil.sp2px(this, 64);
            Bitmap bitmap = Bitmap.createBitmap(height, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(getResources().getColor(colors[random.nextInt(colors.length)]));//填充颜色
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            canvas.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);

            // 绘制居中文字
            paint.setTextSize(DisplayUtil.sp2px(this, 32));
            paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
            // 文字宽
            float textWidth = paint.measureText(text);
            // 文字baseline在y轴方向的位置
            float baseLineY = Math.abs(paint.ascent() + paint.descent()) / 2;
            canvas.drawText(text, -textWidth / 2, baseLineY, paint);
            return Base64BitmapUtil.bitmapToBase64(bitmap);
        } else {
            return "";
        }
    }

    public void doHttpAsync(String url, Map<String, String> hashMap) {
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {

                            if (type == 0) {
                                Intent i = new Intent(RegisterActivity.this
                                        , RegisterActivity.class);
                                int type1;
                                if (response.body().msg.equals("email")) {
                                    type1 = 4;
                                } else {
                                    type1 = 1;
                                }
                                i.putExtra("type", type1);
                                i.putExtra("zhanghao", et1.getText().toString());
                                i.putExtra("sessionid", response.body().data.sessionid);
                                startActivity(i);
                                finish();
                            } else if (type == 1) {
                                okGoLogin(et4.getText().toString(), et2.getText().toString());
                            } else if (type == 4) {
                                okGoLogin(et4.getText().toString(), et2.getText().toString());
                            } else if (type == 3) {
                                Intent i = new Intent(RegisterActivity.this
                                        , RegisterActivity.class);
                                int type1;
                                if (response.body().msg.equals("phone")) {
                                    type1 = 5;
                                } else {
                                    showdialog(response.body().msg);
                                    return;
                                }
                                i.putExtra("type", type1);
                                i.putExtra("zhanghao", et1.getText().toString());
                                i.putExtra("sessionid", response.body().data.sessionid);
                                startActivity(i);
                                finish();
                            } else if (type == 5) {
                                ToastUtill.showMessage("设置成功");
                                Intent i = new Intent(RegisterActivity.this
                                        , RegisterActivity.class);
                                i.putExtra("type", 2);
                                startActivity(i);
                                finish();
                            } else if (type == 6) {
                                ToastUtill.showMessage("创建成功");
                                Intent intent = new Intent();
                                // 获取用户计算后的结果
                                //通过intent对象返回结果，必须要调用一个setResult方法，
                                //setResult(resultCode, data);第一个参数表示结果返回码，一般只要大于1就可以，但是
                                setResult(3, intent);
                                finish(); //结束当前的activity的生命周期
                            }


                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }


                    }
                });

    }

    private void showdialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();

    }

}
