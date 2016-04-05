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

	public static final String DB_NAME = "net.usrlib.movies.db";
	public static final int DB_VERSION = 1;

	public static final String PRIMARY_ID_KEY = "primary_id";
	public static final String ID_CLAUSE = "id = ?";

	public static final String CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ FavoritesEntry.TABLE_NAME
			+ "("
			+ PRIMARY_ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FavoritesEntry.COLUMN_ID + " INTEGER,"
			+ FavoritesEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL,"
			+ FavoritesEntry.COLUMN_OVERVIEW + " TEXT,"
			+ FavoritesEntry.COLUMN_VOTE_COUNT + " INTEGER,"
			+ FavoritesEntry.COLUMN_VOTE_AVERAGE + " REAL,"
			+ FavoritesEntry.COLUMN_POPULARITY + " REAL,"
			+ FavoritesEntry.COLUMN_POSTER_PATH + " TEXT,"
			+ FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT,"
			+ "UNIQUE (" + FavoritesEntry.COLUMN_ID + ") ON CONFLICT REPLACE"
			+ ")";

	public static final String DROP_FAVORITES_TABLE = "DROP TABLE " + FavoritesEntry.TABLE_NAME;

	public static final String SELECT_FAVORITES_WITH_ID = "SELECT "
			+ PRIMARY_ID_KEY
			+ " FROM " + FavoritesEntry.TABLE_NAME
			+ " WHERE " + ID_CLAUSE;

	public static final String SELECT_FAVORITES = "SELECT * FROM " + FavoritesEntry.TABLE_NAME;

	private static MoviesDBHelper sInstance;

	public MoviesDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_FAVORITES_TABLE);
		//db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL(DROP_FAVORITES_TABLE);
		//onCreate(db);
	}

	public void dropFavoritesTable() {
		final SQLiteDatabase db = getWritableDatabase();
		db.execSQL(DROP_FAVORITES_TABLE);
		db.close();
	}

	public ArrayList<MovieItemVO> selectFromFavorites() {
		ArrayList<MovieItemVO> movieItems = new ArrayList<MovieItemVO>();

		final SQLiteDatabase db = getWritableDatabase();
		final Cursor cursor = db.rawQuery(
				SELECT_FAVORITES,
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
				// Notify movie was favored with boolean true
				MovieEvent.MovieFavoriteChanged.notifySuccess(true);
			}
		} else {
			final int rows = db.delete(
					FavoritesEntry.TABLE_NAME,
					ID_CLAUSE,
					new String[]{
							String.valueOf(values.get(FavoritesEntry.COLUMN_ID))
					}
			);

			result = rows > 0;

			if (result) {
				// Notify movie was un-favored with boolean false
				MovieEvent.MovieFavoriteChanged.notifySuccess(false);
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
				SELECT_FAVORITES_WITH_ID,
				new String[] {
						String.valueOf(movieId)
				}
		);

		if (BuildConfig.DEBUG) {
			Log.d(NAME, SELECT_FAVORITES_WITH_ID
					+ String.valueOf(movieId) + " --> " + String.valueOf(cursor.getCount()));
		}

		result = cursor.getCount() != 0;

		cursor.close();
		//db.close();

		return result;
	}

	public static MoviesDBHelper getInstance(Context context) {
		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (sInstance == null) {
			sInstance = new MoviesDBHelper(context.getApplicationContext());
		}

		return sInstance;
	}
}
/*
String[] args={"5P"};
Cursor crs=db.rawQuery("SELECT * FROM CAR WHERE name = ?", args);
 */