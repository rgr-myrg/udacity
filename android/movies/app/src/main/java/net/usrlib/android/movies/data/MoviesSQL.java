package net.usrlib.android.movies.data;

import net.usrlib.android.movies.data.MoviesContract.FavoritesEntry;

public class MoviesSQL {

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

}
