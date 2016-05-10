package com.sam_chordas.android.stockhawk.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sam_chordas.android.stockhawk.util.DateUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yahoofinance.histquotes.HistoricalQuote;

/**
 * Created by rgr-myrg on 5/9/16.
 */
public class QuoteDbHelper extends SQLiteOpenHelper {
	public static final String NAME = QuoteDbHelper.class.getSimpleName();

	public static final String DB_NAME = "net.usrlib.quotes.db";
	public static final int DB_VERSION = 1;

	public static final String PRIMARY_ID_KEY = QuoteEntry.TABLE_NAME + "_item_id";
	public static final String SYMBOL_EQUALS = QuoteEntry.SYMBOL_COLUMN + " = ?";

	public static final String CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ QuoteEntry.TABLE_NAME
			+ "("
				+ PRIMARY_ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ QuoteEntry.SYMBOL_COLUMN + " TEXT NOT NULL,"
				+ QuoteEntry.CLOSE_COLUMN + " REAL,"
				+ QuoteEntry.DATE_COLUMN + " TEXT"
			+ ")";

	public static final String DROP_FAVORITES_TABLE = "DROP TABLE " + QuoteEntry.TABLE_NAME;

	public static final String SELECT_QUOTES_WITH_SYMBOL = "SELECT * FROM "
			+ QuoteEntry.TABLE_NAME
			+ " WHERE " + SYMBOL_EQUALS
			+ " ORDER BY " + QuoteEntry.DATE_COLUMN
			+ " DESC";

	private static QuoteDbHelper sInstance;

	public QuoteDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_FAVORITES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_FAVORITES_TABLE);
		onCreate(db);
	}

	public List<HistoricalQuote> selectQuotesWithSymbol(final String symbol) {
		final SQLiteDatabase db = getWritableDatabase();

		if (db == null) {
			return null;
		}

		final Cursor cursor = db.rawQuery(
				SELECT_QUOTES_WITH_SYMBOL,
				new String[] {
						String.valueOf(symbol)
				}
		);

		final List<HistoricalQuote> quoteList = new ArrayList<>();

		if (cursor.moveToFirst()) {
			do {
				HistoricalQuote quote = new HistoricalQuote();

				quote.setSymbol(
						cursor.getString(cursor.getColumnIndex(QuoteEntry.SYMBOL_COLUMN))
				);

				quote.setClose(
						BigDecimal.valueOf(
								cursor.getFloat(cursor.getColumnIndex(QuoteEntry.CLOSE_COLUMN))
						)
				);

				final Calendar calendar = Calendar.getInstance();

				Log.d(NAME, "DATE: " + cursor.getString(cursor.getColumnIndex(QuoteEntry.DATE_COLUMN)));
				try {
					calendar.setTime(
							DateUtil.sDateFormat.parse(
								cursor.getString(cursor.getColumnIndex(QuoteEntry.DATE_COLUMN))
							)
					);
				} catch (ParseException e) {
					e.printStackTrace();
				}

				quote.setDate(calendar);

				quoteList.add(quote);
			} while (cursor.moveToNext());
		}

		cursor.close();

		return quoteList;
	}

	public boolean bulkInsertWithQuoteList(final List<HistoricalQuote> historicalQuoteList) {
		final SQLiteDatabase db = getWritableDatabase();

		if (db == null) {
			return false;
		}

		db.beginTransaction();

		try {
			for (int x = 0; x < historicalQuoteList.size(); x++) {
				final HistoricalQuote quote = historicalQuoteList.get(x);
				final ContentValues values = new ContentValues();

				values.put(QuoteEntry.SYMBOL_COLUMN, quote.getSymbol());
				values.put(QuoteEntry.CLOSE_COLUMN, quote.getClose().floatValue());
				values.put(QuoteEntry.DATE_COLUMN, DateUtil.sDateFormat.format(quote.getDate().getTime()));

				final long newRowId = db.insert(QuoteEntry.TABLE_NAME, null, values);
				//Log.i(NAME, "Saving date " + sDateFormat.format(quote.getDate().getTime()));
			}

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

		return true;
	}

	public static QuoteDbHelper getInstance(Context context) {
		// Use application context, to prevent accidentally leaking an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new QuoteDbHelper(context.getApplicationContext());
		}

		return sInstance;
	}
}
