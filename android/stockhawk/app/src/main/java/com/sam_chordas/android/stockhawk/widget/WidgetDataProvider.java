package com.sam_chordas.android.stockhawk.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

/**
 * Created by rgr-myrg on 4/27/16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
	public static final String NAME = WidgetDataProvider.class.getSimpleName();
	private static final String BG_RESOURCE = "setBackgroundResource";

	private Context mContext = null;
	private Cursor mCursor = null;

	public WidgetDataProvider(Context context) {
		mContext = context;
	}

	@Override
	public void onCreate() {
		Log.d(NAME, "onCreate");
	}

	@Override
	public void onDataSetChanged() {
		Log.d(NAME, "onDataSetChanged");
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
		if (mCursor != null) {
			mCursor.close();
		}
	}

	@Override
	public int getCount() {
		return mCursor != null ? mCursor.getCount() : 0;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		Log.d(NAME, "getViewAt position: " + String.valueOf(position));

		if (position == AdapterView.INVALID_POSITION
				|| mCursor == null
				|| !mCursor.moveToPosition(position)) {

			return null;
		}

		final RemoteViews remoteViews = new RemoteViews(
				mContext.getPackageName(),
				R.layout.widget_collection_item
		);

		remoteViews.setTextViewText(
				R.id.stock_symbol,
				mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL))
		);

		final String bidPrice = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.BIDPRICE));
		final String symbol = mCursor.getString(mCursor.getColumnIndex(QuoteColumns.SYMBOL));

		remoteViews.setTextViewText(R.id.bid_price, bidPrice);

		if (Utils.showPercent) {
			remoteViews.setTextViewText(
					R.id.change,
					mCursor.getString(mCursor.getColumnIndex(QuoteColumns.PERCENT_CHANGE))
			);
		} else {
			remoteViews.setTextViewText(
					R.id.change,
					mCursor.getString(mCursor.getColumnIndex(QuoteColumns.CHANGE))
			);
		}

		if (mCursor.getInt(mCursor.getColumnIndex(QuoteColumns.ISUP)) == 0) {
			remoteViews.setInt(
					R.id.change,
					BG_RESOURCE,
					R.drawable.percent_change_pill_red
			);
		} else {
			remoteViews.setInt(
					R.id.change,
					BG_RESOURCE,
					R.drawable.percent_change_pill_green
			);
		}

		final Intent intent = new Intent();
		intent.putExtra(QuoteColumns.SYMBOL, symbol);

		remoteViews.setOnClickFillInIntent(R.id.widget_list_item, intent);

		return remoteViews;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}
}
