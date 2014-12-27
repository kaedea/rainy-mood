package com.kaede.rainymood.home;

import com.kaede.rainymood.R;
import com.kaede.rainymood.R.layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
@SuppressLint("ValidFragment")
public class MainFragment extends Fragment {

	private int pos;

	public MainFragment(int posintion) {
		pos = posintion;
	}
	
	public MainFragment() {
		pos = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		ImageView iv_bg = (ImageView) rootView
				.findViewById(R.id.fragmentMain_iv_bg);
		iv_bg.setImageResource(getBgResource(pos));
		return rootView;
	}

	private int getBgResource(int position) {
		int resid = 0;
		switch (position) {
		case 0:
			resid = R.drawable.main_bg_00;
			break;
		case 1:
			resid = R.drawable.main_bg_01;
			break;
		case 2:
			resid = R.drawable.main_bg_02;
			break;
		case 3:
			resid = R.drawable.main_bg_03;
			break;
		case 4:
			resid = R.drawable.main_bg_04;
			break;

		default:
			resid = R.drawable.main_bg_00;
			break;
		}
		return resid;
	}
}