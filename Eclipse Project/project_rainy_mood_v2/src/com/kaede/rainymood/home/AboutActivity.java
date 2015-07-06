package com.kaede.rainymood.home;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.kaede.common.util.DeviceUtil;
import com.kaede.common.util.NavigationUtils;
import com.kaede.common.util.SharePreferenceUtil;
import com.kaede.common.util.ToastUtil;
import com.kaede.rainymood.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class AboutActivity extends ActionBarActivity {

	TextView tvVersionCode;
	com.umeng.fb.fragment.FeedbackFragment sad;
	private UMSocialService mUMsocialController;
	private CheckBox checkCloseAd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_about);
		initView();
		setListener();
		init();
		MobclickAgent.updateOnlineConfig(this);// UMENG在线参数
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initView() {
		tvVersionCode = (TextView) this.findViewById(R.id.tv_about_versioncode);
		checkCloseAd = ((CheckBox)findViewById(R.id.check_about_ad));
	}

	private void setListener() {
		checkCloseAd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharePreferenceUtil.putBoolean("pre_is_close_ad", isChecked);
				
			}
		});
		findViewById(R.id.iv_about_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AboutActivity.this.finish();
			}
		});

		findViewById(R.id.iv_about_share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(AboutActivity.this,"click_about_share");
				mUMsocialController.openShare(AboutActivity.this, false);
				//shareMsg(AboutActivity.this,"a","b","c","");
			}
		});

		findViewById(R.id.layout_about_feedback).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(AboutActivity.this,"click_about_feedback");
				FeedbackAgent agent = new FeedbackAgent(AboutActivity.this);
				agent.startFeedbackActivity();
			}
		});
		findViewById(R.id.layout_about_upgrade).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MobclickAgent.onEvent(AboutActivity.this,"click_about_upgrade");
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
						switch (updateStatus) {
						case UpdateStatus.Yes: // has update
							UmengUpdateAgent.showUpdateDialog(AboutActivity.this, updateInfo);
							break;
						case UpdateStatus.No: // has no update
							ToastUtil.toast(AboutActivity.this, getResources().getString(R.string.about_tip_noupdate));
							break;
						case UpdateStatus.NoneWifi: // none wifi
							break;
						case UpdateStatus.Timeout: // time out
							break;
						}
					}
				});
				UmengUpdateAgent.update(AboutActivity.this);
			}
		});

		findViewById(R.id.layout_about_giveme5).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				NavigationUtils.navigateToMarket(AboutActivity.this, DeviceUtil.getpackageName(AboutActivity.this));
				MobclickAgent.onEvent(AboutActivity.this,"click_about_giveme5");
			}
		});
	}

	private void init() {
		try {
			PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			String versionName = info.versionName;
			int versionCode = info.versionCode;
			tvVersionCode.setText(String.format("%s - %s", versionName, versionCode));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		checkCloseAd.setChecked(SharePreferenceUtil.getBoolean("pre_is_close_ad", false));

		initUMengSoicalService();
	}

	private void initUMengSoicalService() {
		// UMENG 分享
		mUMsocialController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		String url_link = MobclickAgent.getConfigParams(this, "url_share_link");
		String url_img = MobclickAgent.getConfigParams(this, "url_share_img");
		mUMsocialController.setShareContent("分享一款应用，能模拟十多种雨声，挺实用的。" + url_link);
		// 设置分享图片, 参数2为图片的url地址
		mUMsocialController.setShareMedia(new UMImage(this, url_img));

		mUMsocialController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.DOUBAN,
				SHARE_MEDIA.RENREN, SHARE_MEDIA.SMS, SHARE_MEDIA.SMS.EMAIL);
		mUMsocialController.getConfig().setPlatformOrder(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA,
				SHARE_MEDIA.DOUBAN, SHARE_MEDIA.RENREN, SHARE_MEDIA.SMS, SHARE_MEDIA.SMS.EMAIL);
		/*
		 * // 添加微信平台 String appId = "wx967daebe835fbeac"; String appSecret =
		 * "5fa9e68ca3970e87a1f83e563c8dcbce"; UMWXHandler wxHandler = new
		 * UMWXHandler(this,appId,appSecret); wxHandler.addToSocialSDK(); //
		 * 添加微信朋友圈 UMWXHandler wxCircleHandler = new
		 * UMWXHandler(this,appId,appSecret); wxCircleHandler.setToCircle(true);
		 * wxCircleHandler.addToSocialSDK();
		 */

		// QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468", "c7394704798a158208a74ab60104f0ba");
		qqSsoHandler.addToSocialSDK();
		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent("分享一款应用，能模拟十多种雨声，挺实用的");
		// 设置分享title
		qqShareContent.setTitle("_(:з」∠)_");
		// 设置分享图片
		qqShareContent.setShareImage(new UMImage(this, url_img));
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(url_link);
		mUMsocialController.setShareMedia(qqShareContent);

		// QQ ZONE
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468", "c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent("分享一款应用，能模拟十多种雨声，挺实用的");
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(url_link);
		// 设置分享内容的标题
		qzone.setTitle("_(:з」∠)_");
		// 设置分享图片
		qzone.setShareImage(new UMImage(this, url_img));
		mUMsocialController.setShareMedia(qzone);

		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		// 邮件
		com.umeng.socialize.sso.EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
		/*
		 * //Facebook UMFacebookHandler mFacebookHandler = new
		 * UMFacebookHandler(this,"facebook APP ID",PostType.PHOTO);
		 * mFacebookHandler.addToSocialSDK();
		 */

	};

	public void shareMsg(Context context, String activityTitle, String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File("file:///android_asset/avator.jpg");
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/jpg");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}
}
