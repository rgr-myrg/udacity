package com.sam_chordas.android.stockhawk.realm;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.data.realm.implementation.RealmLineData;
import com.github.mikephil.charting.data.realm.implementation.RealmLineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
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

	public QuoteRealm() {
	}

	public void onCreate(final AppCompatActivity app) {
		if (mConfig != null) {
			return;
		}

		mConfig = new RealmConfiguration.Builder(app)
				.name("net.usrlib.realm")
				.build();

		Realm.setDefaultConfiguration(mConfig);
	}

	public void onResume(final AppCompatActivity app) {
//		Realm.deleteRealm(mConfig);
		Realm.setDefaultConfiguration(mConfig);
//
//		mRealm = Realm.getInstance(mConfig);
//		mRealm = Realm.getDefaultInstance();
	}

	public void close() {
		mRealm = Realm.getDefaultInstance();
		mRealm.close();
	}

	public void saveHistoricalQuoteList(final List<HistoricalQuote> quotes) {
		//mRealm = Realm.getInstance(mConfig);
		mRealm = Realm.getDefaultInstance();

		Log.d("REALM", "size: " + String.valueOf(quotes.size()));
		mRealm.beginTransaction();

		for (int x = 0; x < quotes.size(); x++) {
			HistoricalQuote quote = quotes.get(x);
			QuoteData quoteData = mRealm.createObject(QuoteData.class);
			quoteData.setValues(
					quote.getSymbol(),
					quote.getOpen().toString(),
					quote.getLow().toString(),
					quote.getHigh().toString(),
					quote.getClose().floatValue(),
					quote.getAdjClose().toString(),
					quote.getVolume(),
					quote.getDate().toString()
			);
			mRealm.copyToRealm(quoteData);
		}

		mRealm.commitTransaction();
	}

	public RealmLineData getRealmLineData(final String symbol) {
		mRealm = Realm.getDefaultInstance();

		RealmResults<QuoteData> results = mRealm
				.where(QuoteData.class)
				.equalTo(QuoteData.SYMBOL_KEY, symbol)
				.findAll();

		Log.d("REALM", results.toString());

		ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
		dataSets.add(
				new RealmLineDataSet<QuoteData>(
						results,
						QuoteData.CLOSE_KEY,
						QuoteData.ID_KEY
				)
		);

		return new RealmLineData(results, QuoteData.DATE_KEY, dataSets);
	}
}
