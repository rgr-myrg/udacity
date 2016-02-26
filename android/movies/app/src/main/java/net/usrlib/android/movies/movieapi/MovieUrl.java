package net.usrlib.android.movies.movieapi;

import android.net.Uri;

import net.usrlib.android.movies.BuildConfig;

public class MovieUrl {

	public static final String getUrl(String sortBy, int pageNumber) {
		Uri uri = Uri.parse(MovieVars.API_URL).buildUpon()
				.appendQueryParameter(MovieVars.SORT_PARAM_KEY, sortBy)
				.appendQueryParameter(MovieVars.PAGE_PARAM_KEY, String.valueOf(pageNumber))
				.appendQueryParameter(MovieVars.API_PARAM_KEY, BuildConfig.THE_MOVIE_DB_KEY)
				.build();

		return uri.toString();
	}

}
