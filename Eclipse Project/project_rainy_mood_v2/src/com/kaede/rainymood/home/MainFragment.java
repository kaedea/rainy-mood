package com.kaede.rainymood.home;

import java.util.Random;

import thirdparty.com.nineoldandroids.view.ViewHelper;

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

import com.kaede.common.ad.AdManagerMogo;
import com.kaede.common.ad.AdManagerQQ;
import com.kaede.common.util.StringUtil;
import com.kaede.rainymood.R;
import com.kaede.rainymood.RainyConfig;
import com.kaede.rainymood.util.UMengUtilImpl;
import com.umeng.analytics.MobclickAgent;

public class MainFragment extends Fragment {

	private static final String TAG = "MainFragment";
	private static final String EXTRA_INDEX = "EXTRA_INDEX";
	private int pos;
	Random random;
	
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
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ImageView iv_bg = (ImageView) rootView
				.findViewById(R.id.iv_main_fragment_bg);
		iv_bg.setImageResource(RainyConfig.getBgResource(pos));
		ad_addbanner();
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
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
			//handler.removeCallbacks(TASK_ADD_AD);
		}else
		{
			ad_showInsert();
		}
	}
	
	@Override
	public void onDestroy() {
		AdManagerMogo.clear();
		super.onDestroy();
	}

	private void ad_addbanner() {
		if (UMengUtilImpl.instance().isShowBanner) {
			handler.postDelayed(TASK_ADD_AD, 2000);
			//getView().findViewById(R.id.adsMogoView).setVisibility(View.VISIBLE);
			//ViewCompat.setAlpha(getView().findViewById(R.id.adsMogoView), alphaBanner);
			//AdManagerMogo.addBanner(this.getActivity(), (RelativeLayout)getView().findViewById(R.id.layout_main_fragment_banner));
		}else{
			//getView().findViewById(R.id.adsMogoView).setVisibility(View.GONE);
		}
	}
	
	private void ad_showInsert() {
		if (UMengUtilImpl.instance().isHasShowInsert||!UMengUtilImpl.instance().isShowInsert) {
			return;
		}
		if (random == null) {
			random = new Random();
		}
		int i = random.nextInt(UMengUtilImpl.instance().rateShowInsert);
		if (i!=0) {
			return;
		}
		
		AdManagerQQ.loadIntertistial(getActivity());
		UMengUtilImpl.instance().isHasShowInsert=true;
		MobclickAgent.onEvent(MainFragment.this.getActivity(),"show_insert_main");
	}
	
	Handler handler = new Handler();
	Runnable TASK_ADD_AD = new Runnable() {
		
		@Override
		public void run() {
			try {
				RelativeLayout container = (RelativeLayout) getView().findViewById(R.id.layout_main_fragment_banner);
				AdManagerMogo.addBanner(getActivity(), container);
				if (UMengUtilImpl.instance().alphaBanner>0&&UMengUtilImpl.instance().alphaBanner<1.0f) {
					ViewHelper.setAlpha(container, UMengUtilImpl.instance().alphaBanner);
				}
				MobclickAgent.onEvent(MainFragment.this.getActivity(),"show_banner_main");
			} catch (Throwable e) {
				Log.e(TAG,"Add AD Error! ");
				e.printStackTrace();
			}
		}
	};
	
	

	
}