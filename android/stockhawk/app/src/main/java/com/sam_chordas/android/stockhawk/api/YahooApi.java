package com.sam_chordas.android.stockhawk.api;

import java.io.IOException;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by rgr-myrg on 4/21/16.
 */
public class YahooApi {
	public static final String NULL_ERROR = "Error: HistoricalQuote List is Null.";

	public static void fetchHistoricalQuote(final String symbol) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Stock tesla = YahooFinance.get(symbol, true);
					//Stock tesla = YahooFinance.get(symbol, )
					List<HistoricalQuote> list = tesla.getHistory();

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
}
