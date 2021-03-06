package com.sam_chordas.android.stockhawk.realm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.api.DateVO;

import net.usrlib.material.MaterialTheme;
import net.usrlib.material.Theme;
import net.usrlib.material.color.Red;

import java.text.ParseException;
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
	public static final String NAME = QuoteRealm.class.getSimpleName();
	public static final String REALM = "net.usrlib.realm";

	private Realm mRealm;
	private RealmConfiguration mConfig = null;
	private MaterialTheme mTheme = MaterialTheme.get(Theme.COOL);
//	private AppCompatActivity mApp;

	public QuoteRealm() {
	}

	public Realm getRealmInstance(AppCompatActivity app) {
		mConfig = new RealmConfiguration.Builder(app)
				.name(REALM)
				.build();

		Realm.setDefaultConfiguration(mConfig);

		return Realm.getInstance(mConfig);
	}

//	public void onCreate(final AppCompatActivity app) {
//		mApp = app;
//	}
//
	public void onResume(final AppCompatActivity app) {
		mRealm = getRealmInstance(app);
	}

	public void close(final AppCompatActivity app) {
		//mRealm = Realm.getDefaultInstance();
		mRealm = getRealmInstance(app);
		mRealm.close();
	}

	public void saveHistoricalQuoteList(final List<HistoricalQuote> quotes, final AppCompatActivity app) {
		if (quotes == null || quotes.size() == 0) {
			Log.i(NAME, "saveHistoricalQuoteList empty list. Not processing.");
			return;
		}

		mRealm = getRealmInstance(app);
		mRealm.beginTransaction();

		for (int x = 0; x < quotes.size(); x++) {
			HistoricalQuote quote = quotes.get(x);
			QuoteData quoteData = mRealm.createObject(QuoteData.class);

			Log.d(NAME, DateVO.dateFormat.format(quote.getDate().getTime()));
			Log.d(NAME, Calendar.getInstance().getTime().toString());

					quoteData.setValues(
							quote.getSymbol(),
							quote.getOpen().toString(),
							quote.getLow().toString(),
							quote.getHigh().toString(),
							quote.getClose().floatValue(),
							quote.getAdjClose().toString(),
							quote.getVolume(),
							DateVO.dateFormat.format(Calendar.getInstance().getTime())
					);

			mRealm.copyToRealm(quoteData);
		}

//		mRealm.executeTransactionAsync(
//				new Realm.Transaction() {
//					@Override
//					public void execute(Realm realm) {
//						for (int x = 0; x < quotes.size(); x++) {
//							final HistoricalQuote quote = quotes.get(x);
//							Log.d("REALM",String.valueOf(x));
//							mRealm.copyToRealm(
//									new QuoteData(
//											quote.getSymbol(),
//											quote.getOpen().toString(),
//											quote.getLow().toString(),
//											quote.getHigh().toString(),
//											quote.getClose().floatValue(),
//											quote.getAdjClose().toString(),
//											quote.getVolume(),
//											DateVO.dateFormat.format(quote.getDate().getTime())
//									)
//							);
//						}
//					}
//				}
//		);

		mRealm.commitTransaction();
		mRealm.close();
	}

	public RealmLineData getRealmLineData(final String symbol, final AppCompatActivity app) {
		mRealm = getRealmInstance(app);

		final RealmResults<QuoteData> results = mRealm
				.where(QuoteData.class)
				.equalTo(QuoteData.SYMBOL_KEY, symbol)
				.findAllSorted(QuoteData.DATE_KEY);

//		results.addChangeListener(
//				new RealmChangeListener() {
//					@Override
//					public void onChange() {
//						Log.d(NAME, "onChange");
//					}
//				}
//		);

		Log.d(NAME, "getRealmLineData results: " + results.size());

		final RealmLineDataSet<QuoteData> dataSet = new RealmLineDataSet<QuoteData>(
				results,
				QuoteData.CLOSE_KEY,
				QuoteData.ID_KEY
		);

		dataSet.setDrawFilled(true);
		dataSet.setFillAlpha(100);
		dataSet.setFillColor(Color.parseColor(mTheme.getNextColor().hex));

		//dataSet.setFillColor(Color.BLUE);
		dataSet.setCircleColor(Color.BLACK);
		dataSet.setCircleSize(2f);

		ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

		dataSets.add(dataSet);

		//mRealm.close();
		return new RealmLineData(results, QuoteData.DATE_KEY, dataSets);
	}

	public DateVO getDateWithSymbolLookup(final String symbol, final AppCompatActivity app)
			throws ParseException {
		mRealm = getRealmInstance(app);

		final RealmResults<QuoteData> results = mRealm
				.where(QuoteData.class)
				.equalTo(QuoteData.SYMBOL_KEY, symbol)
				.findAllSorted(QuoteData.DATE_KEY);

		Log.d(NAME, "getDateWithSymbolLookup results: " + results.size());

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