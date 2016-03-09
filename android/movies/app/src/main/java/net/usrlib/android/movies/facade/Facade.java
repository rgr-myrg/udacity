package net.usrlib.android.movies.facade;

import android.content.Context;

import net.usrlib.android.movies.data.MoviesDBHelper;
import net.usrlib.android.movies.movieapi.MovieApi;

public class Facade {
	private static Context mAppContext;
	private static MovieApi mMovieApi = null;
	private static MoviesDBHelper mMoviesDBHelper = null;

	public static void onCreate(Context context) {
		mAppContext = context;

		if (mMovieApi == null) {
			mMovieApi = new MovieApi();
		}

		if (mMoviesDBHelper == null) {
			mMoviesDBHelper = new MoviesDBHelper(mAppContext);
		}
	}

	public static MovieApi getMovieApi() {
		return mMovieApi;
	}

	public static MoviesDBHelper getMoviesDBHelper() {
		return mMoviesDBHelper;
	}

}
