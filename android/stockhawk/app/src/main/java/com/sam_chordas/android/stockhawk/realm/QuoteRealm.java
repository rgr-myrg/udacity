package com.sam_chordas.android.stockhawk.realm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.data.realm.implementation.RealmBarData;
import com.github.mikephil.charting.data.realm.implementation.RealmBarDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

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

	public QuoteRealm() {
	}

	public void onResume(AppCompatActivity app) {
		RealmConfiguration config = new RealmConfiguration.Builder(app)
				.name("net.usrlib.realm")
				.build();

		Realm.deleteRealm(config);
		Realm.setDefaultConfiguration(config);

		mRealm = Realm.getInstance(config);
	}

	public void close() {
		mRealm.close();
	}

	public void saveHistoricalQuoteList(final List<HistoricalQuote> quotes) {
		mRealm.executeTransactionAsync(
				new Realm.Transaction() {
					@Override
					public void execute(Realm realm) {
						for (int x = 0; x < quotes.size(); x++) {
							mRealm.copyToRealm(
									QuoteData.fromHistoricalQuote(quotes.get(x))
							);
						}
					}
				}
		);
	}

	public RealmBarData getResultsAsRealmBarData(final String symbol) {
		//RealmResults<QuoteData> results = mRealm.allObjects(QuoteData.class);
		RealmResults<QuoteData> results = mRealm
				.where(QuoteData.class)
				.equalTo("mSymbol", symbol)
				.findAll();

		RealmBarDataSet<QuoteData> set = new RealmBarDataSet<>(results, "mClose", "mId");

		set.setColors(
				new int[] {
						ColorTemplate.rgb("#FF5722"),
						ColorTemplate.rgb("#03A9F4")
				}
		);
		set.setLabel("Realm BarDataSet");

		ArrayList<IBarDataSet> dataSets = new ArrayList<>();
		dataSets.add(set);

		RealmBarData data = new RealmBarData(results, "xValue", dataSets);
		//data.setValueTypeface(mTf);
		data.setValueTextSize(8f);
		data.setValueTextColor(Color.DKGRAY);
		data.setValueFormatter(new PercentFormatter());
		return data;
	}
}
/*
 private void setData() {

        RealmResults<RealmDemoData> result = mRealm.allObjects(RealmDemoData.class);

        //RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "stackValues", "xIndex"); // normal entries
        RealmBarDataSet<RealmDemoData> set = new RealmBarDataSet<RealmDemoData>(result, "value", "xIndex"); // stacked entries
        set.setColors(new int[] {ColorTemplate.rgb("#FF5722"), ColorTemplate.rgb("#03A9F4")});
        set.setLabel("Realm BarDataSet");

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set); // add the dataset

        // create a data object with the dataset list
        RealmBarData data = new RealmBarData(result, "xValue", dataSets);
        styleData(data);

        // set data
        mChart.setData(data);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuart);
    }
 */