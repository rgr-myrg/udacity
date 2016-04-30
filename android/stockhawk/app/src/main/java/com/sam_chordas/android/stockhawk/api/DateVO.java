package com.sam_chordas.android.stockhawk.api;

import android.util.Log;

import com.sam_chordas.android.stockhawk.realm.QuoteData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.RealmResults;

/**
 * Created by rgr-myrg on 4/28/16.
 */
public class DateVO {
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private Calendar mStartCalendar = Calendar.getInstance();
	private Calendar mCurrentCalendar = Calendar.getInstance();

	public DateVO() {}

	public static final DateVO fromRealmResults(RealmResults<QuoteData> results) throws ParseException {
		final DateVO dateVO = new DateVO();

		dateVO.mCurrentCalendar = Calendar.getInstance();

		if (results.size() > 0) {
			Log.d("DateVO", "last: " + results.last().getDate() + " first: " + results.first().getDate());
			dateVO.mStartCalendar.setTime(dateFormat.parse(results.last().getDate()));
		} else {
			dateVO.mStartCalendar.add(Calendar.YEAR, -1);
		}

		return dateVO;
	}

	public Calendar getStartCalendar() {
		return mStartCalendar;
	}

	public Calendar getCurrentCalendar() {
		return mCurrentCalendar;
	}

	public boolean isDateToday() {
		return mStartCalendar.get(Calendar.MONTH) == mCurrentCalendar.get(Calendar.MONTH)
				&& mStartCalendar.get(Calendar.YEAR) == mCurrentCalendar.get(Calendar.YEAR);
	}
}
