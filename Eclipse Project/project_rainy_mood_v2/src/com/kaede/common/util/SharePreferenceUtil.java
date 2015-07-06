package com.kaede.common.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
	public static SharedPreferences sp;
	public static void init(Context c){
		 sp = c.getSharedPreferences("DEFAULT_PRE", android.content.Context.MODE_PRIVATE);
	}
	
	public static int getInt(String name,int defaultValue){
		return sp.getInt(name,defaultValue);
	}
	
	public static void putInt(String name,int value){
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(name, value); 
		editor.commit();
	}
	
	public static Boolean getBoolean(String name,Boolean defaultValue){
		return sp.getBoolean(name,defaultValue);
	}
	
	public static void putBoolean(String name,Boolean value){
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(name, value); 
		editor.commit();
	}
}
