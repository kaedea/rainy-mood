package com.kaede.common.util;

public class StringUtil {

	public static int safeParseInt(String s) {
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
		return i;
	}
	
	public static float safeParseFloat(String s) {
		float i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
		return i;
	}
}
