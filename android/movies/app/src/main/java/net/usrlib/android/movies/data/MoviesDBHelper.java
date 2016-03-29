package net.usrlib.android.movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.usrlib.android.movies.BuildConfig;
import net.usrlib.android.movies.data.MoviesContract.FavoritesEntry;
import net.usrlib.android.movies.movieapi.MovieEvent;
import net.usrlib.android.movies.parcelable.MovieItemVO;

import java.util.ArrayList;

public class MoviesDBHelper extends SQLiteOpenHelper {
	public static final String NAME = MoviesDBHelper.class.getSimpleName();

	public MoviesDBHelper(Context context) {
		super(context, MoviesSQL.DB_NAME, null, MoviesSQL.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MoviesSQL.CREATE_FAVORITES_TABLE);
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL(MoviesSQL.DROP_FAVORITES_TABLE);
		//onCreate(db);
	}

//	public void dropFavoritesTable() {
//		final SQLiteDatabase db = getWritableDatabase();
//		db.execSQL(MoviesSQL.DROP_FAVORITES_TABLE);
//		db.close();
//	}

	public ArrayList<MovieItemVO> selectFromFavorites() {
		ArrayList<MovieItemVO> movieItems = new ArrayList<MovieItemVO>();

		final SQLiteDatabase db = getWritableDatabase();
		final Cursor cursor = db.rawQuery(
				MoviesSQL.SELECT_FAVORITES,
				null
		);

		if (cursor.moveToFirst()) {
			do {
				movieItems.add(new MovieItemVO(
						cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_ID)),
						cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_ORIGINAL_TITLE)),
						cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_POSTER_PATH)),
						cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_OVERVIEW)),
						cursor.getString(cursor.getColumnIndex(FavoritesEntry.COLUMN_RELEASE_DATE)),
						cursor.getInt(cursor.getColumnIndex(FavoritesEntry.COLUMN_VOTE_COUNT)),
						cursor.getDouble(cursor.getColumnIndex(FavoritesEntry.COLUMN_VOTE_AVERAGE)),
						cursor.getDouble(cursor.getColumnIndex(FavoritesEntry.COLUMN_POPULARITY))
				));
			} while (cursor.moveToNext());
		}

		cursor.close();
		//db.close();

		if (BuildConfig.DEBUG) Log.d(NAME, "selected " + String.valueOf(movieItems.size()));

		return movieItems;
	}

	public boolean setMovieAsFavorite(final ContentValues values, final boolean isLiked) {
		final SQLiteDatabase db = getWritableDatabase();
		boolean result;

		if (isLiked) {
			final long id = db.insert(FavoritesEntry.TABLE_NAME, null, values);
			result = id != -1;

			if (result) {
				MovieEvent.MovieSetAsFavorite.notifySuccess();
			}
		} else {
			final int rows = db.delete(
					FavoritesEntry.TABLE_NAME,
					MoviesSQL.ID_CLAUSE,
					new String[]{
							String.valueOf(values.get(FavoritesEntry.COLUMN_ID))
					}
			);

			result = rows > 0;

			if (result) {
				MovieEvent.MovieUnsetAsFavorite.notifySuccess();
			}
		}

		//db.close();

		if (BuildConfig.DEBUG) Log.d(NAME, "setMovieAsFavorite result: " + String.valueOf(result));
		return result;
	}

	public boolean isMovieSetAsFavorite(final int movieId) {
		final SQLiteDatabase db = getWritableDatabase();
		boolean result;

		final Cursor cursor = db.rawQuery(
				MoviesSQL.SELECT_FAVORITES_WITH_ID,
				new String[] {
						String.valueOf(movieId)
				}
		);

		if (BuildConfig.DEBUG) {
			Log.d(NAME, MoviesSQL.SELECT_FAVORITES_WITH_ID
					+ String.valueOf(movieId) + " --> " + String.valueOf(cursor.getCount()));
		}

		result = cursor.getCount() != 0;

		cursor.close();
		//db.close();

		return result;
	}

}
/*
String[] args={"5P"};
Cursor crs=db.rawQuery("SELECT * FROM CAR WHERE name = ?", args);
 */