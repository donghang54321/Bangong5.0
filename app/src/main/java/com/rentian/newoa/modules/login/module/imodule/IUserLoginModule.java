package com.rentian.newoa.modules.login.module.imodule;

import android.content.Context;

import com.rentian.newoa.modules.login.bean.User;


public interface IUserLoginModule {
	
	
	public int isUserInputLegal(User user);

	public User getUserInfo(Context context);
	
	public String login(User user);
	
	public void saveUser(Context context, User user);
}
