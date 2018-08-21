package com.rentian.newoa.modules.communication.model.imodelimpl;

import com.google.gson.reflect.TypeToken;
import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.communication.bean.BaseList;
import com.rentian.newoa.modules.communication.bean.Msg;
import com.rentian.newoa.modules.communication.model.imodel.IMessageModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageModelImpl implements IMessageModel
{

	@Override
	public List<Map<String, Object>> getMessageList(String uid)
	{

		String sendContent = "uid=" + uid;

		String resJson = HttpURLConnHelper.requestByPOST(
				Const.RTOA.URL_MESSAGE_LIST, sendContent);

		BaseList base = null;

		List<Msg> list = null;

		Type type = new TypeToken<List<Msg>>()
		{
		}.getType();

		try
		{
			base = CommonUtil.gson.fromJson(resJson, BaseList.class);
			list = CommonUtil.gson.fromJson(base.list, type);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = null;

		if (null != list)
		{

			for (int i = 0; i < list.size(); i++)
			{
				map = new HashMap<String, Object>();

				map.put("publisher", list.get(i).name);
				// map.put("publisher", "OAС����");
				map.put("msg_id", list.get(i).id);
				map.put("publish_date", list.get(i).time);
				map.put("msg_content", list.get(i).content);

				datas.add(map);
			}
		}

		return datas;
	}

}
