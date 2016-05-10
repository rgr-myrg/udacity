package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteDbHelper;
import com.sam_chordas.android.stockhawk.util.DateUtil;
import com.sam_chordas.android.stockhawk.util.UiUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Created by rgr-myrg on 5/9/16.
 */
public class ChartSqlActivity extends AppCompatActivity {
	public static final String NAME = ChartActivity.class.getSimpleName();
	public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static final int YEAR_OFFSET = -1;

	private QuoteDbHelper mQuoteDbHelper;
	private String mStockSymbol = null;

	private ArrayList<Entry> mStockValueDataSet;
	private ArrayList<String> mDateForXAxis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mQuoteDbHelper = new QuoteDbHelper(getApplicationContext());
		mStockValueDataSet = new ArrayList<>();
		mDateForXAxis = new ArrayList<>();

		mStockSymbol = getStockSymbolFromIntent();

		if (mStockSymbol == null) {
			UiUtil.displayDefaultErrorMsg(this);
			return;
		}

		getSupportActionBar().setTitle(mStockSymbol.toUpperCase());

		fetchHistoricalQuoteFromDb(mStockSymbol);
	}

	private String getStockSymbolFromIntent() {
		final Intent intent = getIntent();

		if (intent == null) {
			return null;
		}

		final String stockSymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);

		return stockSymbol;
	}

	private void fetchHistoricalQuoteFromDb(final String stockSymbol) {
		Log.i(NAME, "fetchHistoricalQuoteFromDb " + stockSymbol);

		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<HistoricalQuote> list = mQuoteDbHelper.selectQuotesWithSymbol(stockSymbol);

				if (isDateSameWeek(list)) {
					// Render Chart With Data
					onHistoricalQuoteLoaded(list);
				} else {
					// New Data Set Needed
					fetchHistoricalQuoteFromYahooFinance(stockSymbol);
				}
			}
		}).start();
	}

	private void fetchHistoricalQuoteFromYahooFinance(final String stockSymbol) {
		Log.i(NAME, "fetchHistoricalQuoteFromYahooFinance " + stockSymbol);

		final Calendar lastYear = Calendar.getInstance();
		lastYear.add(Calendar.YEAR, YEAR_OFFSET);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final Stock stock = YahooFinance.get(
							stockSymbol,
							lastYear,
							Calendar.getInstance(),
							Interval.MONTHLY
					);

					final List<HistoricalQuote> list = stock.getHistory();

					// Render Chart With Data
					onHistoricalQuoteLoaded(list);

					// Save Data Results in Db
					mQuoteDbHelper.bulkInsertWithQuoteList(list);
				} catch (IOException e) {
					onHistoricalQuoteError(e.getMessage());
				}
			}
		}).start();
	}

	private void onHistoricalQuoteLoaded(final List<HistoricalQuote> historicalQuoteList) {
		Log.i(NAME, "onHistoricalQuoteLoaded " + historicalQuoteList.toString());

		if (historicalQuoteList == null || historicalQuoteList.isEmpty()) {
			onHistoricalQuoteError("HistoricalQuote is Empty.");
			return;
		}

		runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						setLineChartDataAndRenderGraph(historicalQuoteList);
					}
				}
		);
	}

	private void onHistoricalQuoteError(String message) {
		Log.e(NAME, "Error: " + message);
	}

	private void setLineChartDataAndRenderGraph(final List<HistoricalQuote> historicalQuoteList) {
		for (int x = 0; x < historicalQuoteList.size(); x++) {
			final HistoricalQuote quote = historicalQuoteList.get(x);
			final float closeValue = quote.getClose().floatValue();
			final String dateValue = sDateFormat.format(quote.getDate().getTime());

			mStockValueDataSet.add(new Entry(closeValue, x));
			mDateForXAxis.add(dateValue);

		}

//		for (int y = historicalQuoteList.size() - 1; y > 0; y--) {
//			final HistoricalQuote quote = historicalQuoteList.get(y);
//
//			mStockValueDataSet.add(new Entry(quote.getClose().floatValue(), y));
//			mDateForXAxis.add(sDateFormat.format(quote.getDate().getTime()));
//		}

		final LineChart lineChart = (LineChart) findViewById(R.id.line_chart);
		final LineDataSet lineDataSet = new LineDataSet(mStockValueDataSet, mStockSymbol);
		final LineData lineData = new LineData(mDateForXAxis, lineDataSet);

		lineDataSet.setDrawCubic(false);
		lineDataSet.setLabel(getString(R.string.line_chart_label));
		lineDataSet.setDrawCircleHole(false);
		lineDataSet.setColor(ColorTemplate.rgb("#FF5722"));
		lineDataSet.setCircleColor(ColorTemplate.rgb("#FF5722"));
		lineDataSet.setLineWidth(1.8f);
		lineDataSet.setCircleRadius(3.6f);

		lineChart.getXAxis().setDrawGridLines(false);
		lineChart.setDescription("");
		lineChart.setData(lineData);
		lineChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
	}

	private boolean isDateSameWeek(final List<HistoricalQuote> historicalQuoteList) {
		if (historicalQuoteList == null || historicalQuoteList.isEmpty()) {
			return false;
		}

		//final HistoricalQuote lastQuote = historicalQuoteList.get(historicalQuoteList.size() - 1);
		final HistoricalQuote lastQuote = historicalQuoteList.get(0);

		try {
			return DateUtil.isDateSameWeek(lastQuote.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}
}
