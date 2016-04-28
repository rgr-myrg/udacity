package com.sam_chordas.android.stockhawk.ui;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.sam_chordas.android.stockhawk.R;


public class ChartMarkerView extends MarkerView {
	private TextView mTextView;

	public ChartMarkerView(Context context, int layoutResource) {
		super(context, layoutResource);

		mTextView = (TextView) findViewById(R.id.chart_marker_view);
	}

	// Invoked when MarkerView is redrawn
	@Override
	public void refreshContent(Entry entry, Highlight highlight) {
		float entryValue = entry.getVal();

		if (entry instanceof CandleEntry) {
			final CandleEntry candleEntry = (CandleEntry) entry;
			entryValue = candleEntry.getHigh();
		}

		mTextView.setText(Utils.formatNumber(entryValue, 0, true));
	}

	@Override
	public int getXOffset(float xpos) {
		// Center MarkerView Horizontally
		return - (getWidth() / 2);
	}

	@Override
	public int getYOffset(float ypos) {
		// Position MarkerView above selected value
		return - getHeight();
	}
}
