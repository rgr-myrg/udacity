package net.usrlib.android.movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.usrlib.android.movies.movieapi.MovieItemKey;

public class MoviesDB extends SQLiteOpenHelper {

	public MoviesDB(Context context) {
		super(context, MoviesSQL.DB_NAME, null, MoviesSQL.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MoviesSQL.CREATE_FAVORITES_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public boolean setMovieAsFavorite(final ContentValues values, final boolean isLiked) {
		final SQLiteDatabase db = getWritableDatabase();
		boolean result = false;

		Log.d("MoviesDB", "setMovieAsFavorite isLiked: " + String.valueOf(isLiked));
		if (isLiked) {
			final long id = db.insert(MoviesSQL.TABLE_MOVIE_FAVORITES, null, values);
			result = id != -1;
		} else {
			final int rows = db.delete(
					MoviesSQL.TABLE_MOVIE_FAVORITES,
					MoviesSQL.ID_CLAUSE,
					new String[] {
							String.valueOf(values.get(MovieItemKey.ID))
					}
			);

			result = rows > 0;
		}

		Log.d("MoviesDB", "setMovieAsFavorite result: " + String.valueOf(result));
		return result;
	}

	public boolean isMovieSetAsFavorite(final int movieId) {
		final SQLiteDatabase db = getWritableDatabase();
		boolean result;

		final Cursor cursor = db.rawQuery(
				MoviesSQL.SELECT_WITH_ID,
				new String[] {
						String.valueOf(movieId)
				}
		);

		Log.d("MoviesDB", MoviesSQL.SELECT_WITH_ID + String.valueOf(movieId) + " --> " + String.valueOf(cursor.getCount()));

		result = cursor.getCount() != 0;

		cursor.close();

		return result;
	}

}
/*
String[] args={"5P"};
Cursor crs=db.rawQuery("SELECT * FROM CAR WHERE name = ?", args);
 */