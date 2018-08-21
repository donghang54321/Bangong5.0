package com.rentian.newoa.common.net;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

//{"errorCode":999,"info":{}}
public class Base {

	@Expose
	public int errorCode = 999;
	
	@Expose
	public JsonElement info;

	@Override
	public String toString() {
		return "Base [errorCode=" + errorCode + ", info=" + info + "]";
	}

}
