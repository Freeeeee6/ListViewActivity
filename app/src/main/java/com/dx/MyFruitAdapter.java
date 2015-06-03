package com.dx;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFruitAdapter extends ArrayAdapter<Fruit> {
	private int mresource;
	private ViewHolder mvh;
	
	public MyFruitAdapter(Context context, int resource, List<Fruit> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		mresource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v;
		Fruit f = getItem(position);
		
		if( convertView == null ){
			v = LayoutInflater.from(getContext()).inflate(mresource, null);
			mvh = new ViewHolder();
			mvh.tv = (TextView)v.findViewById(R.id.tv);
			mvh.iv = (ImageView)v.findViewById(R.id.iv);
			v.setTag(mvh);
						
		}
		else{
			v = convertView;
						
			mvh = (ViewHolder) convertView.getTag();			
		}			
				
		mvh.tv.setText(f.getName());
		mvh.iv.setImageResource(f.getId());
//		Log.e("dx", f.getId()+",name:"+f.getName());
		
		
		
		return v;
	}
	
	class ViewHolder{
		TextView tv;
		ImageView iv;
	}

}

class Fruit{
	private String mname;
	private int mid;
	
	public Fruit(String name,int id) {
		mname = name;
		mid = id;
	}
	
	public String getName() {
		return mname;
	}
	
	public int getId() {
		return mid;
	}
}