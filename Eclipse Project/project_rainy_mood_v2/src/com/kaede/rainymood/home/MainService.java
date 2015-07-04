package com.kaede.rainymood.home;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import thirdparty.de.greenrobot.event.EventBus;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import com.kaede.common.util.SharePreferenceUtil;
import com.kaede.rainymood.RainyConfig;
import com.kaede.rainymood.entity.EventPlayer;
import com.kaede.rainymood.entity.EventTimer;
 
public class MainService extends Service {
    public static final String TAG = "MainService";
    private static final int CORE_POOL_SIZE =1;//1个核心工作线程
    private static final int MAXIMUM_POOL_SIZE = 10;//最多128个工作线程
    private static final int KEEP_ALIVE = 3;//空闲线程的超时时间为1秒
    private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(10);//等待队列
    private static final RejectedExecutionHandler REJECTED_EXECUTION_HANDLER = new ThreadPoolExecutor.DiscardOldestPolicy();// 线程池对拒绝任务的处理策略
    private static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,  MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sWorkQueue,REJECTED_EXECUTION_HANDLER);//线程池是静态变量，所有的异步任务都会放到这个线程池的工作线程内执行。 
	private MediaPlayer mp;
	static Boolean  isPlaying = false;
	static Boolean  startTimer = false;
	private CountDownTimer countDownTimer;
	private PlayAsyncTask playAsyncTask;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "main service oncreate");
        EventBus.getDefault().register(this);
        int postion = SharePreferenceUtil.getInt("CURRENT_TAB", 0);
        Log.e(TAG, "postion="+postion);
        switchBgm(postion);
    }
    @Override  
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    } 
    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
        if (playAsyncTask!=null) {
        	playAsyncTask.cancel(false);
        	playAsyncTask=null;
		}
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
		case EventPlayer.SWITCH:
			switchBgm(e.position);
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
    
    public void switchBgm(int position){
    	Log.e(TAG, "switch to bgm: "+position);
    	if (playAsyncTask!=null) {
    		playAsyncTask.cancel(false);
		}
    	playAsyncTask = new PlayAsyncTask();
    	if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
    		playAsyncTask.executeOnExecutor(threadPoolExecutor,RainyConfig.getBgmResource(position));
    	}else
    	{
    		playAsyncTask.execute(RainyConfig.getBgmResource(position));
    	}
    }
    
    public class PlayTask implements Runnable{

    	int resid;
    	public PlayTask(int resid){
    		this.resid=resid;
    	}
		@Override
		public void run() {
			 mp=MediaPlayer.create(MainService.this,resid);
	         mp.setLooping(true);
	         
		}
    }
    
    public class PlayAsyncTask extends AsyncTask<Integer, Void, Void>{

		@Override
		protected Void doInBackground(Integer... arg0) {
			if (mp!=null&&mp.isPlaying()) {
				mp.stop();
			}
			mp=MediaPlayer.create(MainService.this,arg0[0]);
	        mp.setLooping(true);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (isPlaying) {
	        	 mp.start();
			}
			super.onPostExecute(result);
		}
    	
    }
    
 
}
