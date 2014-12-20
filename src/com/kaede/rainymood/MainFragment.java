package com.kaede.rainymood;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        /*TextView tv = (TextView) rootView.findViewById(R.id.pager_txt);
        tv.setText(String.valueOf(pos));*/
        return rootView;
    }
}