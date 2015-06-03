package com.dx;

import android.content.Context;

public final class dxUtility {
	
	public dxUtility() {
		// TODO Auto-generated constructor stub
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
