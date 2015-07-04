package com.kaede.rainymood;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.kaede.common.util.SharePreferenceUtil;
import com.kaede.rainymood.home.MainService;

/**
 * @author kaede
 * github https://github.com/kidhaibara/rainymood.git
 */
public class BaseApplication extends Application {

	private static final String TAG = "BaseApplication";

	@Override
	public void onCreate() {
		Log.d(TAG,"App onCreate" );
		startService(new Intent(this, MainService.class));
		SharePreferenceUtil.init(this);
		RainyConfig.init(this);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		stopService(new Intent(this, MainService.class));
		Log.d(TAG, "App onTerminate");
		super.onTerminate();
	}

}
