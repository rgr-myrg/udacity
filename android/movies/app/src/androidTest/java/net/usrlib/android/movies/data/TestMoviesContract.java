package net.usrlib.android.movies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestMoviesContract extends AndroidTestCase {

	private static final int TEST_MOVIE_ID = 123456;
	private static final String TEST_URI = "content://net.usrlib.android.movies.data/favorites";

	public void testBuildFavoritesUri() {
		final Uri uri = MoviesContract.FavoritesEntry.buildFavoritesUriWithId(TEST_MOVIE_ID);

		assertNotNull("buildFavoritesUriWithId should not return null", uri);

		assertEquals(
				"buildFavoritesUriWithId should contain TEST_MOVIE_ID",
				String.valueOf(TEST_MOVIE_ID),
				uri.getLastPathSegment()
		);

		assertEquals(
				"buildFavoritesUriWithId should return a qualified uri",
				TEST_URI + "/" + TEST_MOVIE_ID,
				uri.toString()
		);
	}

	public void testBuildMoviesUrl() {
		final Uri uri = MoviesContract.MoviesEntry.buildMovieUriWithId(TEST_MOVIE_ID);

		assertNotNull("buildMovieUriWithId should not return null", uri);
	}

}
