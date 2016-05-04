package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.realm.QuoteData;
import com.sam_chordas.android.stockhawk.util.UiUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by rgr-myrg on 5/4/16.
 */
public class ChartActivity extends AppCompatActivity {
	public static final String NAME = ChartActivity.class.getSimpleName();
	public static final String REALM_ID = "net.usrlib.realm";

	public static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private RealmResults<QuoteData> mRealmResults = null;
	private RealmConfiguration mRealmConfig = null;

	private Realm mRealmInstance = null;
	private String mStockSymbol = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mStockSymbol = getStockSymbolFromIntent();

		if (mStockSymbol == null) {
			UiUtil.displayDefaultErrorMsg(this);
			return;
		}

		getSupportActionBar().setTitle(mStockSymbol.toUpperCase());

		initRealmDatabase(this);

		mRealmResults = getRealmResults(mStockSymbol);

		if (mRealmResults == null || mRealmResults.isEmpty() || mRealmResults.size() == 0) {
			Log.i(NAME, "mRealmResults returns empty");
			fetchHistoricalQuote(mStockSymbol);
		} else {
			setLineChartDataAndRenderGraph();
		}
	}

	private String getStockSymbolFromIntent() {
		final Intent intent = getIntent();

		if (intent == null) {
			return null;
		}

		final String stockSymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);

		return stockSymbol;
	}

	private void initRealmDatabase(final AppCompatActivity app) {
		mRealmConfig = new RealmConfiguration.Builder(app)
				.name(REALM_ID)
				.build();

		Realm.setDefaultConfiguration(mRealmConfig);
		mRealmInstance = Realm.getInstance(mRealmConfig);
	}

	private RealmResults<QuoteData> getRealmResults(final String stockSymbol) {
		final RealmResults<QuoteData> results = mRealmInstance
				.where(QuoteData.class)
				.equalTo(QuoteData.SYMBOL_KEY, stockSymbol)
				.findAllSorted(QuoteData.DATE_KEY);

		Log.i(NAME, "getRealmResults " + stockSymbol + " returns " + results.size());

		return results;
	}

	private void fetchHistoricalQuote(final String stockSymbol) {
		Log.i(NAME, "fetchHistoricalQuote " + stockSymbol);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Stock stock = YahooFinance.get(stockSymbol);

					List<HistoricalQuote> list = stock.getHistory();

					if (list == null || list.isEmpty()) {
						onHistoricalQuoteError("HistoricalQuote is Empty.");
						return;
					}

					onHistoricalQuoteLoaded(list);
				} catch (IOException e) {
					onHistoricalQuoteError(e.getMessage());
				}
			}
		}).start();
	}

	private void onHistoricalQuoteLoaded(final List<HistoricalQuote> historicalQuoteList) {
		Log.i(NAME, "onHistoricalQuoteLoaded " + historicalQuoteList.toString());

		runOnUiThread(
				new Runnable() {
					@Override
					public void run() {
						saveHistoricalQuoteList(historicalQuoteList);
						setLineChartDataAndRenderGraph();
					}
				}
		);
	}

	private void onHistoricalQuoteError(String message) {
		Log.e(NAME, "Error: " + message);
	}

	private void saveHistoricalQuoteList(final List<HistoricalQuote> historicalQuoteList) {
		Log.i(NAME, "saveHistoricalQuoteList quotesList.size: " + historicalQuoteList.size());

		mRealmInstance.beginTransaction();

		for (int x = 0; x < historicalQuoteList.size(); x++) {
			HistoricalQuote quote = historicalQuoteList.get(x);
			QuoteData quoteData = mRealmInstance.createObject(QuoteData.class);

			quoteData.setValues(
					quote.getSymbol(),
					quote.getOpen().toString(),
					quote.getLow().toString(),
					quote.getHigh().toString(),
					quote.getClose().floatValue(),
					quote.getAdjClose().toString(),
					quote.getVolume(),
					sDateFormat.format(Calendar.getInstance().getTime())
			);

			mRealmInstance.copyToRealm(quoteData);
		}

		mRealmInstance.commitTransaction();
		mRealmResults = getRealmResults(mStockSymbol);
	}

	private void setLineChartDataAndRenderGraph() {
		Log.i(NAME, "setLineChartData size: " + mRealmResults.size());

		final RealmLineDataSet<QuoteData> dataSet = new RealmLineDataSet<QuoteData>(
				mRealmResults,
				QuoteData.CLOSE_KEY,
				QuoteData.ID_KEY
		);

		dataSet.setDrawFilled(true);
		dataSet.setFillAlpha(100);
		//dataSet.setFillColor(Color.parseColor(mTheme.getNextColor().hex));

		dataSet.setFillColor(Color.BLUE);
		dataSet.setCircleColor(Color.BLACK);
		dataSet.setCircleSize(2f);

		ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

		dataSets.add(dataSet);

		RealmLineData realmLineData = new RealmLineData(mRealmResults, QuoteData.DATE_KEY, dataSets);

		final LineChart lineChart = (LineChart) findViewById(R.id.line_chart);

		lineChart.setLogEnabled(true);
//		lineChart.destroyDrawingCache();
//		lineChart.clear();
		lineChart.setDrawGridBackground(true);
		lineChart.setTouchEnabled(true);
		lineChart.setDescription("");
		lineChart.setDragEnabled(true);
		lineChart.setScaleEnabled(true);
		lineChart.setVisibility(View.VISIBLE);
		lineChart.setMarkerView(new ChartMarkerView(this, R.layout.chart_marker_view));
		//lineChart.setNoDataText("Loading chart...");

		XAxis xAxis = lineChart.getXAxis();
		xAxis.enableGridDashedLine(8f, 5f, 0f);
		xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
		YAxis yAxis = lineChart.getAxisRight();
		yAxis.setDrawLabels(false);
		yAxis.enableGridDashedLine(8f, 5f, 0f);

		lineChart.setAutoScaleMinMaxEnabled(true);
		lineChart.getLegend().setEnabled(false);
		lineChart.notifyDataSetChanged();

//		lineChart.animateXY(
//				1000,
//				1000,
//				Easing.EasingOption.Linear,
//				Easing.EasingOption.Linear
//		);

		//Refresh chart
		lineChart.invalidate();
		lineChart.animateX(1000, Easing.EasingOption.EaseInOutQuart);
		lineChart.setData(realmLineData);
		UiUtil.hideProgressBar(this);
	}
}
