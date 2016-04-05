package net.usrlib.android.movies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {

	// Ex: content://net.usrlib.android.movies._data.data/movies

	public static final String PROTOCOL_SCHEME = "content://";
	public static final String CONTENT_AUTHORITY = "net.usrlib.android.movies.provider";
	public static final String PATH_MOVIES = "movies";
	public static final String PATH_FAVORITES = "favorites";
	public static final Uri BASE_CONTENT_URI = Uri.parse(PROTOCOL_SCHEME + CONTENT_AUTHORITY);

	public static final class MoviesEntry implements BaseColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI
				.buildUpon()
				.appendPath(PATH_MOVIES)
				.build();

		public static final Uri buildMovieUriWithId(int movieId) {
			return CONTENT_URI
					.buildUpon()
					.appendPath(String.valueOf(movieId))
					.build();
		}

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/" + CONTENT_AUTHORITY
				+ "/" + PATH_MOVIES;

		public static final String getMovieId(Uri uri) {
			return uri.getPathSegments().get(1);
		}
	}

	public static final class FavoritesEntry implements BaseColumns {
		// Ex: content://net.usrlib.android.movies._data.data/favorites
		public static final Uri CONTENT_URI = BASE_CONTENT_URI
				.buildUpon()
				.appendPath(PATH_FAVORITES)
				.build();

		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
				+ "/" + CONTENT_AUTHORITY
				+ "/" + PATH_FAVORITES;

		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
				+ "/" + CONTENT_AUTHORITY
				+ "/" + PATH_FAVORITES;

		public static final String TABLE_NAME = "movie_favorites";
		public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

		public static final String COLUMN_ID = "id";
		public static final String COLUMN_ORIGINAL_TITLE = "original_title";
		public static final String COLUMN_POSTER_PATH = "poster_path";
		public static final String COLUMN_OVERVIEW = "overview";
		public static final String COLUMN_RELEASE_DATE = "release_date";
		public static final String COLUMN_VOTE_COUNT = "vote_count";
		public static final String COLUMN_VOTE_AVERAGE = "vote_average";
		public static final String COLUMN_POPULARITY = "popularity";

		public static Uri buildFavoritesUriWithId(int id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

	}

}
