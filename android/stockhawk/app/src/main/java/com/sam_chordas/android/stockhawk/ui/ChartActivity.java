package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.api.DateVO;
import com.sam_chordas.android.stockhawk.api.StockEvent;
import com.sam_chordas.android.stockhawk.api.YahooApi;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.realm.QuoteRealm;
import com.sam_chordas.android.stockhawk.util.UiUtil;

import net.usrlib.pattern.TinyEvent;

import java.text.ParseException;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

public class ChartActivity extends AppCompatActivity {
	public static final String NAME = ChartActivity.class.getSimpleName();

	private QuoteRealm mQuoteRealm = new QuoteRealm();
	private LineChart mLineChart;
	private String mStockSymbol;

	private TinyEvent.Listener mQuoteLoadedListener = new TinyEvent.Listener() {
		@Override
		public void onSuccess(final Object data) {
			onHistoricalQuoteComplete((List<HistoricalQuote>) data);
		}

		@Override
		public void onError(Object data) {
			onHistoricalQuoteError();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
		);

		setContentView(R.layout.activity_chart);
		UiUtil.displayProgressBar(this);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		StockEvent.QuoteLoaded.addListenerOnce(mQuoteLoadedListener);

		mQuoteRealm.onCreate(this);

		mStockSymbol = getStockSymbolFromIntent();

		if (mStockSymbol == null) {
			UiUtil.displayDefaultErrorMsg(this);
			return;
		}

		getSupportActionBar().setTitle(mStockSymbol.toUpperCase());

		fetchHistoricalQuote();
	}

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Log.d(NAME, "onDestroy");
//		mLineChart.destroyDrawingCache();
//	}

	@Override
	protected void onResume() {
		super.onResume();
		mQuoteRealm.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mQuoteRealm.close();
	}

	private String getStockSymbolFromIntent() {
		final Intent intent = getIntent();

		if (intent == null) {
			return null;
		}

		final String stockSymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);

		return stockSymbol;
	}

	private void fetchHistoricalQuote() {
		Log.d(NAME, "fetchHistoricalQuote mStockSymbol: " + mStockSymbol);

		try {
			DateVO dateVO = mQuoteRealm.getDateWithSymbolLookup(mStockSymbol);
			YahooApi.fetchHistoricalQuoteWithDate(mStockSymbol, dateVO);
		} catch (ParseException e) {
			e.printStackTrace();
			//YahooApi.fetchHistoricalQuote(mStockSymbol);
		} finally {
			//YahooApi.fetchHistoricalQuote(mStockSymbol);
		}
	}

	private void onHistoricalQuoteComplete(final List<HistoricalQuote> quoteList) {
		Log.d(NAME, "onHistoricalQuoteComplete");

		runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						mQuoteRealm.saveHistoricalQuoteList(quoteList);
						setLineChartData(mQuoteRealm.getRealmLineData(mStockSymbol));
					}
				}
		);
	}

	private void onHistoricalQuoteError() {

	}

	private void setLineChartData(RealmLineData data) {
		Log.d(NAME, "setLineChartData count: " + data.getDataSetCount());

		UiUtil.hideProgressBar(this);

		mLineChart = (LineChart) findViewById(R.id.line_chart);
		mLineChart.setLogEnabled(true);

//		mLineChart.destroyDrawingCache();
//		mLineChart.clear();

		mLineChart.setDrawGridBackground(true);
		mLineChart.setTouchEnabled(true);
		mLineChart.setDescription("");
		mLineChart.setDragEnabled(true);
		mLineChart.setScaleEnabled(true);
		mLineChart.setVisibility(View.VISIBLE);
		mLineChart.setMarkerView(new ChartMarkerView(this, R.layout.chart_marker_view));
		//mLineChart.setNoDataText("Loading chart...");

		XAxis xAxis = mLineChart.getXAxis();
		xAxis.enableGridDashedLine(8f, 5f, 0f);
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		YAxis yAxis = mLineChart.getAxisRight();
		yAxis.setDrawLabels(false);
		yAxis.enableGridDashedLine(8f, 5f, 0f);

		mLineChart.setAutoScaleMinMaxEnabled(true);
		mLineChart.getLegend().setEnabled(false);
		mLineChart.setData(data);
//		mLineChart.animateXY(
//				1000,
//				1000,
//				Easing.EasingOption.Linear,
//				Easing.EasingOption.Linear
//		);

		//Refresh chart
		mLineChart.invalidate();
		mLineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
	}

//	private void initLineChart() {
//		Log.d(NAME, "initLineChart");
//
//		mLineChart = (LineChart) findViewById(R.id.line_chart);
//		//mLineChart.setLogEnabled(true);
//		mLineChart.setTouchEnabled(true);
//		mLineChart.setDescription("");
//		mLineChart.setNoDataText("Loading chart...");
//		mLineChart.setDrawGridBackground(true);
//
//		// enable scaling and dragging
//		mLineChart.setDragEnabled(true);
//		mLineChart.setScaleEnabled(true);
//
//		// if disabled, scaling can be done on x- and y-axis separately
//		mLineChart.setPinchZoom(false);
//
//		YAxis leftAxis = mLineChart.getAxisLeft();
//
//		// Reset all limit lines to avoid overlapping lines
//		leftAxis.removeAllLimitLines();
//
//		leftAxis.setTextSize(8f);
//		leftAxis.setTextColor(Color.DKGRAY);
//		leftAxis.setValueFormatter(new PercentFormatter());
//
//		XAxis xAxis = mLineChart.getXAxis();
//
//		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//		xAxis.setTextSize(8f);
//		xAxis.setTextColor(Color.DKGRAY);
//
//		mLineChart.getAxisRight().setEnabled(false);
//	}
}
