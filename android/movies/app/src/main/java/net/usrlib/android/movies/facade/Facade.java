package net.usrlib.android.movies.facade;

import android.content.Context;

import net.usrlib.android.movies.R;
import net.usrlib.android.movies.data.MoviesDBHelper;
import net.usrlib.android.movies.movieapi.MovieApi;

public final class Facade {
	private static Context sAppContext;
	private static MovieApi sMovieApi = null;
	private static MoviesDBHelper sMoviesDBHelper = null;

	private static String sTitleMostPopular = null;
	private static String sTitleHighestRated = null;
	private static String sTitleFavorites = null;

	public static final void onCreate(Context context) {
		sAppContext = context;

		if (sMovieApi == null) {
			sMovieApi = new MovieApi();
		}

		if (sMoviesDBHelper == null) {
			sMoviesDBHelper = new MoviesDBHelper(sAppContext);
		}

		sTitleMostPopular = context.getString(R.string.title_most_popular);
		sTitleHighestRated = context.getString(R.string.title_highest_rated);
		sTitleFavorites = context.getString(R.string.title_favorites);
	}

	public static final MovieApi getMovieApi() {
		return sMovieApi;
	}

	public static final MoviesDBHelper getMoviesDBHelper() {
		return sMoviesDBHelper;
	}

	public static final Context getAppContext() {
		return sAppContext;
	}

	public static void setAppContext(Context context) {
		sAppContext = context;
	}

	public static final class Resource {

		public static final String getTitleMostPopular() {
			return sTitleMostPopular;
		}

		public static final String getTitleHighestRated() {
			return sTitleHighestRated;
		}

		public static final String getTitleFavorites() {
			return sTitleFavorites;
		}

	}
}
