package com.kaede.rainymood;

import android.app.Application;
import android.util.Log;

/**
 * @author kaede
 * github https://github.com/kidhaibara/rainymood.git
 */
public class BaseApplication extends Application {

	private static final String TAG = "BaseApplication";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d(TAG,"App onCreate" );
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "App onTerminate");
		super.onTerminate();
	}

}
