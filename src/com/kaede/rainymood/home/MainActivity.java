package com.kaede.rainymood.home;

import java.util.concurrent.TimeUnit;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.kaede.advertise.AdManagerQQ;
import com.kaede.rainymood.EventPlayer;
import com.kaede.rainymood.EventTimer;
import com.kaede.rainymood.R;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";
	private ImageView main_iv_play;
	private TextView tv_timer;
	private SeekBar seekBar;
	private ViewPager viewPager;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this);
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		intitView();
		setListener();
		startService(new Intent(MainActivity.this, MainService.class));

		if (MainService.isPlaying) {
			main_iv_play.setImageResource(R.drawable.main_pause);
		} else {
			main_iv_play.setImageResource(R.drawable.main_play);
		}

		if (MainService.startTimer) {
			findViewById(R.id.main_layout_timer).setVisibility(View.VISIBLE);
		}
	}

	public void intitView() {
		viewPager = (ViewPager) this.findViewById(R.id.main_viewPager);

		MainFragmentStateAdapter mainFragmentStateAdapter = new MainFragmentStateAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mainFragmentStateAdapter);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
		tabs.setViewPager(viewPager);
		tabs.setIndicatorColorResource(R.color.common_blue);

		main_iv_play = (ImageView) this.findViewById(R.id.main_iv_play);

		tv_timer = (TextView) MainActivity.this.findViewById(R.id.main_tv_timer);

	}

	public void setListener() {
		findViewById(R.id.main_btn_timer).setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				showSetTimerDialog();
			}
		});
		findViewById(R.id.main_btn_play).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play();
			}
		});
		findViewById(R.id.main_btn_download).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Crouton.makeText(MainActivity.this, "Coming Soon", Style.INFO).show(true);
			}
		});
		findViewById(R.id.main_layout_timer).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCancelTimerDialog();
			}
		});
	}

	private void play() {
		if (MainService.isPlaying) {
			stopPlay();
		} else {
			startPlay();
		}
	}

	private void startPlay() {
		EventBus.getDefault().post(new EventPlayer(EventPlayer.PLAY));
		main_iv_play.setImageResource(R.drawable.main_pause);
		MainService.isPlaying = true;
	}

	private void stopPlay() {
		EventBus.getDefault().post(new EventPlayer(EventPlayer.PAUSE));
		main_iv_play.setImageResource(R.drawable.main_play);
		cancelTimer();
		MainService.isPlaying = false;
	}

	private void showSetTimerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		View view_dialog = factory.inflate(R.layout.dialog_pick_timer, null);
		seekBar = (SeekBar) view_dialog.findViewById(R.id.main_seekbar);
		final TextView tv_progress = (TextView) view_dialog.findViewById(R.id.timerdialog_tv_progress);
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
		view_dialog.findViewById(R.id.main_dialog_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view_dialog.findViewById(R.id.main_dialog_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startTimer(((seekBar.getProgress() + 1) * 60 + 1) * 1000);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void startTimer(long millis) {
		cancelTimer();
		startPlay();
		findViewById(R.id.main_layout_timer).setVisibility(View.VISIBLE);
		EventBus.getDefault().post(new EventTimer(EventTimer.START_TIMER, millis));
		MainService.startTimer = true;
	}

	private void cancelTimer() {
		EventBus.getDefault().post(new EventTimer(EventTimer.CANCEL_TIMER));
		if (findViewById(R.id.main_layout_timer).getVisibility() == View.VISIBLE) {
			findViewById(R.id.main_layout_timer).setVisibility(View.INVISIBLE);
		}
		MainService.startTimer = false;
	}

	private void showCancelTimerDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		LayoutInflater factory = LayoutInflater.from(MainActivity.this);
		View view_dialog = factory.inflate(R.layout.dialog_cancel_timer, null);
		builder.setView(view_dialog);
		final Dialog dialog = builder.create();
		view_dialog.findViewById(R.id.dialogCancelTimer_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view_dialog.findViewById(R.id.dialogCancelTimer_confirm).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancelTimer();
				dialog.dismiss();
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



	@Override
	protected void onResume() {
		SharedPreferences sp = this.getSharedPreferences("DEFAULT_PRE", MODE_PRIVATE);
		int current = sp.getInt("CURRENT_TAB", 0);
		viewPager.setCurrentItem(current);

		super.onResume();
	}

	@Override
	protected void onPause() {
		if (MainService.isPlaying) {
			// EventBus.getDefault().post(new EventPlayer(2));
			showNotification();
		}
		super.onPause();
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
		SharedPreferences sp = this.getSharedPreferences("DEFAULT_PRE", MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt("CURRENT_TAB", viewPager.getCurrentItem()); // value to
																	// store
		editor.commit();
		super.onDestroy();
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
		Notification n = new Notification(R.drawable.icon_relaxrain, "正在播放雨声", System.currentTimeMillis());
		n.flags = Notification.FLAG_AUTO_CANCEL;
		Intent i = new Intent(this, MainActivity.class);
		// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		// i.setAction(Intent.ACTION_MAIN);
		// i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
		i.putExtra("isplaying", true);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(this, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(this, "Rainy Mood", "自然的雨声，安静的心情！", contentIntent);
		nm.notify(R.string.app_name, n);

	}

	public void onEventMainThread(EventTimer e) {
		Log.d(TAG, "EventTimer:" + e.type);
		switch (e.type) {
		case EventTimer.ON_TICK:
			tv_timer.setText(millsToMS(e.millis));
			break;
		case EventTimer.ON_FINISH:
			MainService.startTimer = false;
			stopPlay();
			break;
		}
	}

	public static final class MainFragmentStateAdapter extends FragmentStatePagerAdapter {

		private final String[] TITLES = { "Rainy", "Mood", "Always", "Calm", "Everything" };

		public MainFragmentStateAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return new MainFragment(arg0);
		}
	}

}
