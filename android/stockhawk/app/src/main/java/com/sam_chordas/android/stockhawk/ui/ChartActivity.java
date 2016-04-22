package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.api.StockEvent;
import com.sam_chordas.android.stockhawk.api.YahooApi;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.util.UiUtil;

import net.usrlib.pattern.TinyEvent;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import yahoofinance.histquotes.HistoricalQuote;

public class ChartActivity extends AppCompatActivity {

//	private ProgressBar mProgressBar;
	private BarChart mBarChart;
	private Realm mRealm;
	private RealmChangeListener mRealmChangeListener;
	private String mStockSymbol;

	private TinyEvent.Listener quoteLoadedListener = new TinyEvent.Listener() {
		@Override
		public void onSuccess(Object data) {
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

//		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
//		mProgressBar.setVisibility(View.VISIBLE);
		UiUtil.displayProgressBar(this);

		mBarChart = (BarChart) findViewById(R.id.bar_chart);
		setup(mBarChart);

		setStockSymbolFromIntent();
		initRealmConfig();
	}

	private void setStockSymbolFromIntent() {
		final Intent intent = getIntent();

		if (intent == null) {
			return;
		}

		mStockSymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);

		if (mStockSymbol == null) {
			return;
		}

		getSupportActionBar().setTitle(mStockSymbol);
		YahooApi.fetchHistoricalQuote(mStockSymbol);
	}

	private void initRealmConfig() {
		//RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
		mRealm = Realm.getInstance(new RealmConfiguration.Builder(this).build());
		mRealmChangeListener = new RealmChangeListener() {
			@Override
			public void onChange() {
				Log.d("MAIN", "RealmChangeListener.onChange()");
				//setData();
			}
		};
	}

	private void onHistoricalQuoteComplete(final List<HistoricalQuote> stockData) {
		Log.d("MAIN", stockData.toString());
		UiUtil.hideProgressBar(this);
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				mProgressBar.setVisibility(View.INVISIBLE);
//			}
//		});
	}

	private void onHistoricalQuoteError() {

	}

	protected void setup(Chart<?> chart) {

	//	mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// no description text
		chart.setDescription("");
		chart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable touch gestures
		chart.setTouchEnabled(true);

		if (chart instanceof BarLineChartBase) {

			BarLineChartBase mChart = (BarLineChartBase) chart;

			mChart.setDrawGridBackground(false);

			// enable scaling and dragging
			mChart.setDragEnabled(true);
			mChart.setScaleEnabled(true);

			// if disabled, scaling can be done on x- and y-axis separately
			mChart.setPinchZoom(false);

			YAxis leftAxis = mChart.getAxisLeft();
			leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
		//	leftAxis.setTypeface(mTf);
			leftAxis.setTextSize(8f);
			leftAxis.setTextColor(Color.DKGRAY);
			leftAxis.setValueFormatter(new PercentFormatter());

			XAxis xAxis = mChart.getXAxis();
		//	xAxis.setTypeface(mTf);
			xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
			xAxis.setTextSize(8f);
			xAxis.setTextColor(Color.DKGRAY);

			mChart.getAxisRight().setEnabled(false);
		}
	}
}
