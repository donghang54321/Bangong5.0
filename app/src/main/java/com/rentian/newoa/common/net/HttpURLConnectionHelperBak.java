package com.rentian.newoa.common.net;

import com.rentian.newoa.common.utils.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionHelperBak
{

	private static final int TIMEOUT = 60000;

	// private BaseInfo base = new BaseInfo();

	public static void requestByPOST(String url, String sendContent,
			OnResponsedListener<Base> listener)
	{
		
		Base base = new Base();
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try
		{
		
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);
			connection.connect();
			os = connection.getOutputStream();
			os.write(sendContent.getBytes("UTF-8"));
			os.flush();
			switch (connection.getResponseCode())
			{
			case HttpURLConnection.HTTP_OK:

				is = connection.getInputStream();
				// baseJSON :{"errorCode":0,"info":[]}
				String baseJSON = CommonUtil.convertStreamToString(is);
				CommonUtil.log("http return", baseJSON);
				base = CommonUtil.gson.fromJson(baseJSON, Base.class);
				if (base.errorCode == 0)
				{
					listener.onEntityLoadComplete(base);
				} else
				{
					listener.onError(base);
				}
				break;
			default:

				if (listener != null)
				{
					listener.onError(base);
				}
				break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();

			if (listener != null)
			{
				listener.onError(base);
			}
		} finally
		{
			closeRes(os,is,connection,listener,base);
		}
			
	}

	public static void requestByGET(String url,
			OnResponsedListener<Base> listener)
	{
		Base base = new Base();
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try
		{
			CommonUtil.log("http send content", url + "---");
			connection = (HttpURLConnection) new URL(url)
					.openConnection();

			connection.setUseCaches(false);

			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);
			connection.connect();

			switch (connection.getResponseCode())
			{
			case HttpURLConnection.HTTP_OK:

				is = connection.getInputStream();
				
				System.out.println("hehe");
				
				// baseJSON :{"errorCode":0,"info":[]}
				String baseJSON = CommonUtil.convertStreamToString(is);
				
				baseJSON = baseJSON.trim();
			
				CommonUtil.log("http return", baseJSON);
				base = CommonUtil.gson.fromJson(baseJSON, Base.class);
				if (base.errorCode == 1)
				{
					listener.onEntityLoadComplete(base);
				} else
				{
					listener.onError(base);
				}
				break;
			default:

				if (listener != null)
				{
					listener.onError(base);
				}
				break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();

			if (listener != null)
			{
				listener.onError(base);
			}
		} finally
		{
			closeRes(os,is,connection,listener,base);
		}
	}

	private static void closeRes(OutputStream os,InputStream is,HttpURLConnection conn,OnResponsedListener<Base> listener,Base base)
	{
		if (os != null)
		{
			try
			{
				os.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				if (listener != null)
				{
					listener.onError(base);
				}
			}
			os = null;
		}
		if (is != null)
		{
			try
			{
				is.close();
			} catch (IOException e)
			{
				e.printStackTrace();
				if (listener != null)
				{
					listener.onError(base);
				}
			}
			is = null;
		}
		if (conn != null)
		{
			conn.disconnect();
			conn = null;
		}
	}


}
