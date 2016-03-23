package net.usrlib.android.movies.facade;

import android.content.Context;

import net.usrlib.android.movies.data.MoviesDBHelper;
import net.usrlib.android.movies.movieapi.MovieApi;
import net.usrlib.android.movies.viewholder.ResourceHolder;

public final class Facade {
	private static Context sAppContext;
	private static MovieApi sMovieApi = null;
	private static MoviesDBHelper sMoviesDBHelper = null;

	public static final void onCreate(Context context) {
		sAppContext = context;

		if (sMovieApi == null) {
			sMovieApi = new MovieApi();
		}

		if (sMoviesDBHelper == null) {
			sMoviesDBHelper = new MoviesDBHelper(sAppContext);
		}

		ResourceHolder.onCreate(context);
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

}
