package com.kaede.rainymood.util;

import android.content.Context;
import android.util.Log;

import com.kaede.common.ad.AdManagerQQ;
import com.kaede.common.util.StringUtil;
import com.kaede.rainymood.home.MainFragment;
import com.umeng.analytics.MobclickAgent;

public class UMengUtilImpl implements IUMengUtil {
	
	public float alphaBanner = 0.7f;
	public int rateShowInsert = 5;
	public Boolean isShowBanner = false;
	public Boolean isShowInsert = false;
	public Boolean isHasShowInsert = false;
	public Boolean isShowInsetExit = false;
	static UMengUtilImpl instance;
	
	public static UMengUtilImpl instance(){
		if (instance==null) {
			instance = new UMengUtilImpl();
		}
		return instance;
	}
	
	private UMengUtilImpl(){
		
	}
	
	@Override
	public void initOnlineConfig(Context context){
		String s = MobclickAgent.getConfigParams(context, "num_rate_show_insert");
		if (StringUtil.safeParseInt(s)>0) {
			rateShowInsert=StringUtil.safeParseInt(s);
		}
		
		s = MobclickAgent.getConfigParams(context, "num_rate_banner_alpha");
		float f = StringUtil.safeParseInt(s);
		if (f>=0&&f<=100) {
			 alphaBanner = f/100f;
		}
		
		s = MobclickAgent.getConfigParams(context, "is_show_banner_main");
		if (StringUtil.safeParseInt(s)>=0) {
			isShowBanner = true;
		}
		
		s = MobclickAgent.getConfigParams(context, "is_show_insert_main");
		if (StringUtil.safeParseInt(s)>=0) {
			isShowInsert=true;
		}
		
		s = MobclickAgent.getConfigParams(context, "is_show_insert_main_exit");
		if (StringUtil.safeParseInt(s)>=0) {
			isShowInsetExit=true;
		}
	}

}
