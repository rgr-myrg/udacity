package com.sam_chordas.android.stockhawk.realm;

import io.realm.RealmObject;

/**
 * Created by rgr-myrg on 4/22/16.
 */
public class QuoteData extends RealmObject {
	public static final String ID_KEY  = "mId";
	public static final String SYMBOL_KEY  = "mSymbol";
	public static final String DATE_KEY  = "mDate";
	public static final String CLOSE_KEY = "mClose";

	private static int mNextId = 0;
	private int mId;
	private String mSymbol;

	private String mOpen;
	private String mLow;
	private String mHigh;
	private float mClose;
	private String mAdjClose;

//	private Long mVolume;
	private String mDate;

	public QuoteData() {
	}

	public QuoteData(
			String symbol,
			String open,
			String low,
			String high,
			float close,
			String adjClose,
			Long volume,
			String mDate) {
		this.mId = mNextId++;
		this.mSymbol = symbol;
		this.mOpen = open;
		this.mLow = low;
		this.mHigh = high;
		this.mClose = close;
		this.mAdjClose = adjClose;
//		this.mVolume = volume;
		this.mDate = mDate;
	}

	public void setValues(
			String symbol,
			String open,
			String low,
			String high,
			float close,
			String adjClose,
			Long volume,
			String mDate) {
		this.mId = mNextId++;
		this.mSymbol = symbol;
		this.mOpen = open;
		this.mLow = low;
		this.mHigh = high;
		this.mClose = close;
		this.mAdjClose = adjClose;
//		this.mVolume = volume;
		this.mDate = mDate;
	}

	public String getDate() {
		return mDate;
	}

//	public Long getVolume() {
//		return mVolume;
//	}

	public String getAdjClose() {
		return mAdjClose;
	}

	public float getClose() {
		return mClose;
	}

	public String getHigh() {
		return mHigh;
	}

	public String getLow() {
		return mLow;
	}

	public String getOpen() {
		return mOpen;
	}

	public String getSymbol() {
		return mSymbol;
	}

	public int getId() {
		return mId;
	}
}
