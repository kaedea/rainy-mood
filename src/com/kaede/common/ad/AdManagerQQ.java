package com.kaede.common.ad;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.qq.e.ads.AdListener;
import com.qq.e.ads.AdRequest;
import com.qq.e.ads.AdSize;
import com.qq.e.ads.AdView;
import com.qq.e.ads.InterstitialAd;
import com.qq.e.ads.InterstitialAdListener;

/**
 * @author kaede 
 * @version v0.1 2014-12-28 02:39:52
 */
public class AdManagerQQ {
	 static final Boolean TEST_MODE =false;
	 static final String APPId = "1150076575";
	 static final String BannerId = "4040404069629757";
	 static final String InterteristalId = "8010309069022778";
	
	public static void addBanner(Activity activity,ViewGroup container){
		/*
		 * 创建Banner广告view 　　 * “appid”指在 http://e.qq.com/dev/ 能看到的app唯一字符串 　　 *
		 * “广告位 id” 指在 http://e.qq.com/dev/ 生成的数字串， 　　 * 并非 appid 或者 appkey
		 */
		AdView adv = new AdView(activity, AdSize.BANNER, APPId, BannerId);
		container.addView(adv);
		/* 广告请求数据，可以设置广告轮播时间，默认为30s */
		AdRequest adr = new AdRequest();
		/*
		 * 这个接口的作用是设置广告的测试模式，该模式下点击不扣费 未发布前请设置testad为true，
		 * 上线的版本请确保设置为false或者去掉这行调用
		 */
		
		adr.setTestAd(TEST_MODE);
		/* 设置广告刷新时间，为30~120之间的数字，单位为s */
		adr.setRefresh(31);
		adr.setShowCloseBtn(true);
		/*
		 * 设置空广告和首次收到广告数据回调 调用fetchAd方法后会发起广告请求，广告轮播时不会产生回调
		 */
		adv.setAdListener(new AdListener() {
			@Override
			public void onNoAd() {
				Log.i("no ad cb:", "no");
			}

			@Override
			public void onAdReceiv() {
				Log.i("ad recv cb:", "revc");
			}

			@Override
			public void onAdClicked() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAdExposure() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onBannerClosed() {
				// TODO Auto-generated method stub

			}
		});
		/* 发起广告请求，收到广告数据后会展示数据 */
		adv.fetchAd(adr);
	}
	
	public static void loadIntertistial(final Activity activity)
	{
		final InterstitialAd iad =
	            new InterstitialAd(activity, APPId, InterteristalId);
	        iad.setAdListener(new InterstitialAdListener() {
	          @Override
	          public void onBack() {
	            // iad.loadAd();
	            Log.i("admsg:","Intertistial AD Closed");
	          }

	          @Override
	          public void onFail() {
	            Log.i("admsg:","Intertistial AD Load Fail");
	          }

	          @Override
	          public void onAdReceive() {
	            Log.i("admsg:", "Intertistial AD  ReadyToShow");
	            //加载完成后显示
	            iad.show(activity);
	          }

	          @Override
	          public void onClicked() {
	            //插屏广告发生点击时回调，由于点击去重等因素不能保证回调数量与联盟最终统计数量一致
	            Log.i("admsg:","Intertistial AD Clicked");
	          }

	          @Override
	          public void onExposure() {
	            //插屏广告曝光时的回调
	            Log.i("admsg:","Intertistial AD Exposured");
	          }
	        });
	        iad.loadAd();
	}
}
