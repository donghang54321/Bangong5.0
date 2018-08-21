package com.rentian.newoa.modules.communication.model.imodel;

import java.util.List;
import java.util.Map;

public interface IMessageModel
{
	public List<Map<String,Object>> getMessageList(String uid);
}
