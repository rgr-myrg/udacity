package com.sam_chordas.android.stockhawk.realm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.api.DateVO;
import com.sam_chordas.android.stockhawk.util.ColorUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by rgr-myrg on 4/22/16.
 */
public class QuoteRealm {
	private Realm mRealm;
	private RealmConfiguration mConfig = null;
	private AppCompatActivity mApp;

	public QuoteRealm() {
	}

	public Realm getRealmInstance() {
		mConfig = new RealmConfiguration.Builder(mApp)
				.name("net.usrlib.realm")
				.build();

		Realm.setDefaultConfiguration(mConfig);

		return Realm.getInstance(mConfig);
	}

	public void onCreate(final AppCompatActivity app) {
		mApp = app;
	}

	public void onResume(final AppCompatActivity app) {
		mApp = app;
	}

	public void close() {
		//mRealm = Realm.getDefaultInstance();
		mRealm = getRealmInstance();
		mRealm.close();
	}

	public void saveHistoricalQuoteList(final List<HistoricalQuote> quotes) {
		mRealm = getRealmInstance();

		Log.d("REALM", "size: " + String.valueOf(quotes.size()));
		mRealm.beginTransaction();

		for (int x = 0; x < quotes.size(); x++) {
			HistoricalQuote quote = quotes.get(x);
			QuoteData quoteData = mRealm.createObject(QuoteData.class);
			//Log.d("QuoteRealm", quote.getDate().toString());
			//Log.d("QuoteRealm", DateVO.dateFormat.format(quote.getDate()) );
			Log.d("QuoteRealm", DateVO.dateFormat.format(quote.getDate().getTime()) );

			quoteData.setValues(
					quote.getSymbol(),
					quote.getOpen().toString(),
					quote.getLow().toString(),
					quote.getHigh().toString(),
					quote.getClose().floatValue(),
					quote.getAdjClose().toString(),
					quote.getVolume(),
					DateVO.dateFormat.format(quote.getDate().getTime())
					//quote.getDate().toString()
			);

			mRealm.copyToRealm(quoteData);
		}

		mRealm.commitTransaction();
		//mRealm.close();
	}

	public RealmLineData getRealmLineData(final String symbol) {
		mRealm = getRealmInstance();

		final RealmResults<QuoteData> results = mRealm
				.where(QuoteData.class)
				.equalTo(QuoteData.SYMBOL_KEY, symbol)
				.findAllSorted(QuoteData.DATE_KEY);
				//.findAll();

		Log.d("REALM", results.toString());

		final RealmLineDataSet<QuoteData> dataSet = new RealmLineDataSet<QuoteData>(
				results,
				QuoteData.CLOSE_KEY,
				QuoteData.ID_KEY
		);

		dataSet.setDrawFilled(true);
		//dataSet.setFillAlpha(10);
		dataSet.setFillColor(Color.parseColor(ColorUtil.getNextColorDarkTheme()));
		dataSet.setCircleColor(Color.BLACK);
		dataSet.setCircleSize(2f);

		ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

		dataSets.add(dataSet);

		//mRealm.close();
		return new RealmLineData(results, QuoteData.DATE_KEY, dataSets);
	}

	public DateVO getDateWithSymbolLookup(final String symbol) throws ParseException {
		mRealm = getRealmInstance();

		final RealmResults<QuoteData> results = mRealm
				.where(QuoteData.class)
				.equalTo(QuoteData.SYMBOL_KEY, symbol)
				.findAllSorted(QuoteData.DATE_KEY);
				//.findAll();

		return DateVO.fromRealmResults(results);
	}
}

//	public void getDates() {
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Calendar cal = Calendar.getInstance();
//		currentDate = dateFormat.format(cal.getTime());
//		RealmResults<HistoricalData> historicalData = realm.where(HistoricalData.class).equalTo("stock", symbol).findAll();
//		if (historicalData.size() > 0) {
//			pastDate = historicalData.last().getDate();
//			lastId = historicalData.last().getId();
//			Log.d("TEST", lastId + "  " + pastDate);
//		} else {
//			lastId = -1;
//			cal.add(Calendar.YEAR, -1);
//			pastDate = dateFormat.format(cal.getTime());
//		}
//	}