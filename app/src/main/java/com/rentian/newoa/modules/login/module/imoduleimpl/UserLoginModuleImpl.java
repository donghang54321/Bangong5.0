package com.rentian.newoa.modules.login.module.imoduleimpl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import com.rentian.newoa.MyApp;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.net.OkHttp3Utils;
import com.rentian.newoa.modules.login.bean.User;
import com.rentian.newoa.modules.login.module.imodule.IUserLoginModule;

import java.util.HashMap;
import java.util.Map;

public class UserLoginModuleImpl implements IUserLoginModule {

    @Override
    public User getUserInfo(Context context) {


        User user = new User();

        SharedPreferences sp = context.getSharedPreferences("user_info",
                Activity.MODE_PRIVATE);


        user.setUsername(sp.getString("username", ""));
        user.setPassword(sp.getString("password", ""));
        user.setIsLogin(sp.getString("loginStaus", "0"));

        return user;
    }

    @Override
    public int isUserInputLegal(User user) {
        if (user.getUsername().length() == 0
                || user.getPassword().length() == 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public String login(User user) {

        String sendContent = "username=" + user.getUsername().trim()
                + "&password=" + user.getPassword().trim();
        String resJson = HttpURLConnHelper.requestByPOST(Const.RTOA.URL_LOGIN,
                sendContent);

        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("username",user.getUsername().trim());
        hashMap.put("password",user.getPassword().trim());
        new OkHttp3Utils().post(Const.RTOA.URL_LOGIN, hashMap,
                new OkHttp3Utils.HttpCallback() {
                    @Override
                    public void onSuccess(String data) {

                    }
                });
        return resJson;

    }

    @Override
    public void saveUser(Context context, User user) {

        SharedPreferences sp = context.getSharedPreferences("user_info",
                Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("username", user.getUsername());
        editor.putString("password", user.getPassword());
        editor.putString("loginStaus", "1");
        editor.putString("uid", MyApp.getInstance().getMyUid());
        editor.apply();
    }

}
