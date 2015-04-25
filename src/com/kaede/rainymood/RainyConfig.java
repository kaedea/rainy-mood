package com.kaede.rainymood;

public class RainyConfig {
	public static final String[] TITLES = { "雷阵雨", "音乐", "田野", "中雨", "森林" , "湖", "雨中漫步", "小雨", "河水", "窗", "街道", "夜雨", "大雨"};
	public static String getPagerTitle(int index){
		return TITLES[index];
	}
	public static int getPagerLength(){
		return TITLES.length;
	}
	
	public static int getBgResource(int position) {
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
		case 5:
			resid = R.drawable.main_bg_05;
			break;
		case 6:
			resid = R.drawable.main_bg_06;
			break;
		case 7:
			resid = R.drawable.main_bg_07;
			break;
		case 8:
			resid = R.drawable.main_bg_08;
			break;
		case 9:
			resid = R.drawable.main_bg_09;
			break;
		case 10:
			resid = R.drawable.main_bg_10;
			break;
		case 11:
			resid = R.drawable.main_bg_11;
			break;
		case 12:
			resid = R.drawable.main_bg_12;
			break;

		default:
			resid = R.drawable.main_bg_00;
			break;
		}
		return resid;
	}
	
	public static int getBgmResource(int position){
		int resid = 0;
		switch (position) {
		case 0:
			resid = R.raw.rain_lightning_2;
			break;
		case 1:
			resid = R.raw.rain_music_1;
			break;
		case 2:
			resid = R.raw.rain_field_1;
			break;
		case 3:
			resid = R.raw.rain_general_1;
			break;
		case 4:
			resid = R.raw.rain_forest_1;
			break;
		case 5:
			resid = R.raw.rain_lake_1;
			break;
		case 6:
			resid = R.raw.rain_umbrella_1;
			break;
		case 7:
			resid = R.raw.rain_small_1;
			break;
		case 8:
			resid = R.raw.rain_river_1;
			break;
		case 9:
			resid = R.raw.rain_window_1;
			break;
		case 10:
			resid = R.raw.rain_small_2;
			break;
		case 11:
			resid = R.raw.rain_night_1;
			break;
		case 12:
			resid = R.raw.rain_heavy_1;
			break;

		default:
			resid = R.raw.rain_heavy_1;
			break;
		}
		return resid;
	}
}
