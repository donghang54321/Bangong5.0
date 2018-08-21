package com.rentian.newoa.common.net;

import com.rentian.newoa.common.utils.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpURLConnHelper
{
	private static final int TIMEOUT = 20000;

	// private BaseInfo base = new BaseInfo();
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest.replaceAll("\u0001\n", "");
	}
	public static String replaceOtherUniqueCharcter(String str){
		String dest = "";
		if (str != null) {
			dest=str.replaceAll("%","%25");
//			dest=str.replaceAll("'\'+","%2B");
			dest=str.replaceAll("'\'?", "%3F");
			dest=str.replaceAll("'\'/","%2F");
			dest=str.replaceAll("#","%23");
		}
		return dest;
	}
	public static String requestByPOST(String url, String sendContent)
	{
		sendContent=replaceBlank(sendContent);
		sendContent=replaceOtherUniqueCharcter(sendContent);
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;

		String baseJSON = null;

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
				
				System.out.println("after input");

				baseJSON = CommonUtil.convertStreamToString(is);

				baseJSON = baseJSON.trim();

				System.out.println(baseJSON);

				break;
			default:

				break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();

		} finally
		{
			closeRes(os, is, connection);

		}

		return baseJSON;

	}

	public static String requestByGET(String url)
	{

		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;

		String baseJSON = null;

		try
		{

			connection = (HttpURLConnection) new URL(url).openConnection();

			connection.setUseCaches(false);

			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);
			
			connection.connect();

			switch (connection.getResponseCode())
			{
			case HttpURLConnection.HTTP_OK:

				is = connection.getInputStream();

				baseJSON = CommonUtil.convertStreamToString(is);

				baseJSON = baseJSON.trim();

				break;
				
			case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
				break;
			default:

				break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			
			return null;

		} finally
		{
			closeRes(os, is, connection);
		}

		return baseJSON;
	}

	private static void closeRes(OutputStream os, InputStream is,
			HttpURLConnection conn)
	{
		if (os != null)
		{
			try
			{
				os.close();
			} catch (IOException e)
			{
				e.printStackTrace();

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
