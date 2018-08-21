package com.rentian.newoa.modules.communication.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.rentian.newoa.MyApp;
import com.rentian.newoa.common.utils.NetworkUtil;
import com.rentian.newoa.modules.communication.model.imodel.IMessageModel;
import com.rentian.newoa.modules.communication.model.imodelimpl.MessageModelImpl;
import com.rentian.newoa.modules.communication.view.iview.IMessageView;

import java.util.List;
import java.util.Map;

public class MessagePresenter
{
	private IMessageView mView;
	private IMessageModel mModule;
	private Activity activity;
	MyApp myApp;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			if (msg.what == 0)
			{
				
				if(null != msg.obj)
				{
					List<Map<String, Object>> list =  (List<Map<String, Object>>) msg.obj;
					
					if(0 == list.size())
					{
						mView.showToast(1);
					}
					else
					{
						mView.showMessageList(list);
					}
					
				}
				else
				{
					mView.showToast(2);
				}
				
			}
		};
	};

	public MessagePresenter(Activity activity, IMessageView mView)
	{
		this.activity = activity;
		this.mView = mView;
		this.mModule = new MessageModelImpl();
		myApp = (MyApp) activity.getApplication();
	}

	public void getMessageList()
	{
		if (NetworkUtil.isNetworkConnected(activity))
		{
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					Message msg = mHandler.obtainMessage(0);

					msg.obj = mModule.getMessageList(MyApp.getInstance().getMyUid());

					msg.sendToTarget();

				}
			}).start();
		} else
		{
			mView.showToast(0);

		}
	}

}
