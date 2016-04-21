package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;

public class ChartActivity extends AppCompatActivity {

	private ProgressBar mProgressBar;
	private String mStockSymbol;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);

		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		setStockSymbolFromIntent();
	}

	private void setStockSymbolFromIntent() {
		final Intent intent = getIntent();
		mStockSymbol = getIntent().getStringExtra(QuoteColumns.SYMBOL);
		if (mStockSymbol != null) {
			getSupportActionBar().setTitle(mStockSymbol);
		}
	}
}
