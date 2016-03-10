package net.usrlib.android.movies.facade;

import android.content.Context;

import net.usrlib.android.movies.R;
import net.usrlib.android.movies.data.MoviesDBHelper;
import net.usrlib.android.movies.movieapi.MovieApi;

public class Facade {
	private static Context mAppContext;
	private static MovieApi mMovieApi = null;
	private static MoviesDBHelper mMoviesDBHelper = null;
	private static String titleMostPopular;
	private static String titleHighestRated;
	private static String titleFavorites;

	public static final void onCreate(Context context) {
		mAppContext = context;

		if (mMovieApi == null) {
			mMovieApi = new MovieApi();
		}

		if (mMoviesDBHelper == null) {
			mMoviesDBHelper = new MoviesDBHelper(mAppContext);
		}

		titleMostPopular = context.getString(R.string.title_most_popular);
		titleHighestRated = context.getString(R.string.title_highest_rated);
		titleFavorites = context.getString(R.string.title_favorites);
	}

	public static final MovieApi getMovieApi() {
		return mMovieApi;
	}

	public static final MoviesDBHelper getMoviesDBHelper() {
		return mMoviesDBHelper;
	}

	public static final boolean isTitleFavorites(String title) {
		return title.contentEquals(titleFavorites);
	}

	public static final class Resource {

		public static final String getTitleMostPopular() {
			return titleMostPopular;
		}

		public static final String getTitleHighestRated() {
			return titleHighestRated;
		}

		public static final String getTitleFavorites() {
			return titleFavorites;
		}

	}
}
