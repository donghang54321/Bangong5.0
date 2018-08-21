package com.rentian.newoa.common.utils;

/**
 * Created by rentianjituan on 2017/3/2.
 */

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.rentian.newoa.MyApp;


/**
 * Created by rentianjituan on 2016/11/23.
 */

public class PackageInfoUtil {

    PackageManager manager;

    PackageInfo info = null;

    private  int versionCode;

    public PackageInfoUtil() {
        creatManager();
    }

    private  void creatManager() {
        manager = MyApp.getInstance().getPackageManager();

        try {

            info = manager.getPackageInfo( MyApp.getInstance().getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {

// TODO Auto-generated catch block

            e.printStackTrace();

        }
        versionCode=info.versionCode;
    }
    public  int getVersionCode(){

        return versionCode;
    }

    public  int getVersionCode2(){
        Log.e("versionCode",info.versionName.substring(4));
        return Integer.parseInt(info.versionName.substring(4));
    }

}