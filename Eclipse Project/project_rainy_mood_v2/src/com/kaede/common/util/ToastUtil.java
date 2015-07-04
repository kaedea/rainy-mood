package com.kaede.common.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kaede.rainymood.R;

public class ToastUtil {
	private static final String TAG = "ToastUtil";
	private static Toast toast;
	private static TextView tv;

	public static void toast(Context context, String message) {
		if (toast != null) {
			tv.setText(message);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		} else {
			View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_bg, null);
			toast=new Toast(context);
			toast.setView(toastRoot);
			toast.setGravity(Gravity.CENTER, 0, ResolutionUtil.dipToPx(context, 170f));
		    tv=(TextView)toastRoot.findViewById(R.id.tv_toast);
			tv.setText(message);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	public static void toast(Context context, String message,int duration) {
		if (toast != null) {
			tv.setText(message);
			toast.setDuration(duration);
			toast.show();
		} else {
			View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast_bg, null);
			toast=new Toast(context);
			toast.setView(toastRoot);
			toast.setGravity(Gravity.CENTER, 0, ResolutionUtil.dipToPx(context, 170f));
		    tv=(TextView)toastRoot.findViewById(R.id.tv_toast);
			tv.setText(message);
			toast.setDuration(duration);
			toast.show();
		}
	}
}
