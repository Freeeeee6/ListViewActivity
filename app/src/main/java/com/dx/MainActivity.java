package com.dx;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static NotificationManager manager;
	public static Notification notification;
	private List<Fruit> l = new ArrayList<Fruit>();	
	private MyReceive mmr = new MyReceive();
	//private Context = MainActivity.this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initList();
		ListView lv = (ListView)findViewById(R.id.list);
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

		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification(R.drawable.abc_ic_menu_cut_mtrl_alpha , "This is ticker text",
				System.currentTimeMillis());
		notification.setLatestEventInfo(MainActivity.this, "This is content title", "This is content text", null);

		new Thread(){
			public void run(){
				try {
					int i =1;

					while(true) {
						manager.notify(i, notification);
						Log.e("Test Thread", "22");

						Thread.sleep(1000);

						manager.cancel(i++);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
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

