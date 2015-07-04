package com.kaede.rainymood.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import com.kaede.rainymood.R;
import com.kaede.rainymood.home.AboutActivity;

public class NavigationUtil {

	/**
	 * 统一的滑动打开activity startActivity
	 * 
	 * @param context
	 * @param intent
	 */
	private static void slideStartActivity(Context context, Intent intent) {
		ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.slide_in_from_right, R.anim.slide_out_from_left);
		ActivityCompat.startActivity((Activity) context, intent, options.toBundle());
	}
	
	public static void toAboutAcitivity(Context context){
		Intent intent = new Intent(context,AboutActivity.class);
		slideStartActivity(context,intent);
	}
}
