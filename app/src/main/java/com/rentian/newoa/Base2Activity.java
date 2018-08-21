package com.rentian.newoa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.farsunset.cim.sdk.android.CIMEventListener;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.google.gson.reflect.TypeToken;
import com.rentian.newoa.common.CurrentLocation;
import com.rentian.newoa.common.constant.Constants;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.common.utils.TrackReceiver;
import com.rentian.newoa.common.utils.ViewUtil;
import com.rentian.newoa.modules.todolist.bean.TaskData;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rentianjituan on 2018/7/4.
 */

public class Base2Activity extends AppCompatActivity implements CIMEventListener
        , SensorEventListener {

    private static boolean isMiUi = false;

    protected void initWindow() {

    }

    /**
     * 动画类型
     */
    protected static final String EXTRA_TYPE = "type";
    /**
     * 代码 定义动画
     */
    protected static final int TYPE_PROGRAMMATICALLY = 0;
    /**
     * Xml 定义动画
     */
    protected static final int TYPE_XML = 1;
    public TaskData loginData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("loginMsg", MyApp.getInstance().loginMsg);
        if (MyApp.getInstance().loginMsg.trim().length() > 0) {
            Type type = new TypeToken<TaskData>() {
            }.getType();
            loginData = CommonUtil.gson.fromJson(MyApp.getInstance().loginMsg, type);
        }
        CIMListenerManager.registerMessageListener(this);
        initWindow();
    }

    /**
     * 设置小米黑色状态栏字体
     */
    @SuppressLint("PrivateApi")
    private void setMIUIStatusBarDarkMode() {
        if (isMiUi) {
            Class<? extends Window> clazz = getWindow().getClass();
            try {
                int darkModeFlag;
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(getWindow(), darkModeFlag, darkModeFlag);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 静态域，获取系统版本是否基于MIUI
     */

    static {
        try {
            @SuppressLint("PrivateApi") Class<?> sysClass = Class.forName("android.os.SystemProperties");
            Method getStringMethod = sysClass.getDeclaredMethod("get", String.class);
            String version = (String) getStringMethod.invoke(sysClass, "ro.miui.ui.version.name");
            isMiUi = version.compareTo("V6") >= 0 && Build.VERSION.SDK_INT < 24;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置魅族手机状态栏图标颜色风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setMeiZuDarkMode(Window window, boolean dark) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 24) {
            return false;
        }
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @SuppressLint("InlinedApi")
    private int getStatusBarLightMode() {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isMiUi) {
                result = 1;
            } else if (setMeiZuDarkMode(getWindow(), true)) {
                result = 2;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            }
        }
        return result;
    }


    @SuppressLint("InlinedApi")
    protected void setStatusBarDarkMode() {
        int type = getStatusBarLightMode();
        if (type == 1) {
            setMIUIStatusBarDarkMode();
        } else if (type == 2) {
            setMeiZuDarkMode(getWindow(), true);
        } else if (type == 3) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void finish() {
        super.finish();
        CIMListenerManager.removeMessageListener(this);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        CIMListenerManager.registerMessageListener(this);
    }


    @Override
    public void onMessageReceived(Message arg0) {
    }

    ;

    @Override
    public void onNetworkChanged(NetworkInfo info) {
    }

    /**
     * 与服务端断开连接时回调,不要在里面做连接服务端的操作
     */
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

    /**
     * 连接服务端成功时回调
     */

    @Override
    public void onConnectionSuccessed(boolean arg0) {
    }


    @Override
    public void onReplyReceived(ReplyBody arg0) {
    }

    @Override
    public void onSentSuccessed(SentBody sentBody) {

    }

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = null;

    private SensorManager mSensorManager;

    protected void initTrack() {
        initTrackListener();
        viewUtil = new ViewUtil();
        powerManager = (PowerManager) MyApp.getInstance().getSystemService(Context.POWER_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);// 获取传感器管理服务
//        MyApp.getInstance().mClient.startTrace(MyApp.getInstance().mTrace, traceListener);//开始服务
        addBaimingdan();
    }

    protected void startCaiji() {
        MyApp.getInstance().mClient.startTrace(MyApp.getInstance().mTrace, traceListener);//开始服务

    }

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;
    private boolean firstLocate = true;
    private ViewUtil viewUtil;

    /**
     * 打包周期
     */
    public int packInterval = Constants.DEFAULT_PACK_INTERVAL;

    private void initTrackListener() {

        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                //经过服务端纠偏后的最新的一个位置点，回调
                try {
                    if (StatusCodes.SUCCESS != response.getStatus()) {
                        return;
                    }

                    LatestPoint point = response.getLatestPoint();
                    if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                            .getLongitude())) {
                        return;
                    }

//                    LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
//                    if (null == currentLatLng) {
//                        return;
//                    }

                    if (firstLocate) {
                        firstLocate = false;
//                        Toast.makeText(MainActivity.this, "起点获取中，请稍后...", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //当前经纬度
                    CurrentLocation.locTime = point.getLocTime();
                    CurrentLocation.latitude = point.getLocation().getLatitude();
                    CurrentLocation.longitude = point.getLocation().getLongitude();

//                    if (trackPoints == null) {
//                        return;
//                    }
//                    trackPoints.add(currentLatLng);
//
//                    mapUtil.drawHistoryTrack(trackPoints, false, mCurrentDirection);//时时动态的画出运动轨迹
                } catch (Exception x) {

                }


            }
        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                //本地LBSTraceClient客户端获取的位置
                try {
                    if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                            location.getLongitude())) {
                        return;
                    }
//                    LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
//                    if (null == currentLatLng) {
//                        return;
//                    }
                    CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                    CurrentLocation.latitude = location.getLatitude();
                    CurrentLocation.longitude = location.getLongitude();

//                    if (null != mapUtil) {
//                        mapUtil.updateMapLocation(currentLatLng, mCurrentDirection);//显示当前位置
//                        mapUtil.animateMapStatus(currentLatLng);//缩放
//                    }

                } catch (Exception x) {

                }


            }

        };

        traceListener = new OnTraceListener() {

            @Override
            public void onBindServiceCallback(int errorNo, String message) {
//                viewUtil.showToast(MainActivity.this,
//                        String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    MyApp.getInstance().isTraceStarted = true;
                    SharedPreferences.Editor editor = MyApp.getInstance().trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
//                    setTraceBtnStyle();
                    registerGPSReceiver();
                }
                // TODO: 2017/9/5
                MyApp.getInstance().mClient.setInterval(Constants.DEFAULT_GATHER_INTERVAL, packInterval);
                MyApp.getInstance().mClient.startGather(traceListener);//开启采集
//                viewUtil.showToast(MainActivity.this,
//                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    MyApp.getInstance().isTraceStarted = false;
                    MyApp.getInstance().isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = MyApp.getInstance().trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
//                    setTraceBtnStyle();
//                    setGatherBtnStyle();
//                    unregisterPowerReceiver();
                    firstLocate = true;
                }
//                viewUtil.showToast(MainActivity.this,
//                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    MyApp.getInstance().isGatherStarted = true;
                    SharedPreferences.Editor editor = MyApp.getInstance().trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
//                    setGatherBtnStyle();
//
                    stopRealTimeLoc();
                    startRealTimeLoc(packInterval);
                }
//                viewUtil.showToast(MainActivity.this,
//                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    MyApp.getInstance().isGatherStarted = false;
                    SharedPreferences.Editor editor = MyApp.getInstance().trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
//                    setGatherBtnStyle();

                    firstLocate = true;
                    stopRealTimeLoc();
                    startRealTimeLoc(Constants.LOC_INTERVAL);

//                    if (trackPoints.size() >= 1) {
//                        try {
//                            mapUtil.drawEndPoint(trackPoints.get(trackPoints.size() - 1));
//                        } catch (Exception e) {
//
//                        }
//
//                    }

                }
//                viewUtil.showToast(MainActivity.this,
//                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {

            }

            @Override
            public void onInitBOSCallback(int i, String s) {

            }
        };

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
        }
    }

    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();
    private RealTimeLocRunnable realTimeLocRunnable = null;

    /**
     * 实时定位任务
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            MyApp.getInstance().getCurrentLocation(entityListener, trackListener);
            realTimeHandler.postDelayed(this, interval * 1000);
        }
    }

    public void startRealTimeLoc(int interval) {
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        MyApp.getInstance().mClient.stopRealTimeLoc();
    }

    /**
     * 注册广播（电源锁、GPS状态）
     */
    protected void registerGPSReceiver() {
        if (MyApp.getInstance().isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        MyApp.getInstance().registerReceiver(trackReceiver, filter);
        MyApp.getInstance().isRegisterReceiver = true;

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // 读取手机状态
        addPermission(permissions, Manifest.permission.READ_PHONE_STATE);
        addPermission(permissions, Manifest.permission.CAMERA);
        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    private void addBaimingdan() {
        ((MyApp) getApplication()).setStartActivityName(getClass().getName());
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);

        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = MyApp.getInstance().getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (mSensorManager!=null) {
//        ((MyApp) getApplication()).setStartActivityName(getClass().getName());
//            mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
//
//            // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                String packageName = MyApp.getInstance().getPackageName();
//                boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
//                if (!isIgnoring) {
//                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                    intent.setData(Uri.parse("package:" + packageName));
//                    try {
//                        startActivity(intent);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        }
    }
}