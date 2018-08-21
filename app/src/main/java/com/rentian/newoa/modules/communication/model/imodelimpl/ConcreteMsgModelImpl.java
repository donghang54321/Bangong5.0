package com.rentian.newoa.modules.communication.model.imodelimpl;

import com.rentian.newoa.common.constant.Const;
import com.rentian.newoa.common.net.HttpURLConnHelper;
import com.rentian.newoa.common.utils.CommonUtil;
import com.rentian.newoa.modules.communication.bean.BaseConcreteMsg;
import com.rentian.newoa.modules.communication.bean.ConcreteMsg;
import com.rentian.newoa.modules.communication.model.imodel.IConcreteMsgModel;

public class ConcreteMsgModelImpl implements IConcreteMsgModel
{

	@Override
	public ConcreteMsg getConcreteMsg(long msgId)
	{
		String sendContent = "id="+msgId;
		
		String resJson = HttpURLConnHelper.requestByPOST(Const.RTOA.URL_CONCRETE_MESSAGE,sendContent);
		
		BaseConcreteMsg base = null;
		
		try
		{
			base = CommonUtil.gson.fromJson(resJson,BaseConcreteMsg.class);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if(null != base)
			return base.message;
		
		return null;
	}

}
