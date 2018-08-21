package com.rentian.newoa.modules.information.view;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.farsunset.cim.sdk.android.CIMEventListener;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.view.RefreshLayout;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.information.asynctask.GetInformationTask;
import com.rentian.newoa.modules.meeting.VoiceMeetingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NotificationActivity extends AppCompatActivity implements
        CIMEventListener {

    private ListView lv;
    private List<Map<String, Object>> list = new ArrayList<>();
    private SimpleAdapter adapter;
    private RefreshLayout myRefreshListView;
    private WhorlView loadView;
    private LinearLayout loadLayout;
    private int pn = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (null != msg.obj) {
                    list.addAll((List<Map<String, Object>>) msg.obj);
                    adapter.notifyDataSetChanged();
                    if (pn > 1)
                        myRefreshListView.setLoading(false);
                    else setViewGone(loadLayout);
                } else {
                    Toast.makeText(NotificationActivity.this, "服务器未响应", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (android.os.Build.VERSION.SDK_INT > 18)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_office_notification);
        findViews();
        CIMListenerManager.registerMessageListener(this);
        CIMPushManager.connect(this, Const.RTOA.CIM_SERVER_HOST, Const.RTOA.CIM_SERVER_PORT);

        registerListeners();
        loadView.start();
        new GetInformationTask(handler.obtainMessage(0))
                .execute(Const.RTOA.URL_INFORMATION + MyApp.getInstance().getMyUid() + "&pn=" + pn);


    }

    private void findViews() {
        myRefreshListView = (RefreshLayout) findViewById(R.id.swipe_layout);
        loadView = (WhorlView) findViewById(R.id.loading_view);
        loadLayout = (LinearLayout) findViewById(R.id.loading_layout);
        lv = (ListView) findViewById(R.id.lv_ofe_notificatin);
        adapter = new SimpleAdapter(
                NotificationActivity.this, list,
                R.layout.item_office_notification_lv, new String[]{
                "notificatin_date",
                "notification_publisher",
                "notification_subject"}, new int[]{
                R.id.notification_date,
                R.id.notification_publisher,
                R.id.notification_subject}) {
            @Override
            public long getItemId(int position) {
                long id = (Integer) list.get(position).get("id");
                return id;
            }
        };

        lv.setAdapter(adapter);
    }

    private void registerListeners() {
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent(NotificationActivity.this,
                        NotificationDetailsActivity.class);
                intent.putExtra("id", arg3);
                startActivity(intent);

            }
        });
        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

                pn++;
                // 更新数据
                new GetInformationTask(handler.obtainMessage(0))
                        .execute(Const.RTOA.URL_INFORMATION + MyApp.getInstance().getMyUid() + "&pn=" + pn);

            }
        });
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                myRefreshListView.setRefreshing(false);
            }
        });
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
       startActivity(new Intent(this, VoiceMeetingActivity.class));
    }

    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(NotificationActivity.this, "再按一次退出软件", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
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
}
