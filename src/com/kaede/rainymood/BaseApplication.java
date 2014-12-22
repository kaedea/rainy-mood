package com.kaede.rainymood;

import android.app.Application;
import android.util.Log;

public class BaseApplication extends Application {

	private static final String GITHUB_URL = "https://github.com/kidhaibara/rainymood.git";
	private static final String TAG = "kaede";

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
