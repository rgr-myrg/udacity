package com.sam_chordas.android.stockhawk.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rgr-myrg on 5/9/16.
 */
public class DateUtil {
	public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final int WEEK_OFFSET = 1;

	// Solution adapted from:
	// http://stackoverflow.com/questions/9640210/compare-if-two-dates-are-within-same-week-in-android
	public static final boolean isDateSameWeek(Calendar previousDate) throws ParseException {
		final Calendar today = Calendar.getInstance();
		today.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		int thisYear = today.get(Calendar.YEAR);
		int thisWeek = today.get(Calendar.WEEK_OF_YEAR) - WEEK_OFFSET;

		previousDate.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		int previousYear = previousDate.get(Calendar.YEAR);
		int previousWeek = previousDate.get(Calendar.WEEK_OF_YEAR);

		Log.d("DateUtil", thisYear + ":" + previousYear);
		Log.d("DateUtil", thisWeek + ":" + previousWeek);

		return thisYear == previousYear && thisWeek == previousWeek;
	}
}
