package com.kaede.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class NavigationUtils {

	public static void navigateToMarket(Context context, String packageName) {
		Uri uri = Uri.parse("market://details?id=" + packageName);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		try {
			context.startActivity(it);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "找不到安卓市场", Toast.LENGTH_LONG).show();;
		}
	}
}
