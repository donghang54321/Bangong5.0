package com.rentian.newoa.modules.todolist.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Visibility;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoyz.treasure.Treasure;
import com.bumptech.glide.Glide;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.noober.menu.FloatMenu;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.callback.JsonCallback;
import com.rentian.newoa.common.bean.SimplePreferences;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.BitmapUtil;
import com.rentian.newoa.common.utils.TestMenuItem;
import com.rentian.newoa.common.utils.ToastUtill;
import com.rentian.newoa.common.utils.WakeLockUtil;
import com.rentian.newoa.common.view.ComplexPopup;
import com.rentian.newoa.common.view.NoScrollViewPager;
import com.rentian.newoa.common.view.RecycleViewDivider;
import com.rentian.newoa.modules.app.view.AppFragment;
import com.rentian.newoa.modules.kaoqin.view.DakaActivity;
import com.rentian.newoa.modules.login.view.LoginActivity;
import com.rentian.newoa.modules.main.adapter.FragmentAdapter;
import com.rentian.newoa.modules.setting.view.SettingFragment;
import com.rentian.newoa.modules.setting.view.SettingsActivity;
import com.rentian.newoa.modules.todolist.adapter.PoplistAdapter;
import com.rentian.newoa.modules.todolist.bean.AllData;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.bean.TaskData;
import com.rentian.newoa.modules.todolist.fragment.AddtaskFragment;
import com.rentian.newoa.modules.todolist.fragment.MainFragment;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.showzeng.demo.Interface.DialogFragmentDataCallback;
import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends Base2Activity
        implements NavigationView.OnNavigationItemSelectedListener,
        DialogFragmentDataCallback, SetActionBarListener {
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private MainFragment mainFragment;
    private AppFragment appFragment;
    private SettingFragment settingFragment;
    private NavigationView navigationView;
    private RelativeLayout rlTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
//
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initVP();
        setStatusBarDarkMode();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            setupWindowAnimations();
//        okhttPLogin();
        BitmapUtil.init();
        initTrack();
        WakeLockUtil.acquireWakeLock(this);
        registerGPSReceiver();
        okGoLogin();
    }


    private void refreshInfo(AllData allData){
        mGongsi.clear();
        mGongsi.addAll(allData.company);
        DataList dataList = new DataList();
        dataList.titleType = 2;
        DataList dataList2 = new DataList();
        dataList2.typeId = 1;
        dataList2.titleType = 1;
        DataList dataList3 = new DataList();
        dataList3.titleType = 1;
        dataList3.typeId = 2;
        mGongsi.add(dataList);
        mGongsi.add(dataList2);
        mGongsi.add(dataList3);
        mPoplistAdapter.notifyDataSetChanged();
    }

    //登录方法
    private void okGoLogin() {
        CIMPushManager.connect(Main2Activity.this,
                Const.RTOA.CIM_SERVER_HOST, Const.RTOA.CIM_SERVER_PORT);

        mGongsi.clear();
        //// TODO: 2018/7/27 测试多添加了两次公司
        mGongsi.addAll(loginData.data.company);
        mGongsi.addAll(loginData.data.company);
        mGongsi.addAll(loginData.data.company);
        DataList dataList = new DataList();
        dataList.titleType = 2;
        DataList dataList2 = new DataList();
        dataList2.typeId = 1;
        dataList2.titleType = 1;
        DataList dataList3 = new DataList();
        dataList3.titleType = 1;
        dataList3.typeId = 2;
        mGongsi.add(dataList);
        mGongsi.add(dataList2);
        mGongsi.add(dataList3);
        tvTitle.setText(loginData.data.cn);
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("cid", "" + loginData.data.cid);
//        settingFragment.initData(loginData);
        initGongsi();
        MyApp.getInstance().initYingyan("" + loginData.data.uid);
        startCaiji();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private NoScrollViewPager viewPager;
    private LinearLayout linearLayout;
    private TextView tvTitle;

    private void initVP() {
        rlTitle=findViewById(R.id.rl_title);
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Main2Activity.this, DakaActivity.class));
            }
        });
        tvTitle = findViewById(R.id.tv_toolbar);
        viewPager = findViewById(R.id.vp_main);
        viewPager.setNoScroll(true);
        linearLayout = findViewById(R.id.tab);
        imageView1 = findViewById(R.id.main_tab_iv1);
        imageView2 = findViewById(R.id.main_tab_iv2);
        imageView3 = findViewById(R.id.main_tab_iv3);
        textView1 = findViewById(R.id.main_tab_tv1);
        textView2 = findViewById(R.id.main_tab_tv2);
        textView3 = findViewById(R.id.main_tab_tv3);
        viewPager.setNoScroll(true);
        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);
        textViews.add(textView1);
        textViews.add(textView2);
        textViews.add(textView3);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(mainFragment = MainFragment.newInstance("1", loginData.data.cid + ""));
        fragments.add(appFragment = AppFragment.newInstance(loginData.data.cid + ""
                , loginData.data.avatar));
        fragments.add(settingFragment = SettingFragment.newInstance(loginData.data.cid
                + "", loginData.data.avatar));
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
                this, fragments));


    }

    private void initmenu() {
        final FloatMenu floatMenu = new FloatMenu(this);
        List<TestMenuItem> itemList = new ArrayList<>();
        TestMenuItem menuItem = new TestMenuItem();
        menuItem.setItem("菜单1");
        itemList.add(menuItem);
        TestMenuItem menuItem2 = new TestMenuItem();
        menuItem2.setItem("菜单2");
        itemList.add(menuItem2);
        TestMenuItem menuItem3 = new TestMenuItem();
        menuItem3.setItem("菜单2");
        itemList.add(menuItem3);
//		floatMenu.items("菜单1", "菜单2", "菜单3");
        floatMenu.items(itemList);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                AddtaskFragment addtaskFragment = new AddtaskFragment();
                addtaskFragment.show(getFragmentManager(), "todolist");
            }
        });
        floatMenu.show(point);

    }

    private void initRankmenu() {
        final FloatMenu floatMenu = new FloatMenu(this);
        floatMenu.inflate(R.menu.menu_normal);
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                //// TODO: 2018/7/5
            }
        });
        floatMenu.show(point);

    }

    public void onTabClick0(View v) {
//        toolbar.setTitle("工作台");
        onTab(v);
        viewPager.setCurrentItem(0);
    }

    public void onTabClick1(View v) {
//        toolbar.setTitle("应用");
        onTab(v);
        viewPager.setCurrentItem(1);
    }

    public void onTabClick2(View v) {
//        toolbar.setTitle("设置");
        onTab(v);
        viewPager.setCurrentItem(2);
    }

    private ImageView imageView1, imageView2, imageView3;
    private TextView textView1, textView2, textView3;
    private ArrayList<TextView> textViews = new ArrayList<>();
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int intImageViewArray2[] = {
            R.drawable.tab_off1, R.drawable.tab_off2,
            R.drawable.tab_off3};

    private void onTab(View v) {

        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setImageResource(intImageViewArray2[i]);
            textViews.get(i).setTextColor(getResources().getColor(R.color.tab_top_text_1));
        }
        if (v.getId() == R.id.tab_rl_1) {
            imageViews.get(0).setImageResource(R.drawable.tab_on1);
            textView1.setTextColor(getResources().getColor(R.color.colorAccent));
        } else if (v.getId() == R.id.tab_rl_2) {
            imageViews.get(1).setImageResource(R.drawable.tab_on2);
            textView2.setTextColor(getResources().getColor(R.color.colorAccent));
        } else if (v.getId() == R.id.tab_rl_3) {
            imageViews.get(2).setImageResource(R.drawable.tab_on3);
            textView3.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    private EasyPopup mCirclePop;

    private void showPop() {
        mCirclePop = EasyPopup.create(this)
                .setContentView(R.layout.layout_popupwindow, ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (getResources().getDisplayMetrics().heightPixels / 3.5))
                .setAnimationStyle(R.style.BottomPopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.2f)
                .apply();
        mCirclePop.showAtAnchorView(linearLayout, YGravity.ALIGN_BOTTOM, XGravity.CENTER);

    }

    private ArrayList<DataList> mGongsi = new ArrayList<>();
    private PoplistAdapter mPoplistAdapter;
    private ComplexPopup mComplexPopup;
    private void initComplexPop() {
        mComplexPopup = ComplexPopup.create(this)
                .setDimView(viewPager)
                .apply();

    }
    private void showComplexPop(View view) {
//        mComplexPopup.showAtAnchorView(view, YGravity.ABOVE, XGravity.LEFT);
        mComplexPopup.showAtLocation(view, Gravity.BOTTOM,0,0);
    }
    private void initGongsi() {
        RecyclerView mRecyclerView = navigationView
                .findViewById(R.id.recycler_view);
        TextView tvName = navigationView.findViewById(R.id.tv_name);
        TextView tvDept = navigationView.findViewById(R.id.tv_dept);
        TextView tvPost = navigationView.findViewById(R.id.tv_postion);
        ImageView ivSet= navigationView.findViewById(R.id.iv_set);
        ivSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent
                        (Main2Activity.this, SettingsActivity.class),REQUESTCODE);
            }
        });
        CircleImageView touxiang = navigationView.findViewById(R.id.touxiang);
        Glide.with(this)
                .load(Const.RTOA.URL_BASE + loginData.data.avatar)
                .into(touxiang);
        tvName.setText(loginData.data.name);
        tvDept.setText(loginData.data.dept );
        tvPost.setText(loginData.data.post );
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPoplistAdapter = new PoplistAdapter(this, mGongsi, loginData.data.cid, this);
        mRecyclerView.setAdapter(mPoplistAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 1, R.color.task_text2));

    }

    private void showListPop() {
        if (loginData == null)
            return;
        mCirclePop = EasyPopup.create(this)
                .setContentView(R.layout.pop_recycleview, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                .setAnimationStyle(R.style.BottomPopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                .apply();
        RecyclerView mRecyclerView = mCirclePop.getContentView()
                .findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPoplistAdapter = new PoplistAdapter(this, mGongsi, loginData.data.cid, this);
        mRecyclerView.setAdapter(mPoplistAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(
                this, LinearLayoutManager.VERTICAL, 1, R.color.task_text2));
        mCirclePop.showAtLocation(linearLayout, YGravity.ALIGN_BOTTOM,0,0);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            initmenu();
//            startActivity(new Intent(this, SyllabusActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#303F9F"));
        mActionBar.setBackgroundDrawable(drawable);
        return super.onSupportNavigateUp();
    }

    private int rank = 1;

    public void addTask(View view) {
        mCirclePop.dismiss();
        rank = 1;
        AddtaskFragment addtaskFragment = new AddtaskFragment();
        addtaskFragment.show(getFragmentManager(), "todolist");
    }

    @Override
    public String getCommentText() {
        return "";
    }

    @Override
    public void setCommentText(String commentTextTemp) {

    }

    @Override
    public void setRank(int rank) {
        this.rank = rank;
//        initRankmenu();
//        dialogChoice();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void sendPinglun(String commentTextTemp) {
        // 创建任务
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("title", commentTextTemp);
        hashMap.put("rank", "" + rank);
        String uid = "";
        if (MyApp.getInstance().idlist.size() != 0) {
            for (String id : MyApp.getInstance().idlist) {
                uid += id + ",";
            }
            hashMap.put("uid", uid);
        }
        MyApp.getInstance().idlist.clear();
        doHttpAsync(Const.RTOA.URL_CREAT_TASK, hashMap);

    }


    @Override
    public void setActionBar(int i, String str) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        tvTitle.setText(mGongsi.get(i).mingcheng);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("cid", "" + mGongsi.get(i).qiye);
        mainFragment.cid = "" + mGongsi.get(i).qiye;
        MyApp.getInstance().qiye= mGongsi.get(i).qiye;
        mainFragment.doHttpAsync(Const.RTOA.URL_TODOLIST_DATA, hashMap);

    }

    private Point point = new Point();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            point.x = (int) ev.getRawX();
            point.y = (int) ev.getRawY();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void doHttpAsync(String url, Map<String, String> hashMap) {
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        Map<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("cid", "" + mainFragment.cid);
                        mainFragment.doHttpAsync(Const.RTOA.URL_TODOLIST_DATA, hashMap);
                    }
                });

    }

    private void setupWindowAnimations() {
        // 首次进入显示的动画
        Visibility visibility = buildEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(visibility);
        }

        // 重新进入的动画。即第二次进入，可以和首次进入不一样。
        // visibility = buildReturnTransition();
        // getWindow().setReenterTransition(visibility);

        // 启动新 Activity ，此页面退出的动画
        visibility = buildReturnTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(visibility);
        }

        // 调用 finishAfterTransition() 退出时，此页面退出的动画
        // visibility = buildReturnTransition();
        // getWindow().setReturnTransition(visibility);
    }

    private Visibility buildEnterTransition() {
        Fade enterTransition = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            enterTransition = new Fade();
            enterTransition.setDuration(1500);
        }
        // 此视图将不会受到输入过渡动画的影响
        // enterTransition.excludeTarget(R.id.square_red, true);
        return enterTransition;
    }


    private Visibility buildReturnTransition() {

        Visibility visibility = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            visibility = new Slide();
            visibility.setDuration(1000);
        }
        return visibility;
    }

    @Override
    public void onConnectionSuccessed(boolean autoBind) {
        //连接服务端
        Log.e("长连接", autoBind + "登录" + MyApp.getInstance().getMyUid());
        if (!autoBind) {
            CIMPushManager.bindAccount(this, MyApp.getInstance().getMyUid());
        }
    }

    //收到消息
    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

        if (message.getAction().equals(Const.MessageType.TYPE_999)) {
            Log.e("长连接：", "下线" + MyApp.getInstance().getMyUid());
            //返回登录页面，停止接受消息
            CIMPushManager.stop(this);
            SimplePreferences preferences = Treasure.get(this
                    , SimplePreferences.class);
            preferences.setIsLogin(false);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else if (message.getAction().equals(Const.MessageType.TYPE_5)) {
//            Message message1 = new Message();
//            message1.name = message.getExtra();
//            message1.content = message.getContent();
            Map<String, String> hashMap = new HashMap<String, String>();
            hashMap.put("cid", "" + mainFragment.cid);
            mainFragment.doHttpAsync(Const.RTOA.URL_TODOLIST_DATA, hashMap);


        } else if (message.getAction().equals(Const.MessageType.TYPE_3)) {
        }

    }

    protected void getUsreinfo(String url, Map<String, String> hashMap) {
        OkGo.<TaskData>post(url)
                .tag(this)
                .params(hashMap)
                .execute(new JsonCallback<TaskData>(TaskData.class) {
                    @Override
                    public void onSuccess(Response<TaskData> response) {
                        if (response.body().code == 0) {
                            refreshInfo(response.body().data);

                        } else {
                            ToastUtill.showMessage(response.body().msg);
                        }


                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public final static int REQUESTCODE = 1; // 返回的结果码

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 3) {
            if (requestCode == REQUESTCODE) {
                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("time", "1");
                getUsreinfo(Const.RTOA.URL_USER_INFO, hashMap);
            }
        }else if (resultCode == 4){
            if (requestCode == REQUESTCODE) {
                startActivity(new Intent(this,LoginActivity.class));
                finish();
            }
        }
    }
}
