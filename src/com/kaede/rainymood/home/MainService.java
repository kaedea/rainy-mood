package com.kaede.rainymood.home;



import thirdparty.de.greenrobot.event.EventBus;

import com.kaede.rainymood.R;
import com.kaede.rainymood.R.raw;
import com.kaede.rainymood.entity.EventPlayer;
import com.kaede.rainymood.entity.EventTimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
 
public class MainService extends Service {
    public static final String TAG = "MainService";
	private MediaPlayer mp;
	static Boolean  isPlaying = false;
	static Boolean  startTimer = false;
	private CountDownTimer countDownTimer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mp=MediaPlayer.create(this,R.raw.rainy);
        Log.e(TAG, "main service oncreate");
        EventBus.getDefault().register(this);
        mp.setLooping(true);
        
    }
    @Override  
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    } 
    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        EventBus.getDefault().unregister(this);
    }
    
    public void onEventMainThread(EventPlayer e)
    {
    	Log.d(TAG, "EventPlayer:"+e.type);
    	switch (e.type) {
		case EventPlayer.PLAY:		
			mp.start();
			break;
		case EventPlayer.PAUSE:		
			mp.pause();
			break;
		case EventPlayer.NOTICICATION:		
			break;
		default:
			break;
		}
    }
    
    public void onEventMainThread(EventTimer e)
    {
    	Log.d(TAG, "EventTimer:"+e.type);
    	switch (e.type) {
		case EventTimer.START_TIMER:
			startTimer(e.millis);
			break;
		case EventTimer.CANCEL_TIMER:
			if (countDownTimer != null) {
				countDownTimer.cancel();
			}
			break;
		default:
			break;
		}
    }
    
    public void startTimer(long millis)
    {
    	countDownTimer = new CountDownTimer(millis, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				//tv_timer.setText(millsToMS(millisUntilFinished));
				EventBus.getDefault().post(new EventTimer(EventTimer.ON_TICK, millisUntilFinished));
			}

			@Override
			public void onFinish() {
				/*findViewById(R.id.main_layout_timer).setVisibility(
						View.INVISIBLE);
				stopPlay();*/
				EventBus.getDefault().post(new EventTimer(EventTimer.ON_FINISH));
			}
		};
		countDownTimer.start();
    }
    
 
}
