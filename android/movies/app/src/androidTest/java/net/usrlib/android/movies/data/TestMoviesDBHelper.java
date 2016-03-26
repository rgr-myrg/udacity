package net.usrlib.android.movies.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

public class TestMoviesDBHelper extends AndroidTestCase {

	public void testCreateDb() {
		final HashSet<String> tables = new HashSet<String>();
		tables.add(MoviesContract.FavoritesEntry.TABLE_NAME);

		SQLiteDatabase db = new MoviesDBHelper(mContext).getWritableDatabase();

		assertEquals(
				"getWritableDatabase should return a database instance",
				true,
				db.isOpen()
		);

		Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

		assertTrue(
				"Cursor should return at least one table",
				cursor.moveToFirst()
		);

		do {
			tables.remove(cursor.getString(0));
		} while (cursor.moveToNext());

		assertTrue(
				"Cursor should return at least one table",
				tables.isEmpty()
		);

		cursor = db.rawQuery(
				"PRAGMA table_info(" + MoviesContract.FavoritesEntry.TABLE_NAME + ")",
				null
		);

		assertTrue(
				"Pragma should return table information",
				cursor.moveToFirst()
		);

		final HashSet<String> columns = new HashSet<String>();

		columns.add(MoviesContract.FavoritesEntry.COLUMN_ID);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_OVERVIEW);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_POPULARITY);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_POSTER_PATH);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_RELEASE_DATE);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE);
		columns.add(MoviesContract.FavoritesEntry.COLUMN_VOTE_COUNT);

		int columnIndex = cursor.getColumnIndex("name");

		do {
			columns.remove(cursor.getString(columnIndex));
		} while(cursor.moveToNext());

		assertTrue(
				"Cursor should contain all the columns",
				columns.isEmpty()
		);

		db.close();
	}

}
