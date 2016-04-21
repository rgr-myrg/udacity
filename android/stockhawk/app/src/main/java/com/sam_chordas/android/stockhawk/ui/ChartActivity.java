package com.sam_chordas.android.stockhawk.ui;

import android.os.Bundle;
import android.app.Activity;

import com.sam_chordas.android.stockhawk.R;

public class ChartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chart);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
