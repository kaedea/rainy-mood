package com.kaede.rainymood.home;



import com.kaede.rainymood.EventPlayer;
import com.kaede.rainymood.R;
import com.kaede.rainymood.R.raw;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
 
public class MainService extends Service {
    public static final String TAG = "MainService";
	private MediaPlayer mp;
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
    	switch (e.type) {
		case EventPlayer.PLAY:		
			mp.start();
			break;
		case EventPlayer.PAUSE:		
			mp.pause();
			break;
		default:
			break;
		}
    }
 
}
