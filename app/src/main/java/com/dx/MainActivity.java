package com.dx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.R.anim;
import android.R.drawable;
import android.R.string;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends Activity {

	public static final int SHOW_RESPONSE1 = 0;
	public static final int SHOW_RESPONSE2 = 1;
	private NotificationManager manager;
	private Notification notification;
	private List<Fruit> l = new ArrayList<Fruit>();	
	private MyReceive mmr = new MyReceive();
	//private Context = MainActivity.this;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SHOW_RESPONSE1:
					String response = (String) msg.obj;
// 在这里进行UI操作，将结果显示到界面上
					//responseText.setText(response);
					Log.e("HttpURLConnection",response);
					break;
				case SHOW_RESPONSE2:
//					String response = (String) msg.obj;
// 在这里进行UI操作，将结果显示到界面上
					//responseText.setText(response);
					Log.e("HttpClient", (String) msg.obj);
					break;
			}
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initList();
		ListView lv = (ListView)findViewById(R.id.list);
		WebView wv = (WebView) findViewById(R.id.wv);
		MyFruitAdapter ma = new MyFruitAdapter(this, R.layout.list_layout, l);
		lv.setAdapter(ma);
		lv.setOnItemClickListener(new OnItemClickListener() {			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Fruit f = l.get(arg2);				
				Toast.makeText(MainActivity.this, f.getName(), Toast.LENGTH_SHORT).show();
			}
		});
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
//		ni.isAvailable()

		wv.getSettings().setJavaScriptEnabled(true);
		wv.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				//return super.shouldOverrideUrlLoading(view, url);
				view.loadUrl(url);
				return true;
			}
		});

		wv.loadUrl("https://www.baidu.com");

		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.abc_ic_menu_cut_mtrl_alpha , "This is ticker text",
				System.currentTimeMillis());
		notification.setLatestEventInfo(MainActivity.this, "This is content title", "This is content text", null);
//        notification.ledARGB = Color.YELLOW;
//        notification.ledOffMS = 1000;
//        notification.ledOnMS = 1000;
//        notification.flags = Notification.FLAG_SHOW_LIGHTS;
		//notification.defaults = Notification.DEFAULT_VIBRATE;
		//notification.flags = Notification.FLAG_INSISTENT;


		new Thread(){
			public void run(){
				try {
					int i =1;

					//while(true)
					{
						manager.notify(i, notification);

						Thread.sleep(1000);

						manager.cancel(i);
						i++;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();


		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL("http://www.baidu.com");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
// 下面对获取到的输入流进行读取
					BufferedReader reader = new BufferedReader(new
							InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					Message message = new Message();
					message.what = SHOW_RESPONSE1;
// 将服务器返回的结果存放到Message中
					message.obj = response.toString();
//					handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();


		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
//					HttpGet httpGet = new HttpGet("http://www.baidu.com");
					HttpGet httpGet = new HttpGet("http://127.0.0.1/get_data.xml");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
// 请求和响应都成功了
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity,
								"utf-8");
						
						parseXMLWithPull(response);
						Message message = new Message();
						message.what = SHOW_RESPONSE2;
// 将服务器返回的结果存放到Message中
						message.obj = response.toString();
						handler.sendMessage(message);

						Toast.makeText(MainActivity.this,"请求成功",Toast.LENGTH_SHORT).show();
					}
					else
						Toast.makeText(MainActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();


	}

	private void parseXMLWithPull(String xmlData) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xmlPullParser = factory.newPullParser();
				xmlPullParser.setInput(new StringReader(xmlData));
				int eventType = xmlPullParser.getEventType();
				String id = "";
				String name = "";
				String version = "";
				while (eventType != XmlPullParser.END_DOCUMENT) {
					String nodeName = xmlPullParser.getName();
					switch (eventType) {
// 开始解析某个结点
						case XmlPullParser.START_TAG: {
							if ("id".equals(nodeName)) {
								id = xmlPullParser.nextText();
							} else if ("name".equals(nodeName)) {
								name = xmlPullParser.nextText();
							} else if ("version".equals(nodeName)) {
								version = xmlPullParser.nextText();
							}
							break;
						}

// 完成解析某个结点
						case XmlPullParser.END_TAG: {
							if ("app".equals(nodeName)) {
								Log.d("MainActivity", "id is " + id);
								Log.d("MainActivity", "name is " + name);
								Log.d("MainActivity", "version is " + version);
							}
							break;
						}
						default:
							break;
					}
					eventType = xmlPullParser.next();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	class MyReceive extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if( intent.getAction() == "android.net.conn.CONNECTIVITY_CHANGE" )
				Toast.makeText(context, "网络变动", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private void initFilter() {
		IntentFilter i = new IntentFilter();
		i.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(mmr, i);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		unregisterReceiver(mmr);
	}
	
	public void initList() {
//		String[] s = {"Item1","Item2","Item3","Item5","Item6","Item7","Item8","Item9","Item10","Item11",
//				"Item12","Item13"};
		String [] s = new String[10000];
		int i = 0;
				
		for (String ss : s) {			
			s[i] = new String("hash code:"+dxUtility.random(50)+",num:"+i++);			
		}
		
		i=0;
		for (String ss : s) {
			if(i%5==0)
				l.add(new Fruit(ss, R.drawable.abc_btn_check_material));
			else
				l.add(new Fruit(ss, R.drawable.ic_launcher));
			
			i++;
		}
	}
}

