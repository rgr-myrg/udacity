package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by rgr-myrg on 4/27/16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
	private Context mContext = null;
	private Cursor mCursor = null;

	public WidgetDataProvider(Context context) {
		mContext = context;
	}

	@Override
	public void onCreate() {

	}

	@Override
	public void onDataSetChanged() {
		if (mCursor != null) {
			mCursor.close();
		}

		mCursor = mContext
				.getContentResolver()
				.query(
						QuoteProvider.Quotes.CONTENT_URI,
						new String[]{
								QuoteColumns.SYMBOL,
								QuoteColumns.BIDPRICE,
								QuoteColumns.PERCENT_CHANGE,
								QuoteColumns.CHANGE,
								QuoteColumns.ISUP
						},
						QuoteColumns.ISCURRENT + " = ?",
						new String[]{"1"},
						null
				);

		final long binderToken = Binder.clearCallingIdentity();
		Binder.restoreCallingIdentity(binderToken);
	}

	@Override
	public void onDestroy() {

	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		return null;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}
}
