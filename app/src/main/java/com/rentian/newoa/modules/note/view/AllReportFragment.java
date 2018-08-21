package com.rentian.newoa.modules.note.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.CacheType;
import com.okhttplib.callback.CallbackOk;
import com.rentian.newoa.MyApp;
import com.rentian.newoa.R;
import com.rentian.newoa.common.NotifyMessage;
import com.rentian.newoa.common.NotifyMessageManager;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.NetworkUtil;
import com.rentian.newoa.common.view.RefreshLayout;
import com.rentian.newoa.common.view.WhorlView;
import com.rentian.newoa.modules.note.adapter.AllReportAdapter;
import com.rentian.newoa.modules.note.bean.AllReport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AllReportFragment extends Fragment {
    private Context context;
    private ListView listView;
    private List<AllReport> allReports = new ArrayList<>();
    private List<AllReport> addAllReports;
    private AllReportAdapter myAdapter;
    private RefreshLayout myRefreshListView;
    private View view;
    private LinearLayout loadLayout;
    private WhorlView loadView;
    private FrameLayout flSend;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
//        update();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_report_all, container, false);
        initViews(view);
        loadView.start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailsReportActivity.class);
                intent.putExtra("id", allReports.get(position).id);
                intent.putExtra("time", allReports.get(position).time);
                intent.putExtra("name", allReports.get(position).name);
                intent.putExtra("img", allReports.get(position).img);
                intent.putExtra("uid", allReports.get(position).uid);
                startActivity(intent);
            }
        });
        myRefreshListView = (RefreshLayout)
                view.findViewById(R.id.swipe_layout);
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 更新完后调用该方法结束刷新
                        allReports.clear();
                        pn=1;
                        getAllReportInfoByNet();
                        myRefreshListView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {
                getAllReportInfoByNet();
            }
        });
        getAllReportInfoByNet();
        NotifyMessageManager.getInstance().setNotifyMessage(new NotifyMessage() {
            @Override
            public void sendMessage(String msg) {
//                allReports.clear();
                pn=1;
                getNewReprot();
            }
        });
        return view;
    }

    /**
     * 初始化界面组件
     *
     * @param view 父视图
     */
    private void initViews(View view) {
        listView =  view.findViewById(R.id.lv_all_report);
        loadLayout = view.findViewById(R.id.loading_layout);
        loadView =  view.findViewById(R.id.loading_view);
        flSend=view.findViewById(R.id.send);
        flSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SendReportActivity.class));
            }
        });
    }

    private int pn = 1;

    private void getAllReportInfoByNet() {
        int cacheType;
        if (NetworkUtil.isNetworkConnected(getActivity())){
            cacheType= CacheType.FORCE_NETWORK;
        }else {
            cacheType= CacheType.NETWORK_THEN_CACHE;
        }
        OkHttpUtil.Builder()
                .setCacheType(cacheType)
                .build(this)
                .doGetAsync(
                HttpInfo.Builder().setUrl(Const.RTOA.URL_ALL_REPORT_INFO)
                        .addParam("uid", MyApp.getInstance().getMyUid())
                        .addParam("pno", pn + "")
                        .build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(HttpInfo info) throws IOException {
                        if (info.isSuccessful()) {
                            Log.e("addAllReports", info.getRetDetail());
                            Type type = new TypeToken<List<AllReport>>() {
                            }.getType();
                            addAllReports = CommonUtil.gson.fromJson(info.getRetDetail(), type);

                            allReports.addAll(addAllReports);
                            if (addAllReports.size()<10){
                                myRefreshListView.setOnLoadListener(null);
                            }
                            if (myAdapter == null) {
                                myAdapter = new AllReportAdapter(context, allReports);
                                listView.setAdapter(myAdapter);
                                setViewGone(loadLayout);
                            } else {
                                myAdapter.notifyDataSetChanged();
                                // 加载完后调用该方法

                                myRefreshListView.setLoading(false);//
                            }
                            if (pn==1&&allReports.size()==0){
                                myRefreshListView.setVisibility(View.GONE);
                            }
                            pn++;
                        } else {
                            Log.e("addAllReports", info.getRetDetail());
                        }
                    }
                }
        );

    }


    private void getNewReprot() {
        int cacheType;
        if (NetworkUtil.isNetworkConnected(getActivity())){
            cacheType= CacheType.FORCE_NETWORK;
        }else {
            cacheType= CacheType.NETWORK_THEN_CACHE;
        }
        OkHttpUtil.Builder()
                .setCacheType(cacheType)
                .build(this)
                .doGetAsync(
                        HttpInfo.Builder().setUrl(Const.RTOA.URL_ALL_REPORT_INFO)
                                .addParam("uid", MyApp.getInstance().getMyUid())
                                .addParam("pno", pn + "")
                                .build(),
                        new CallbackOk() {
                            @Override
                            public void onResponse(HttpInfo info) throws IOException {
                                if (info.isSuccessful()) {
                                    Log.e("addAllReports", info.getRetDetail());
                                    Type type = new TypeToken<List<AllReport>>() {
                                    }.getType();
                                    allReports.clear();
                                    addAllReports = CommonUtil.gson.fromJson(info.getRetDetail(), type);

                                    allReports.addAll(addAllReports);
                                    if (addAllReports.size()<10){
                                        myRefreshListView.setOnLoadListener(null);
                                    }
                                    if (myAdapter == null) {
                                        myAdapter = new AllReportAdapter(context, allReports);
                                        listView.setAdapter(myAdapter);
                                        setViewGone(loadLayout);
                                    } else {
                                        myAdapter.notifyDataSetChanged();
                                        // 加载完后调用该方法

                                        myRefreshListView.setLoading(false);//
                                    }
                                    pn++;
                                } else {
                                    Log.e("addAllReports", info.getRetDetail());
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

}