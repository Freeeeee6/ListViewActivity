package com.dx;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

interface HttpCallbackListener {
	void onFinish(String response);
	void onError(Exception e);
}

public final class dxUtility {
	
	public dxUtility() {
		// TODO Auto-generated constructor stub
	}

	public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// 回调onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}
	
	public static int random(int max) {
		return (int) (Math.random() * max);
	}

	/** 
	 * dp转成px 
	 * @param dipValue 
	 * @return 
	 */  
	public static int dip2px(Context context,float dipValue) {  
		float scale = context.getResources().getDisplayMetrics().density;  
	    return (int) (dipValue * scale + 0.5f);  
	}  
	  
	/** 
	 * px转成dp 
	 * @param pxValue 
	 * @return 
	 */  
	public static int px2dip(Context context,float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (pxValue / scale + 0.5f);  
	}  
	  
}
