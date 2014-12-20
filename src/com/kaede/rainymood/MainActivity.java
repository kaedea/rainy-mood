package com.kaede.rainymood;

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
import android.os.Build;

public class MainActivity extends ActionBarActivity {


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //EventBus.getDefault().register(this);

        ViewPager viewPager = (ViewPager) this.findViewById(R.id.main_viewPager);
        MainFragmentStateAdapter mainFragmentStateAdapter = new MainFragmentStateAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mainFragmentStateAdapter);
		
		startService(new Intent(MainActivity.this,MainService.class));
		EventBus.getDefault().post(new Event());
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final class MainFragmentStateAdapter extends FragmentStatePagerAdapter {
    	public MainFragmentStateAdapter(FragmentManager fm) {
    		super(fm);
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
    	//EventBus.getDefault().unregister(this);
    }
}
