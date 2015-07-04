package com.kaede.common.ad;

import me.kaede.rainymood.av.RainymoodLayout;
import android.app.Activity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AdManagerMogo {
	static final String APPId = "ccbb6ab4a27c49988946b20b33447c2e";
	 static final Boolean TEST_MODE =false;
	public static void addBanner(Activity activity,ViewGroup container){
		me.kaede.rainymood.av.RainymoodLayout adviewLayout = new RainymoodLayout(activity, APPId,me.kaede.rainymood.util.RainymoodSize.RainymoodAutomaticScreen);
		adviewLayout.isOtherSizes = true;
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		container.addView(adviewLayout, layoutParams);
	}
	
	public static void clear(){
		me.kaede.rainymood.av.RainymoodLayout.clear();
	}
}
