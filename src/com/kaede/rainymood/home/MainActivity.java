package com.kaede.rainymood.home;

import com.astuetz.PagerSlidingTabStrip;
import com.kaede.rainymood.EventPlayer;
import com.kaede.rainymood.R;
import com.kaede.rainymood.R.id;
import com.kaede.rainymood.R.layout;
import com.kaede.rainymood.R.menu;

import de.greenrobot.event.EventBus;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	
	private Boolean isPlaying =false;
	private ImageView main_iv_play;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		intitView();
		setListener();
		// ��������
		startService(new Intent(MainActivity.this, MainService.class));
		startTimer();
	}

	public void intitView() {
		ViewPager viewPager = (ViewPager) this
				.findViewById(R.id.main_viewPager);
		MainFragmentStateAdapter mainFragmentStateAdapter = new MainFragmentStateAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(mainFragmentStateAdapter);

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) this
				.findViewById(R.id.tabs);
		tabs.setViewPager(viewPager);
		
		main_iv_play = (ImageView) this.findViewById(R.id.main_iv_play);
	}

	public void setListener() {
		findViewById(R.id.main_btn_timer).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

					}
				});
		findViewById(R.id.main_btn_play).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				play();
			}
		});
		findViewById(R.id.main_btn_download).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Crouton.showText(MainActivity.this, "Coming Soon", Style.INFO);
			}
		});
	}
	
	public void play()
	{
		if (isPlaying) {
			EventBus.getDefault().post(new EventPlayer(1));
			main_iv_play.setImageResource(R.drawable.main_play);
			isPlaying=false;
		}
		else{
			EventBus.getDefault().post(new EventPlayer(0));
			main_iv_play.setImageResource(R.drawable.main_stop);
			isPlaying = true;
		}
	}
	
	public void startTimer()
	{
		Chronometer timer = (Chronometer) this.findViewById(R.id.main_chronometer);
		timer.setBase(SystemClock.elapsedRealtime());  
		timer.start();
		timer.setOnChronometerTickListener(new OnChronometerTickListener() {
			
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.give_me_5) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}
	public static final class MainFragmentStateAdapter extends
			FragmentStatePagerAdapter {

		private final String[] TITLES = { "Categories", "Home", "Top Paid",
				"Top Free", "Top Grossing", "Top New Paid", "Top New Free",
				"Trending" };

		public MainFragmentStateAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub

			return TITLES[position];
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return new MainFragment(arg0);
		}
	}


}
