package com.kaede.rainymood.home;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.kaede.common.ad.AdManagerQQ;
import com.kaede.common.util.StringUtil;
import com.kaede.rainymood.R;
import com.kaede.rainymood.RainyConfig;
import com.umeng.analytics.MobclickAgent;

public class MainFragment extends Fragment {

	private static final String TAG = "MainFragment";
	private static final String EXTRA_INDEX = "EXTRA_INDEX";
	private int pos;
	Boolean isHasShowInsert = false;
	Random random;
	private int rateShowInsert;
	float alphaBanner = 100f;
	
	public static MainFragment getInstance(int posintion){
		MainFragment fragment = new MainFragment();
		final Bundle args = new Bundle(1);
        args.putInt(EXTRA_INDEX, posintion);
        fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.pos = getArguments().getInt(EXTRA_INDEX,0);
		random = new Random();
		String s = MobclickAgent.getConfigParams(getActivity(), "num_rate_show_insert");
		rateShowInsert = StringUtil.safeParseInt(s);
		if (rateShowInsert<=0) {
			rateShowInsert=5;
		}
		s = MobclickAgent.getConfigParams(getActivity(), "num_rate_banner_alpha");
		float f = StringUtil.safeParseInt(s);
		if (f>=0&&f<=100) {
			 alphaBanner = f/100f;
		}
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ImageView iv_bg = (ImageView) rootView
				.findViewById(R.id.iv_main_fragment_bg);
		iv_bg.setImageResource(RainyConfig.getBgResource(pos));
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		ad_addbanner();
		MobclickAgent.onPageStart("MainFragment-"+pos);
	}
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MainFragment-"+pos); 
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (!isVisibleToUser) {
			handler.removeCallbacks(TASK_ADD_AD);
		}else
		{
			ad_showInsert();
		}
	}

	private void ad_addbanner() {
		String s = MobclickAgent.getConfigParams(getActivity(), "is_show_banner_main");
		Log.e(TAG, "is_show_banner_main="+s );
		int isShowBanner = StringUtil.safeParseInt(s);
		if (isShowBanner>=0) {
			//handler.postDelayed(TASK_ADD_AD, 2000);
			getView().findViewById(R.id.adsMogoView).setVisibility(View.VISIBLE);
			ViewCompat.setAlpha(getView().findViewById(R.id.adsMogoView), alphaBanner);
		}else{
			getView().findViewById(R.id.adsMogoView).setVisibility(View.GONE);
		}
	}
	
	private void ad_showInsert() {
		if (isHasShowInsert) {
			return;
		}
		int i = random.nextInt(rateShowInsert);
		if (i!=0) {
			return;
		}
		String s = MobclickAgent.getConfigParams(getActivity(), "is_show_insert_main");
		Log.e(TAG, "is_show_insert_main="+s );
		int isShowInsert = StringUtil.safeParseInt(s);
		if (isShowInsert>=0) {
			AdManagerQQ.loadIntertistial(getActivity());
			isHasShowInsert=true;
			MobclickAgent.onEvent(MainFragment.this.getActivity(),"show_insert_main");
		}
	}
	
	Handler handler = new Handler();
	Runnable TASK_ADD_AD = new Runnable() {
		
		@Override
		public void run() {
			try {
				RelativeLayout container = (RelativeLayout) getView().findViewById(R.id.layout_main_fragment_banner);
				AdManagerQQ.addBanner(getActivity(), container);
				MobclickAgent.onEvent(MainFragment.this.getActivity(),"show_banner_main");
			} catch (Throwable e) {
				Log.e(TAG,"Add AD Error! ");
				e.printStackTrace();
			}
		}
	};
	

	
}