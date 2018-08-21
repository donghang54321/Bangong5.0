package com.rentian.newoa.modules.communication.view.iview;

import java.util.List;
import java.util.Map;

public interface IMessageView
{
	public void showMessageList(List<Map<String, Object>> list);
	
	public void showToast(int flag);
}
