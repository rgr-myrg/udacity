package net.usrlib.android.movies.data;

import net.usrlib.android.movies.movieapi.MovieItemVO;

public class MoviesSQL {

	public static final String DB_NAME = "net.usrlib.movies.db";
	public static final int DB_VERSION = 1;

	public static final String PRIMARY_ID_KEY = "primary_id";
	public static final String ID_CLAUSE = "id = ?";
	public static final String TABLE_MOVIE_FAVORITES = "movie_favorites";
	public static final String CREATE_FAVORITES_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_MOVIE_FAVORITES
			+ "("
				+ PRIMARY_ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MovieItemVO.ID + " INTEGER,"
				+ MovieItemVO.ORIGINAL_TITLE + " TEXT NOT NULL,"
				+ MovieItemVO.OVERVIEW + " TEXT,"
				+ MovieItemVO.VOTE_COUNT + " INTEGER,"
				+ MovieItemVO.VOTE_AVERAGE + " REAL,"
				+ MovieItemVO.POPULARITY + " REAL,"
				+ MovieItemVO.POSTER_PATH + " TEXT,"
				+ MovieItemVO.RELEASE_DATE + " TEXT,"
				+ "UNIQUE (" + MovieItemVO.ID + ") ON CONFLICT REPLACE"
			+ ")";

	public static final String DROP_FAVORITES_TABLE = "DROP TABLE " + TABLE_MOVIE_FAVORITES;

	public static final String SELECT_FAVORITES_WITH_ID = "SELECT "
			+ PRIMARY_ID_KEY
			+ " FROM " + TABLE_MOVIE_FAVORITES
			+ " WHERE " + ID_CLAUSE;

	public static final String SELECT_FAVORITES = "SELECT * FROM " + TABLE_MOVIE_FAVORITES;

}
