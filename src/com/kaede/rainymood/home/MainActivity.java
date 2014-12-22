package com.kaede.rainymood.home;

import com.astuetz.PagerSlidingTabStrip;
import com.kaede.rainymood.EventPlayer;
import com.kaede.rainymood.R;
import com.kaede.rainymood.R.id;
import com.kaede.rainymood.R.layout;
import com.kaede.rainymood.R.menu;

import de.greenrobot.event.EventBus;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ViewPager viewPager = (ViewPager) this.findViewById(R.id.main_viewPager);
        MainFragmentStateAdapter mainFragmentStateAdapter = new MainFragmentStateAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mainFragmentStateAdapter);
		
		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) this.findViewById(R.id.tabs);
		tabs.setViewPager(viewPager);
		
		//Æô¶¯·þÎñ
		startService(new Intent(MainActivity.this,MainService.class));
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
        if (id == R.id.action_settings) {
            EventBus.getDefault().post(new EventPlayer(0));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final class MainFragmentStateAdapter extends FragmentStatePagerAdapter {
    	
    	private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
				"Top New Free", "Trending" };
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
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    
}
