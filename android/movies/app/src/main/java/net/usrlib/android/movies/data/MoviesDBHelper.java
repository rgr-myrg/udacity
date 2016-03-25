package net.usrlib.android.movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.usrlib.android.movies.BuildConfig;
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
						cursor.getInt(cursor.getColumnIndex(MovieItemVO.ID)),
						cursor.getString(cursor.getColumnIndex(MovieItemVO.ORIGINAL_TITLE)),
						cursor.getString(cursor.getColumnIndex(MovieItemVO.POSTER_PATH)),
						cursor.getString(cursor.getColumnIndex(MovieItemVO.OVERVIEW)),
						cursor.getString(cursor.getColumnIndex(MovieItemVO.RELEASE_DATE)),
						cursor.getInt(cursor.getColumnIndex(MovieItemVO.VOTE_COUNT)),
						cursor.getDouble(cursor.getColumnIndex(MovieItemVO.VOTE_AVERAGE)),
						cursor.getDouble(cursor.getColumnIndex(MovieItemVO.POPULARITY))
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
			final long id = db.insert(MoviesSQL.TABLE_MOVIE_FAVORITES, null, values);
			result = id != -1;

			if (result) {
				MovieEvent.MovieSetAsFavorite.notifyComplete(null);
			}
		} else {
			final int rows = db.delete(
					MoviesSQL.TABLE_MOVIE_FAVORITES,
					MoviesSQL.ID_CLAUSE,
					new String[]{
							String.valueOf(values.get(MovieItemVO.ID))
					}
			);

			result = rows > 0;

			if (result) {
				MovieEvent.MovieUnsetAsFavorite.notifyComplete(null);
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