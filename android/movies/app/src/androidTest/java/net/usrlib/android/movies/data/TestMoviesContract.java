package net.usrlib.android.movies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

public class TestMoviesContract extends AndroidTestCase {

	private static final int TEST_MOVIE_ID = 123456;
	private static final String TEST_URI = "content://net.usrlib.android.movies.data/favorites";

	public void testBuildFavoritesUri() {
		final Uri uri = MoviesContract.FavoritesEntry.buildFavoritesUri(TEST_MOVIE_ID);

		assertNotNull("buildFavoritesUri returns null", uri);

		assertEquals(
				"buildFavoritesUri should contain TEST_MOVIE_ID",
				String.valueOf(TEST_MOVIE_ID),
				uri.getLastPathSegment()
		);

		assertEquals(
				"buildFavoritesUri should return a qualified uri",
				TEST_URI + "/" + TEST_MOVIE_ID,
				uri.toString()
		);
	}

}
