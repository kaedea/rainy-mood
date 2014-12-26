package com.kaede.rainymood.home;



import com.kaede.rainymood.EventPlayer;
import com.kaede.rainymood.R;
import com.kaede.rainymood.R.raw;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
 
public class MainService extends Service {
    public static final String TAG = "MainService";
	private MediaPlayer mp;
	static Boolean  isPlaying = false;
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
		case EventPlayer.NOTICICATION:		
			showNotification();
			break;
		default:
			break;
		}
    }
    
    public void showNotification()
    {
    	/*NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    	Notification notification = new Notification(R.drawable.ic_launcher, "Rainy Mood", System
                 .currentTimeMillis());

         RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification_main);
         notification.contentView = view;

         PendingIntent contentIntent = PendingIntent.getActivity(this,R.string.app_name, new Intent(),PendingIntent.FLAG_UPDATE_CURRENT);
         notification.contentIntent = contentIntent;
         notificationManager.notify(0, notification);*/
    	
    	NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);               
    	Notification n = new Notification(R.drawable.ic_launcher, "正在播放雨声", System.currentTimeMillis());             
    	n.flags = Notification.FLAG_AUTO_CANCEL;                
    	Intent i = new Intent(this, MainActivity.class);
//    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
    	i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
    	i.putExtra("isplaying", true);
    	//PendingIntent
    	PendingIntent contentIntent = PendingIntent.getActivity(
    			this, 
    	        R.string.app_name, 
    	        i, 
    	        PendingIntent.FLAG_UPDATE_CURRENT);
    	                 
    	n.setLatestEventInfo(
    			this,
    	        "Rainy Mood", 
    	        "自然的雨声，安静的心情！", 
    	        contentIntent);
    	nm.notify(R.string.app_name, n);
    }
 
}
