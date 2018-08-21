package com.rentian.newoa.modules.kaoqin.view;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.bumptech.glide.Glide;
import com.rentian.newoa.Base2Activity;
import com.rentian.newoa.R;
import com.rentian.newoa.common.utils.LQRPhotoSelectUtils;
import com.rentian.newoa.common.utils.Uri2BitmapUtil;
import com.rentian.newoa.modules.kaoqin.adapter.ImageAdapter;
import com.rentian.newoa.modules.kaoqin.bean.PicData;
import com.rentian.newoa.modules.todolist.adapter.PoplistAdapter;
import com.rentian.newoa.modules.todolist.bean.DataList;
import com.rentian.newoa.modules.todolist.imodules.SetActionBarListener;
import com.scu.miomin.shswiperefresh.core.SHSwipeRefreshLayout;
import com.zyyoona7.popup.EasyPopup;
import com.zyyoona7.popup.XGravity;
import com.zyyoona7.popup.YGravity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class DakaActivity extends Base2Activity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener
        , View.OnClickListener, SetActionBarListener {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    MapView mMapView;
    BaiduMap mBaiduMap;
    private ImageView bottom;
    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;
    private float direction;
    private SuperTextView bt;
    private TextView tvAddress, tvAddress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daka);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("打卡");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setStatusBarDarkMode();
        tvAddress = findViewById(R.id.tv_add);
        tvAddress.setOnClickListener(this);
        tvAddress2 = findViewById(R.id.tv_add2);
        bottom = findViewById(R.id.bottom);
        bt = findViewById(R.id.bt);
        bt.setOnClickListener(this);

        // 地图初始化
        initMap();
        initPic();
    }

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private List<String> suggest;
    private int loadIndex = 0;

    private void initMap() {
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mMapView.removeViewAt(1);
        mMapView.showZoomControls(false);
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化建议搜索模块，注册建议搜索事件监听
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);

        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker,
                accuracyCircleFillColor, accuracyCircleStrokeColor));
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();


    }

    private RecyclerView mRvPic;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    private ImageAdapter imageAdapter;
    private ArrayList<PicData> imgs=new ArrayList<>();

    private void initPic() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
//                mTvPath.setText(outputFile.getAbsolutePath());
//                mTvUri.setText(outputUri.toString());
//                Glide.with(DakaActivity.this).load(outputUri).into(mIvPic);
                PicData ivdata=new PicData();
                try {
                    ivdata.bt= Uri2BitmapUtil.getBitmapFormUri(DakaActivity.this,outputUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivdata.img=outputUri.toString();
                imgs.add(0,ivdata);
                if (imgs.size()==5){
                    imgs.remove(4);
                }
                imageAdapter.notifyDataSetChanged();
            }
        }, false);//true裁剪，false不裁剪

        mRvPic = findViewById(R.id.recycler_iv);
        LinearLayoutManager ms = new LinearLayoutManager(this);
        // 设置 recyclerview 布局方式为横向布局
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvPic.setLayoutManager(ms);
        PicData ivdata=new PicData();
        ivdata.img="1";
        imgs.add(ivdata);
        imageAdapter = new ImageAdapter(this, imgs);
        mRvPic.setAdapter(imageAdapter);



    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        //        Toast.makeText(getApplicationContext(), "不给我权限是吧，那就别玩了", Toast.LENGTH_SHORT).show();
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + DakaActivity.this.getPackageName()));
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }
//        mBtnSelectPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 3、调用从图库选取图片方法
//                PermissionGen.needPermission(MainActivity.this,
//                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
//                );
//            }
//        });

//    @Override
//    public void onSensorChanged(SensorEvent sensorEvent) {
//        double x = sensorEvent.values[SensorManager.DATA_X];
//        if (Math.abs(x - lastX) > 1.0) {
//            mCurrentDirection = (int) x;
//            locData = new MyLocationData.Builder()
//                    .accuracy(mCurrentAccracy)
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(mCurrentDirection).latitude(mCurrentLat)
//                    .longitude(mCurrentLon).build();
//            mBaiduMap.setMyLocationData(locData);
//        }
//        lastX = x;
//
//    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    LatLng center = null;

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            //// TODO: 2018/8/16 处理未找到结果
//            Toast.makeText(PoiSearchDemo.this, "未找到结果", Toast.LENGTH_LONG)
//                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            //// TODO: 2018/8/16 处理搜索结果结果
            if (result.getCurrentPageNum() == 0)
                mAddress.clear();
            List<PoiInfo> list = result.getAllPoi();
            for (PoiInfo info : list) {
                DataList data = new DataList();
                data.titleType = 4;
                data.name = info.name;
                data.content = info.address;
                mAddress.add(data);

            }
            if (result.getCurrentPageNum() == 0) {
                if (list != null && list.size() > 0) {
                    tvAddress.setText(list.get(0).name);
                    tvAddress2.setText(list.get(0).address);
                } else {
                    tvAddress.setText("无相关位置");
                }
                if (mPoplistAdapter != null) {
                    mPoplistAdapter.notifyDataSetChanged();
                }
            } else {
                mPoplistAdapter.notifyDataSetChanged();
                swipeRefreshLayout.finishLoadmore();
            }
            loadIndex = result.getCurrentPageNum() + 1;
//            if (result.getTotalPageNum() > result.getCurrentPageNum()) {
//                searchNearby(result.getCurrentPageNum() + 1);
//            }
        }

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        Log.e("poi", poiDetailResult.getAddress());

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        if (res == null || res.getAllSuggestions() == null) {
            return;
        }
//        suggest = new ArrayList<String>();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
            if (info.key != null) {
                DataList data = new DataList();
                data.titleType = 4;
                data.name = info.key;
                data.content = info.address;
                mAddress.add(data);
            }
        }

    }

    /**
     * 检索附近公司
     */
    private void searchNearby(int loadIndex) {
        mPoiSearch.searchNearby(new PoiNearbySearchOption()
                .keyword("公司")
                .sortType(PoiSortType.distance_from_near_to_far)
                .location(center)
                .radius(100)
                .radiusLimit(true)
                .pageNum(loadIndex));
    }

    @Override
    public void onClick(View v) {
        if (v == tvAddress) {

            showListPop();
            mCirclePop.showAtAnchorView(bottom, YGravity.ALIGN_BOTTOM, XGravity.CENTER);
            searchNearby(loadIndex);
        }
    }

    @Override
    public void setActionBar(int i, String s) {
        tvAddress.setText(mAddress.get(i).name);
        tvAddress2.setText(mAddress.get(i).content);
        mCirclePop.dismiss();

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
//            mCurrentLat = 38.050602;
//            mCurrentLon = 114.618195;
            center = new LatLng(mCurrentLat, mCurrentLon);
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                searchNearby(loadIndex);
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    private EasyPopup mCirclePop;
    private ArrayList<DataList> mAddress = new ArrayList<>();
    private PoplistAdapter mPoplistAdapter;
    private SHSwipeRefreshLayout swipeRefreshLayout;

    private void showListPop() {
        mCirclePop = EasyPopup.create(this)
                .setContentView(R.layout.pop_recycleview, ViewGroup.LayoutParams.MATCH_PARENT,
                        (int) (getResources().getDisplayMetrics().heightPixels / 1.5))
                .setAnimationStyle(R.style.BottomPopAnim)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                .apply();
        swipeRefreshLayout =
                mCirclePop.getContentView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshEnable(false);
        swipeRefreshLayout.setLoadmoreEnable(true);
        RecyclerView mRecyclerView = mCirclePop.getContentView()
                .findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPoplistAdapter = new PoplistAdapter(this, mAddress, loginData.data.cid, this);
        mRecyclerView.setAdapter(mPoplistAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                    }
                }, 0);
            }

            @Override
            public void onLoading() {
                searchNearby(loadIndex);
//                swipeRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeRefreshLayout.finishLoadmore();
//                    }
//                }, 0);
            }

            /**
             * 监听下拉刷新过程中的状态改变
             * @param percent 当前下拉距离的百分比（0-1）
             * @param state 分三种状态{NOT_OVER_TRIGGER_POINT：还未到触发下拉刷新的距离；OVER_TRIGGER_POINT：已经到触发下拉刷新的距离；START：正在下拉刷新}
             */
            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("下拉刷新");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("松开刷新");
                        break;
                    case SHSwipeRefreshLayout.START:
                        swipeRefreshLayout.setLoaderViewText("正在刷新");
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("上拉加载");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setLoaderViewText("松开加载");
                        break;
                    case SHSwipeRefreshLayout.START:
                        swipeRefreshLayout.setLoaderViewText("正在加载...");
                        break;
                }
            }
        });
        mCirclePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                loadIndex = 0;

            }
        });

    }
}

