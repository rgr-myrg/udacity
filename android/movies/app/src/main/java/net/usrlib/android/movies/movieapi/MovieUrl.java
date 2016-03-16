package net.usrlib.android.movies.movieapi;

import android.net.Uri;

import net.usrlib.android.movies.BuildConfig;

public final class MovieUrl {

	// http://api.themoviedb.org/3/discover/movie?sort_by=vote_count.desc&api_key=3ea54608cea21741f6b4b79f70cdc8c5&page=1
	public static final String getDiscoverUrl(final String sortBy, final int pageNumber) {
		Uri uri = Uri.parse(MovieVars.DISCOVER_MOVIE_URL).buildUpon()
				.appendQueryParameter(MovieVars.SORT_PARAM_KEY, sortBy)
				.appendQueryParameter(MovieVars.PAGE_PARAM_KEY, String.valueOf(pageNumber))
				.appendQueryParameter(MovieVars.API_PARAM_KEY, BuildConfig.THE_MOVIE_DB_KEY)
				.build();

		return uri.toString();
	}

	// http://api.themoviedb.org/3/movie/27205/videos?api_key=3ea54608cea21741f6b4b79f70cdc8c5
	public static final String getTrailersUrl(final int id) {
		String videosUrl = MovieVars.TRAILERS_URL.replace(
				MovieVars.ID_TOKEN, String.valueOf(id)
		);

		Uri uri = Uri.parse(videosUrl).buildUpon()
				.appendQueryParameter(MovieVars.API_PARAM_KEY, BuildConfig.THE_MOVIE_DB_KEY)
				.build();

		return uri.toString();
	}

	// http://api.themoviedb.org/3/movie/27205/reviews?api_key=3ea54608cea21741f6b4b79f70cdc8c5
	public static final String getReviewsUrl(final int id) {
		String videosUrl = MovieVars.REVIEWS_URL.replace(
				MovieVars.ID_TOKEN, String.valueOf(id)
		);

		Uri uri = Uri.parse(videosUrl).buildUpon()
				.appendQueryParameter(MovieVars.API_PARAM_KEY, BuildConfig.THE_MOVIE_DB_KEY)
				.build();

		return uri.toString();
	}

}
