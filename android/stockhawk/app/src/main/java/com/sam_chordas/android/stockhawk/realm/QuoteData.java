package com.sam_chordas.android.stockhawk.realm;

import io.realm.RealmObject;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by rgr-myrg on 4/22/16.
 */
public class QuoteData extends RealmObject {
	private static int mNextId = 0;
	private int mId;
	private String mSymbol;

	private String mOpen;
	private String mLow;
	private String mHigh;
	private float mClose;
	private String mAdjClose;

	private Long mVolume;
	private String mDate;

	public QuoteData() {
	}

	public QuoteData(
			int id,
			String symbol,
			String open,
			String low,
			String high,
			float close,
			String adjClose,
			Long volume,
			String mDate) {
		this.mId = id;
		this.mSymbol = symbol;
		this.mOpen = open;
		this.mLow = low;
		this.mHigh = high;
		this.mClose = close;
		this.mAdjClose = adjClose;
		this.mVolume = volume;
		this.mDate = mDate;
	}

	public String getDate() {
		return mDate;
	}

	public Long getVolume() {
		return mVolume;
	}

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

	public static final QuoteData fromHistoricalQuote(final HistoricalQuote quote) {
		return new QuoteData(
				mNextId++,
				quote.getSymbol(),
				quote.getOpen().toString(),
				quote.getLow().toString(),
				quote.getHigh().toString(),
				quote.getClose().floatValue(),
				quote.getAdjClose().toString(),
				quote.getVolume(),
				quote.getDate().toString()
		);
	}
}
