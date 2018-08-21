//package com.rentian.rtsxy.modules.apply.view;
//
//import android.app.AlertDialog;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.view.Display;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.TextView;
//
//import com.rentian.rentianoa.R;
//import com.rentian.rentianoa.modules.apply.view.fragment.ApplyListFragment;
//import com.rentian.rentianoa.modules.apply.view.fragment.ExaminationListFragment;
//import com.rentian.rentianoa.modules.report.adapter.ReportPagerAdapter;
//
//import java.util.ArrayList;
//
//import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
//
///**
// * Created by rentianjituan on 2016/5/3.
// */
//public class ApplyMainActivity extends SwipeBackActivity implements View.OnClickListener {
//    private ViewPager viewPager;
//    private ArrayList<Fragment> pagerList;
//    private AlertDialog dialog;
//    private View view;
//    private TextView title;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //透明状态栏
//        if (android.os.Build.VERSION.SDK_INT > 18)
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        setContentView(R.layout.activity_main_apply);
//        pagerList = new ArrayList<>();
//        pagerList.add(new ApplyListFragment());
//        pagerList.add(new ExaminationListFragment());
//
//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        viewPager.setAdapter(new ReportPagerAdapter(getSupportFragmentManager(), this, pagerList));
//        viewPager.setOffscreenPageLimit(1);
//        initPagerListener();
//
//        view = LayoutInflater.from(this).inflate(R.layout.menu_report, null);
//        TextView title_apply= (TextView) view.findViewById(R.id.title1);
//        TextView title_examina= (TextView) view.findViewById(R.id.title2);
//        title_apply.setText("发送申请");
//        title_examina.setText("我的审批");
//        dialog = new AlertDialog.Builder(this).setView(view).create();
//        dialog.getWindow().setGravity(Gravity.TOP);
//        title = (TextView) findViewById(R.id.title);
//        title.setOnClickListener(this);
//        if (getIntent().getIntExtra("action",0)==1){
//            viewPager.setCurrentItem(1, true);
//        }
//
//    }
//    private void initPagerListener() {
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0) {
//                    title.setText("发送申请");
//                }
//                if (position == 1) {
//                    title.setText("我的审批");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//    }
//    public void onBack(View view) {
//        finish();
//    }
//    private boolean once=true;
//    @Override
//    public void onClick(View v) {
//        if (v == title) {
//            if (once) {
//                WindowManager m = getWindowManager();
//                Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//                dialog.show();
//                WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
//                if (d.getWidth() * 0.5 < 300) {
//                    p.width = (int) (d.getWidth() * 0.6);
//                } else {
//                    p.width = (int) (d.getWidth() * 0.5);
//                }
//                dialog.getWindow().setAttributes(p);
//                once = false;
////                Log.e("view.getWidth()", d.getWidth() * 0.5 + "");
//            } else {
//                dialog.show();
//            }
//        }
//    }
//    public void titleClick1(View view) {
//        title.setText("发送申请");
//        viewPager.setCurrentItem(1, true);
//        dialog.dismiss();
//
//    }
//
//    public void titleClick2(View view) {
//        title.setText("我的审批");
//        dialog.dismiss();
//        viewPager.setCurrentItem(0, true);
//    }
//}
