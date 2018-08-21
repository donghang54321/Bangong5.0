package com.rentian.newoa.modules.main.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.farsunset.cim.sdk.android.CIMEventListener;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.google.gson.reflect.TypeToken;
import com.gxz.PagerSlidingTabStrip;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.PackageInfoUtil;
import com.rentian.newoa.modules.apply.view.fragment.ApplyListFragment;
import com.rentian.newoa.modules.communication.view.ContactDetailsActivity;
import com.rentian.newoa.modules.contactlist.view.ContactListFragment;
import com.rentian.newoa.modules.examiation.view.ExaminationListActivity;
import com.rentian.newoa.modules.information.view.fragment.NotificationFragment;
import com.rentian.newoa.modules.login.view.LoginActivity;
import com.rentian.newoa.modules.main.adapter.MyFragmentPagerAdapter;
import com.rentian.newoa.modules.meeting.VoiceMeetingActivity;
import com.rentian.newoa.modules.meeting.bean.Msg;
import com.rentian.newoa.modules.note.view.AllReportFragment;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        CIMEventListener {

    private String[] titles = {"联盟通知", "通讯录", "培训课堂"
            , "培训笔记", "发送申请"};
    private ViewPager mPager;
    private PagerSlidingTabStrip tabs;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Fragment> pagerList = new ArrayList<>();
    private ImageView ivMeeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        CIMListenerManager.registerMessageListener(this);
        CIMPushManager.connect(this, Const.RTOA.CIM_SERVER_HOST, Const.RTOA.CIM_SERVER_PORT);
        for (int i = 0; i < titles.length; i++) {
            list.add(titles[i]);

        }
        pagerList.add(NotificationFragment.newInstance("0", "1"));
        pagerList.add(new ContactListFragment());
        pagerList.add(NotificationFragment.newInstance("1", "1"));
        pagerList.add(new AllReportFragment());
        pagerList.add(new ApplyListFragment());
        initView();
        update();
    }

    private View viewRight;
    private android.app.AlertDialog dialogRight;
    private PopupWindow mPopupwinow;
    private TextView tvRight;

    private void initView() {
        ivMeeting = findViewById(R.id.title_right_iv);
        tvRight = findViewById(R.id.tv_examina);
        viewRight = LayoutInflater.from(this).inflate(R.layout.apply_menu_two, null);
        dialogRight = new android.app.AlertDialog.Builder(this).setView(viewRight).create();
        dialogRight.getWindow().setGravity(Gravity.TOP);
        dialogRight.getWindow().setGravity(Gravity.RIGHT);
        mPager = (ViewPager) findViewById(R.id.vp);
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this, pagerList, list));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                setTitleColor(position);
                if (position == 4) {
                    ivMeeting.setVisibility(View.GONE);
                    tvRight.setVisibility(View.VISIBLE);
                } else {
                    tvRight.setVisibility(View.GONE);
                    ivMeeting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setOffscreenPageLimit(titles.length);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        tabs.setViewPager(mPager);
        tabs.setShouldExpand(true);

// 设置Tab的分割线的颜色
        tabs.setDividerColor(getResources().getColor(R.color.colorAccent));
// 设置分割线的上下的间距,传入的是dp
        tabs.setDividerPaddingTopBottom(12);

// 设置Tab底部线的高度,传入的是dp
        tabs.setUnderlineHeight(1);
//设置Tab底部线的颜色
//        tabs.setUnderlineColor(getResources().getColor(R.color.red));

// 设置Tab 指示器Indicator的高度,传入的是dp
        tabs.setIndicatorHeight(4);
// 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.colorAccent));

// 设置Tab标题文字的大小,传入的是sp
        tabs.setTextSize(12);
// 设置选中Tab文字的颜色
        tabs.setSelectedTextColor(getResources().getColor(R.color.colorAccent));
//设置正常Tab文字的颜色
        tabs.setTextColor(getResources().getColor(R.color.Black));
//设置Tab文字的左右间距,传入的是dp
        tabs.setTabPaddingLeftRight(10);

//设置点击每个Tab时的背景色
//        tabs.setTabBackground(null);

//是否支持动画渐变(颜色渐变和文字大小渐变)
        tabs.setFadeEnabled(false);
// 设置最大缩放,是正常状态的0.3倍
        tabs.setZoomMax(0.3F);

//这是当点击tab时内容区域Viewpager是否是左右滑动,默认是true
        tabs.setSmoothScrollWhenClickTab(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack(View view) {
        startActivity(new Intent(this, VoiceMeetingActivity.class));
    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出软件", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    private void update() {
        OkHttpUtil.getDefault(MainActivity.this).doGetAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_CHECK_UPDATE).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        Log.e("update", info.getRetDetail());
                        if (info.isSuccessful()) {
                            Type type = new TypeToken<Msg>() {
                            }.getType();
                            Msg version = CommonUtil.gson.fromJson(
                                    info.getRetDetail(), type);
                            if (new PackageInfoUtil().getVersionCode() < version.version) {
                                creatDialog();
                            }
                        } else {

                        }
                    }
                }
        );

    }

    private AlertDialog.Builder dialog;

    private void creatDialog() {
        dialog = new AlertDialog.Builder(this);  //先得到构造器
        dialog.setTitle("更新"); //设置标题
        dialog.setMessage("软件有新版可以下载"); //设置内容
        dialog.setIcon(R.drawable.app_logo);//设置图标，图片id即可
        dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); //关闭dialog
                final Uri uri = Uri.parse("https://fir.im/xrb9");
                final Intent it = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(it);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        //参数都设置完成了，创建并显示出来
        dialog.create().show();

    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

    }

    @Override
    public void onReplyReceived(ReplyBody replybody) {

    }

    @Override
    public void onSentSuccessed(SentBody body) {

    }

    @Override
    public void onNetworkChanged(NetworkInfo networkinfo) {

    }

    @Override
    public void onConnectionSuccessed(boolean hasAutoBind) {
        //连接服务端
        if (!hasAutoBind) {
            CIMPushManager.bindAccount(this, MyApp.getInstance().getMyUid());
        }
    }

    @Override
    public void onConnectionClosed() {

    }

    @Override
    public void onConnectionFailed() {

    }

    @Override
    public int getEventDispatchOrder() {
        return 0;
    }

    public void onTitleLeftClick(View view) {
        Intent intent = new Intent(this, ContactDetailsActivity.class);
        intent.putExtra("isMe", true);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
