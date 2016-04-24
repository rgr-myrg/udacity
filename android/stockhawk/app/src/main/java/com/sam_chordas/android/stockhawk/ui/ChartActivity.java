package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.api.StockEvent;
import com.sam_chordas.android.stockhawk.api.YahooApi;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.realm.QuoteRealm;
import com.sam_chordas.android.stockhawk.util.UiUtil;

import net.usrlib.pattern.TinyEvent;

import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

public class ChartActivity extends AppCompatActivity {
	private LineChart mLineChart;
	private QuoteRealm mQuoteRealm = new QuoteRealm();
	private String mStockSymbol;

	private TinyEvent.Listener quoteLoadedListener = new TinyEvent.Listener() {
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
		setContentView(R.layout.activity_chart);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		StockEvent.QuoteLoaded.addListener(quoteLoadedListener);

		UiUtil.displayProgressBar(this);

		mQuoteRealm.onCreate(this);

		initLineChart();

		mStockSymbol = getStockSymbolFromIntent();

		if (mStockSymbol == null) {
			UiUtil.displayDefaultErrorMsg(this);
			return;
		}

		getSupportActionBar().setTitle(mStockSymbol);
		YahooApi.fetchHistoricalQuote(mStockSymbol);
	}

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

	private void onHistoricalQuoteComplete(final List<HistoricalQuote> quoteList) {
		mQuoteRealm.saveHistoricalQuoteList(quoteList);
		setLineChartData(mQuoteRealm.getRealmLineData(mStockSymbol));

		UiUtil.hideProgressBar(this);
	}

	private void onHistoricalQuoteError() {

	}

	private void setLineChartData(RealmLineData data) {
		//CustomMarkerView customMarkerView = new CustomMarkerView(this, R.layout.custom_marker_view);
		//mLineChart.setMarkerView(customMarkerView);

//		XAxis xAxis = mLineChart.getXAxis();
//		xAxis.enableGridDashedLine(8f, 5f, 0f);
//		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//		YAxis yAxis = mLineChart.getAxisRight();
//		yAxis.setDrawLabels(false);
//		yAxis.enableGridDashedLine(8f, 5f, 0f);

		mLineChart.setAutoScaleMinMaxEnabled(true);
		mLineChart.getLegend().setEnabled(false);
		mLineChart.setData(data);

		runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						mLineChart.animateXY(
								3000,
								1000,
								Easing.EasingOption.Linear,
								Easing.EasingOption.Linear
						);
					}
				}
		);
	}

	private void initLineChart() {
		mLineChart = (LineChart) findViewById(R.id.line_chart);
		mLineChart.setTouchEnabled(true);
		//mLineChart.setDrawGridBackground(false);
		mLineChart.setDescription("");
		mLineChart.setNoDataText("Loading chart...");
		mLineChart.setDrawGridBackground(true);


		// enable scaling and dragging
		mLineChart.setDragEnabled(true);
		mLineChart.setScaleEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		mLineChart.setPinchZoom(false);

		YAxis leftAxis = mLineChart.getAxisLeft();

		// Reset all limit lines to avoid overlapping lines
		leftAxis.removeAllLimitLines();

		leftAxis.setTextSize(8f);
		leftAxis.setTextColor(Color.DKGRAY);
		leftAxis.setValueFormatter(new PercentFormatter());

		XAxis xAxis = mLineChart.getXAxis();

		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		xAxis.setTextSize(8f);
		xAxis.setTextColor(Color.DKGRAY);

		mLineChart.getAxisRight().setEnabled(false);
	}
}
