package com.kaede.rainymood.home;

import java.util.concurrent.TimeUnit;

import thirdparty.com.astuetz.PagerSlidingTabStrip;
import thirdparty.com.nineoldandroids.animation.ObjectAnimator;
import thirdparty.de.greenrobot.event.EventBus;
import thirdparty.de.keyboardsurfer.android.widget.crouton.Crouton;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.kaede.common.ad.AdManagerQQ;
import com.kaede.common.util.DeviceUtil;
import com.kaede.common.util.NavigationUtils;
import com.kaede.common.util.ResolutionUtil;
import com.kaede.common.util.SharePreferenceUtil;
import com.kaede.common.util.ToastUtil;
import com.kaede.rainymood.R;
import com.kaede.rainymood.RainyConfig;
import com.kaede.rainymood.entity.EventPlayer;
import com.kaede.rainymood.entity.EventTimer;
import com.kaede.rainymood.util.NavigationUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends ActionBarActivity {

	public static final int NOTIDICATION_ID = 233;
	private static final String TAG = "MainActivity";
	private ImageView main_iv_play;
	private TextView tv_timer;
	private SeekBar seekBar;
	private ViewPager viewPager;
	Handler _handler = new Handler(Looper.getMainLooper());
	private PagerSlidingTabStrip tabs;

	//----------------- Life Period -----------------//
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this);
		getSupportActionBar().hide();
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		//getSupportActionBar().
		intitView();
		setListener();
		//startService(new Intent(MainActivity.this, MainService.class));

		if (MainService.isPlaying) {
			main_iv_play.setImageResource(R.drawable.main_pause);
		} else {
			main_iv_play.setImageResource(R.drawable.main_play);
			postAnimator(findViewById(R.id.layout_main_play),false,1000);
		}

		if (MainService.startTimer) {
			findViewById(R.id.layout_main_timer_container).setVisibility(View.VISIBLE);
		}else{
			postAnimator(findViewById(R.id.layout_main_timer),false,1300);
		}
		postAnimator(findViewById(R.id.layout_main_download),false,1600);
		
		int current = SharePreferenceUtil.getInt("CURRENT_TAB", 0);
		viewPager.setCurrentItem(current);
        tabs.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				Log.d(TAG, "onPageSelected index="+arg0);
				EventPlayer event = new EventPlayer(EventPlayer.SWITCH);
				event.position=arg0;
				EventBus.getDefault().post(event);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
        
        UmengUpdateAgent.setUpdateAutoPopup(true);
        UmengUpdateAgent.update(this);//UMENG更新
        MobclickAgent.updateOnlineConfig(this);//UMENG在线参数
        
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (MainService.isPlaying) {
			// EventBus.getDefault().post(new EventPlayer(2));
			showNotification();
		}
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_main_give_me_5:
			NavigationUtils.navigateToMarket(this, DeviceUtil.getpackageName(this));
			break;
			
		case R.id.menu_main_more_apps:
			AdManagerQQ.loadIntertistial(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		Crouton.cancelAllCroutons();
		EventBus.getDefault().unregister(this);
		SharePreferenceUtil.putInt("CURRENT_TAB", viewPager.getCurrentItem());
		super.onDestroy();
	}

	//----------------- Method -----------------//
	public void intitView() {
		viewPager = (ViewPager) this.findViewById(R.id.viewPager_main);

		MainFragmentStateAdapter mainFragmentStateAdapter = new MainFragmentStateAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mainFragmentStateAdapter);
		//viewPager.setOffscreenPageLimit(0);
		

		tabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs_main);
		tabs.setViewPager(viewPager);
		tabs.setTextColorResource(R.color.common_white);
		tabs.setTextSize(ResolutionUtil.spToPx(this, 14f));
		
		main_iv_play = (ImageView) this.findViewById(R.id.main_iv_play);
		tv_timer = (TextView) MainActivity.this.findViewById(R.id.tv_main_timer);
		
		/*ImageView iv_main_progress=(ImageView) findViewById(R.id.iv_main_progress);
		ClipDrawable clipDrawable = (ClipDrawable) iv_main_progress.getDrawable();
		clipDrawable.setLevel(9000);*/
		//ViewHelper.setTranslationY(, 50);
	}

	public void setListener() {
		findViewById(R.id.btn_main_timer).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showSetTimerDialog();
				MobclickAgent.onEvent(MainActivity.this,"click_main_timer");
			}
		});
		
		findViewById(R.id.btn_main_play).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play();
				MobclickAgent.onEvent(MainActivity.this,"click_main_play");
			}
		});
		
		findViewById(R.id.btn_main_download).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtil.toast(MainActivity.this, getResources().getString(R.string.main_tip_download_hd));
				MobclickAgent.onEvent(MainActivity.this,"click_main_download");
			}
		});
		
		findViewById(R.id.layout_main_timer).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showSetTimerDialog();
				postAnimator(findViewById(R.id.layout_main_timer),true,0);
				MobclickAgent.onEvent(MainActivity.this,"click_main_timer");
			}
		});
		findViewById(R.id.layout_main_play).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play();
				MobclickAgent.onEvent(MainActivity.this,"click_main_play");
			}
		});
		findViewById(R.id.layout_main_download).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Crouton.makeText(MainActivity.this, "在线雨声资源正在搭建中，请注意更新", Style.INFO).show(true);
				ToastUtil.toast(MainActivity.this, getResources().getString(R.string.main_tip_download_hd));
				postAnimator(findViewById(R.id.layout_main_download),true,0);
				postAnimator(findViewById(R.id.layout_main_download),false,3000);
				MobclickAgent.onEvent(MainActivity.this,"click_main_download");
			}
		});
		findViewById(R.id.layout_main_timer_container).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCancelTimerDialog();
			}
		});
		
		findViewById(R.id.layout_main_toolbar).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NavigationUtil.toAboutAcitivity(MainActivity.this);
			}
		});
		
	}
	
	private void postAnimator(final View v, Boolean inOrOut, int delayMillis) {
		if (inOrOut) {
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					animateIn(v);
				}
			}, delayMillis);
		} else {
			getHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					animateOut(v);
				}
			}, delayMillis);
		}
	}
	
	private void animateOut(View v){
		Log.d(TAG, "animateOut");
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "translationY",0f,ResolutionUtil.dipToPx(this, 24f));
		objectAnimator.setDuration(2000);
		objectAnimator.setInterpolator(new BounceInterpolator());
		objectAnimator.start();
	}
	
	private void animateIn(View v){
		Log.d(TAG, "animateIn");
		ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "translationY",ResolutionUtil.dipToPx(this, 24f),0f);
		objectAnimator.setDuration(2000);
		objectAnimator.setInterpolator(new BounceInterpolator());
		objectAnimator.start();
	}

	private void play() {
		if (MainService.isPlaying) {
			stopPlay();
		} else {
			startPlay();
		}
	}

	private void startPlay() {
		postAnimator(findViewById(R.id.layout_main_play),true,0);
		EventBus.getDefault().post(new EventPlayer(EventPlayer.PLAY));
		main_iv_play.setImageResource(R.drawable.main_pause);
		MainService.isPlaying = true;
	}

	private void stopPlay() {
		postAnimator(findViewById(R.id.layout_main_play),false,0);
		if (MainService.startTimer) {
			postAnimator(findViewById(R.id.layout_main_timer),false,0);
		}
		EventBus.getDefault().post(new EventPlayer(EventPlayer.PAUSE));
		main_iv_play.setImageResource(R.drawable.main_play);
		cancelTimer();
		MainService.isPlaying = false;
		
		clearNotification();
	}

	private void showSetTimerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		View view_dialog = factory.inflate(R.layout.dialog_main_pick_timer, null);
		seekBar = (SeekBar) view_dialog.findViewById(R.id.seekbar_dialog_pick_timer);
		final TextView tv_progress = (TextView) view_dialog.findViewById(R.id.tv_dialog_pick_timer_progress);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tv_progress.setText(String.valueOf(progress + 1));

			}
		});
		builder.setView(view_dialog);
		final Dialog dialog = builder.create();
		view_dialog.findViewById(R.id.layout_dialog_pick_timer_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view_dialog.findViewById(R.id.layout_dialog_pick_timer_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startTimer(((seekBar.getProgress() + 1) * 60 + 1) * 1000);
				ToastUtil.toast(MainActivity.this, getResources().getString(R.string.main_tip_cancel_countdown));
				dialog.dismiss();
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				if (!MainService.startTimer){
					postAnimator(findViewById(R.id.layout_main_timer),false,0);
				}
			}
		});
		dialog.show();
	}

	private void startTimer(long millis) {
		cancelTimer();
		startPlay();
		findViewById(R.id.layout_main_timer_container).setVisibility(View.VISIBLE);
		EventBus.getDefault().post(new EventTimer(EventTimer.START_TIMER, millis));
		MainService.startTimer = true;
	}

	private void cancelTimer() {
		EventBus.getDefault().post(new EventTimer(EventTimer.CANCEL_TIMER));
		if (findViewById(R.id.layout_main_timer_container).getVisibility() == View.VISIBLE) {
			findViewById(R.id.layout_main_timer_container).setVisibility(View.INVISIBLE);
		}
		MainService.startTimer = false;
	}

	private void showCancelTimerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		View view_dialog = factory.inflate(R.layout.dialog_main_cancel_timer, null);
		builder.setView(view_dialog);
		final Dialog dialog = builder.create();
		view_dialog.findViewById(R.id.layout_dialog_cancel_timer_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view_dialog.findViewById(R.id.layout_dialog_cancel_timer_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancelTimer();
				dialog.dismiss();
			}
		});
		
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				if (!MainService.startTimer){
					postAnimator(findViewById(R.id.layout_main_timer),false,0);
				}
			}
		});

		dialog.show();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public String millsToMS(long millis) {
		long m = TimeUnit.MILLISECONDS.toMinutes(millis);
		String minute = String.valueOf(m);
		if (m < 10)
			minute = "0" + minute;

		long s = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
		String second = String.valueOf(s);
		if (s < 10)
			second = "0" + second;

		return minute + ":" + second;
	}

	private Handler getHandler(){
		return _handler;
	}

	
	public void showNotification() {
		/*
		 * 自定义View NotificationManager notificationManager =
		 * (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		 * Notification notification = new Notification(R.drawable.ic_launcher,
		 * "Rainy Mood", System .currentTimeMillis());
		 * 
		 * RemoteViews view = new RemoteViews(getPackageName(),
		 * R.layout.notification_main); notification.contentView = view;
		 * 
		 * PendingIntent contentIntent =
		 * PendingIntent.getActivity(this,R.string.app_name, new
		 * Intent(),PendingIntent.FLAG_UPDATE_CURRENT);
		 * notification.contentIntent = contentIntent;
		 * notificationManager.notify(0, notification);
		 */

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher_rain, getResources().getString(R.string.notification_description_short), System.currentTimeMillis());
		n.flags = Notification.FLAG_NO_CLEAR;//FLAG_AUTO_CANCEL为可清除
		Intent i = new Intent(this, MainActivity.class);
		// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		// i.setAction(Intent.ACTION_MAIN);
		// i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
		i.putExtra("isplaying", true);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(this, getResources().getString(R.string.notification_title), getResources().getString(R.string.notification_description), contentIntent);
		nm.notify(NOTIDICATION_ID, n);
	}
	
	public void clearNotification(){
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIDICATION_ID);
	}

	public void onEventMainThread(EventTimer e) {
		Log.d(TAG, "EventTimer:" + e.type);
		switch (e.type) {
		case EventTimer.ON_TICK:
			tv_timer.setText(millsToMS(e.millis));
			break;
		case EventTimer.ON_FINISH:
			postAnimator(findViewById(R.id.layout_main_timer),false,0);
			MainService.startTimer = false;
			stopPlay();
			break;
		}
	}
	
	
	//----------------- Inner Class -----------------//
	public static final class MainFragmentStateAdapter extends FragmentStatePagerAdapter {

		public MainFragmentStateAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//super.destroyItem(container, position, object);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return RainyConfig.getPagerTitle(position);
		}

		@Override
		public int getCount() {
			return RainyConfig.getPagerLength();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return MainFragment.getInstance(arg0);
		}
	}

}
