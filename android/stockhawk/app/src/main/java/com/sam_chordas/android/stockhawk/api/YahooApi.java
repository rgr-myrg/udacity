package com.sam_chordas.android.stockhawk.api;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by rgr-myrg on 4/21/16.
 */
public class YahooApi {
	public static final String NAME = YahooApi.class.getSimpleName();
	public static final String NULL_ERROR = "Error: HistoricalQuote List is Null.";

	public static void fetchHistoricalQuoteWithDate(final String symbol, final DateVO dateVO) {
		Log.d(NAME, "from: " + dateVO.getStartCalendar().getTime() + " to: " + dateVO.getCurrentCalendar().getTime());

		if (dateVO.isDateToday()) {
			Log.i(NAME, "Not enough time has passed for another request.");
			// Trigger event
			StockEvent.QuoteLoaded.notifySuccess();
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Stock stock = YahooFinance.get(
							symbol,
							dateVO.getStartCalendar(),
							dateVO.getCurrentCalendar()
					);

					List<HistoricalQuote> list = stock.getHistory();

					if (list == null) {
						StockEvent.QuoteLoaded.notifyError(NULL_ERROR);
					}

					StockEvent.QuoteLoaded.notifySuccess(list);
				} catch (IOException e) {
					StockEvent.QuoteLoaded.notifyError(e.getMessage());
				}
			}
		}).start();
	}
	/*
Calendar from = Calendar.getInstance();
Calendar to = Calendar.getInstance();
from.add(Calendar.YEAR, -5); // from 5 years ago

Stock google = YahooFinance.get("GOOG", from, to, Interval.WEEKLY);

*/
//	public static List<HistoricalQuote> fetchQuoteWithSymbol(final String symbol) {
//		final List<HistoricalQuote> list;
//		new Thread(
//				new Runnable() {
//					@Override
//					public void run() {
//						try {
//							Stock stock = YahooFinance.get(symbol, true);
//							list = stock.getHistory();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//		).start();
//		return list;
//	}
}
