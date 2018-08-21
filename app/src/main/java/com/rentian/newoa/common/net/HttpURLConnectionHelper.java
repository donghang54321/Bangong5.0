package com.rentian.newoa.common.net;

import com.rentian.newoa.common.utils.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionHelper
{

	private static final int TIMEOUT = 60000;

	// private BaseInfo base = new BaseInfo();

	public static <T> void requestByPOST(String url, String sendContent,
			OnResponsedListener<T> listener)
	{

		Class clazz = listener.getClass();

		Type[] genTypes = clazz.getGenericInterfaces();

		Type[] params = ((ParameterizedType) genTypes[0])
				.getActualTypeArguments();

		Class genClazz = (Class) params[0];

		T base = null;

		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try
		{
			CommonUtil.log("http send content", url + "---" + sendContent);
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);
			connection.connect();
			os = connection.getOutputStream();
			os.write(sendContent.getBytes());
			os.flush();
			switch (connection.getResponseCode())
			{
			case HttpURLConnection.HTTP_OK:

				is = connection.getInputStream();
				// baseJSON :{"errorCode":0,"info":[]}
				String baseJSON = CommonUtil.convertStreamToString(is);

				System.out.println(baseJSON);

				baseJSON = baseJSON.trim();

				base = (T) CommonUtil.gson.fromJson(baseJSON, genClazz);

				System.out.println("base:" + base);

				Base bs = (Base) base;

				if (bs.errorCode == 0 || bs.errorCode == 1)
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
			closeRes(os, is, connection, listener, base);
		}

	}

	public static <T> void requestByGET(String url,
			OnResponsedListener<T> listener)
	{

		Class clazz = listener.getClass();

		Type[] genTypes = clazz.getGenericInterfaces();

		Type[] params = ((ParameterizedType) genTypes[0])
				.getActualTypeArguments();

		Class genClazz = (Class) params[0];

		T base = null;

		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try
		{
			CommonUtil.log("http send content", url + "---");
			connection = (HttpURLConnection) new URL(url).openConnection();

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

				System.out.println(baseJSON);

				base = (T) CommonUtil.gson.fromJson(baseJSON, genClazz);

				Base bs = (Base) base;

				if (bs.errorCode == 1)
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
			closeRes(os, is, connection, listener, base);
		}
	}

	private static <T> void closeRes(OutputStream os, InputStream is,
			HttpURLConnection conn, OnResponsedListener<T> listener, T base)
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
