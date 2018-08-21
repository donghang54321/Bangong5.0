package com.rentian.newoa.modules.communication.presenter;

import android.os.Handler;
import android.os.Message;

import com.rentian.newoa.modules.communication.bean.ConcreteMsg;
import com.rentian.newoa.modules.communication.model.imodel.IConcreteMsgModel;
import com.rentian.newoa.modules.communication.model.imodelimpl.ConcreteMsgModelImpl;
import com.rentian.newoa.modules.communication.view.iview.IConcreteMsgView;

public class ConcreteMsgPresenter
{
	private IConcreteMsgView mView;
	
	private IConcreteMsgModel mModule;
	
	private long msgId;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(0 == msg.what)
			{
				if(null != msg.obj)
				{
					mView.showConcreteMsg((ConcreteMsg) msg.obj);
				}
			}
		};
	};
	
	public void setMsgId(long msgId)
	{
		this.msgId = msgId;
	}
	
	public ConcreteMsgPresenter(IConcreteMsgView mView)
	{
		this.mView = mView;
		this.mModule = new ConcreteMsgModelImpl();
		
		
			
	}
	
	public void getConcreteMsg()
	{
		new Thread(new Runnable(){
			@Override
			public void run()
			{
				Message msg = mHandler.obtainMessage(0);
				
				msg.obj = mModule.getConcreteMsg(msgId);
				
				msg.sendToTarget();
			}
		}).start();
		
	}
}
