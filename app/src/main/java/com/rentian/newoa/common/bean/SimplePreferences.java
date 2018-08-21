package com.rentian.newoa.common.bean;

import com.baoyz.treasure.Clear;
import com.baoyz.treasure.Default;
import com.baoyz.treasure.Preferences;
import com.baoyz.treasure.Remove;

import java.util.Set;

/**
 * Created by rentianjituan on 2018/8/7.
 */
@Preferences
public interface SimplePreferences {
    @Default("无")
    String getUsername();

    void setUsername(String username);

    @Default("")
    String getPassword();

    void setPassword(String password);

    @Default("false")
    boolean getIsLogin();

    void setIsLogin(boolean isLogin);

    @Remove
    void removePassword();

    @Remove
    void removeUsername();

    @Remove
    void removeIsLogin();

    // default is 1 hour
    @Default("1000 * 60 * 60")
    long getTimeout();

    @Default({"hello", "world", "!"})
    Set<String> getStringSet();

    @Clear
    void clear();
}
