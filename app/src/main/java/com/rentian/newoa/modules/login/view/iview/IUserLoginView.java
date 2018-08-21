package com.rentian.newoa.modules.login.view.iview;


import com.rentian.newoa.modules.login.bean.User;

public interface IUserLoginView {

	public User getUserInfo();
	public void setUserInfo(User user);

	public void showLoginExecuting();
	public void showLoginPost();
	public void showLoginResult(int flag);//flag用来标识结果类型：是跳转到另一个页面，还是Toast...
}
