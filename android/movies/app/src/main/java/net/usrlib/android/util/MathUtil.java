package net.usrlib.android.util;

public class MathUtil {

	// Helpful Util method sourced from:
	// http://stackoverflow.com/questions/22186778/using-math-round-to-round-to-one-decimal-place
	public static double round(double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

}
