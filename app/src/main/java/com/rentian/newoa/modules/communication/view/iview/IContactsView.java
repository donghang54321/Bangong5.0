package com.rentian.newoa.modules.communication.view.iview;

import com.rentian.newoa.modules.communication.bean.EmployeeInfoByNet;

import java.util.List;

public interface IContactsView
{
	public void showContacts(List<EmployeeInfoByNet> book);
	
	public void showToast(int flag);
}
